package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.passport.misc.SessionThreadLocal;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletFinanceExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class JuniorPayService {

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private ConfigService configService;

	@Value("${wlpay.pudong.acctno}")
	private String cmpAcctNo;


	public static final String PREFIX_FINANCE = "WF";

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Autowired
	private WalletFinanceExtDao walletFinanceDao;

	@Autowired
	private SessionThreadLocal sessionThreadLocal;

	/**
	 * 出佣到个人钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PayInResp payIn(List<PayInReq> payInReqs) {
		// 出佣请求不能为空, 数量不能大于20
		if (payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		// 保存记录
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> {
			return walletOrderDao.selectCountByBatchNo(id) == 0;
		});
		payInReqs.forEach(payInReq -> {

			// 金额大于零
			if (payInReq.getAmount() <= 0) {
				throw new WalletResponseException(
					EnumWalletResponseCode.PAY_IN_AMOUNT_ERROR);
			}
			// 用户必须已经绑卡
			WalletCard walletCard = getDefWalletCard(payInReq.getWalletId());
			Optional.ofNullable(walletCard)
				.orElseThrow(
					() -> new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
						, String.valueOf(payInReq.getWalletId())));

			String orderNo = IdGenerator.createBizId(PREFIX_FINANCE, 19, id -> {
				return walletOrderDao.selectCountByOrderNo(id) == 0;
			});

			WalletOrder order = WalletOrder.builder()
				.orderNo(orderNo)
				.batchNo(batchNo)
				.bizNo(payInReq.getBizNo())
				.walletId(payInReq.getWalletId())
				.type(OrderType.FINANCE.getValue())
				.amount(payInReq.getAmount())
				.note(payInReq.getNote())
				.progress(GwProgress.WAIT_SEND.getValue())
				.status(OrderStatus.WAITTING.getValue())
				.tunnelType(TunnelType.PUDONG.getValue())
				.sourceAppId(sessionThreadLocal.getApp().getId())
				.createTime(new Date())
				.build();
			walletOrderDao.insertSelective(order);
			// 生成财务结算单
			WalletFinance walletFinance = WalletFinance.builder()
				.orderId(order.getId())
				.payerAccount(cmpAcctNo)
				.payPurpose(payInReq.getPayPurpose() != null ?
					String.valueOf(payInReq.getPayPurpose()) : null)
				.build();
			walletFinanceDao.insertSelective(walletFinance);
		});

		return PayInResp.builder()
			.batchNo(batchNo)
			.build();
	}


	private WalletCard getDefWalletCard(Long walletId) {
		return walletCardDao.selectDefCardByWalletId(walletId);
	}


	public List<Triple<WalletOrder, WalletFinance, GatewayTrans>> updateOrderStatus(
		String batchNo) {
		List<WalletOrder> walletOrders = walletOrderDao.selectByBatchNo(batchNo);
		// 如果没有处理中，则结束
		walletOrders = walletOrders.stream()
			.filter(
				w -> w != null && w.getStatus().byteValue() == OrderStatus.WAITTING.getValue())
			.collect(Collectors.toList());

		// 选择处理器
		EBankHandler handler = handlerHelper
			.selectByTunnelType(walletOrders.get(0).getTunnelType());
		try {
			for (WalletOrder walletOrder : walletOrders) {
				log.info("开始更新初级订单 [{}]", walletOrder.getOrderNo());
				walletOrderDao.incTryTimes(walletOrder.getOrderNo());
			}
			return handler.updateOrderStatus(walletOrders);
		} catch (Exception e) {
			log.error("定时更新支付状态, 异常！", e);
			throw new WalletResponseException(EnumWalletResponseCode.UNDEFINED_ERROR);
		}
	}

	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	public List<PayStatusResp> sendMQ(List<PayStatusResp> resps) {
		return resps;
	}
}
