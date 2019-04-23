package com.rfchina.wallet.server.service;

import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.domain.model.WalletLogCriteria;
import com.rfchina.wallet.domain.model.WalletLogCriteria.Criteria;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.model.ext.AcceptNo;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import com.rfchina.wallet.server.service.handler.Handler8800;
import com.rfchina.wallet.server.service.handler.HandlerAQ52;
import com.rfchina.wallet.server.service.handler.HandlerHelper;
import com.rfchina.wallet.server.service.handler.PuDongHandler;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Service
public class JuniorWalletService {

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletLogExtDao walletLogDao;

	@Autowired
	private WalletExtDao walletDao;


	@Autowired
	private HandlerHelper handlerHelper;

	@Value("${wlpay.pudong.acctno}")
	private String cmpAcctNo;

	/**
	 * 出佣到个人钱包
	 */
	public PayInResp payIn(List<PayInReq> payInReqs) {
		// 出佣请求不能为空, 数量不能大于20
		if (payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new RuntimeException();
		}

		// 钱包类型唯一
		List<Long> walletIds = payInReqs.stream()
			.map(payInReq -> payInReq.getWalletId())
			.distinct()
			.collect(Collectors.toList());
		List<Byte> walletTypes = walletDao.selectWalletType(walletIds);
		if (walletTypes.size() != 1) {
			throw new RuntimeException();
		}
		Byte walletType = walletTypes.get(0);

		// 记录钱包流水
		List<WalletLog> walletLogs = payInReqs.stream().map(payInReq -> {

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new RuntimeException();
			}

			WalletLog walletLog = WalletLog.builder()
				.walletId(payInReq.getWalletId())
				.type(WalletLogType.TRANSFER.getValue())
				.amount(payInReq.getAmount())
				.payerAccount(cmpAcctNo)
				.payeeType(walletType)
				.payeeAccount(walletCard.getBankAccount())
				.elecChequeNo(payInReq.getElecChequeNo())
				.status(WalletLogStatus.SENDING.getValue())
				.createTime(new Date())
				.build();

			walletLogDao.insert(walletLog);
			return walletLog;
		}).collect(Collectors.toList());

		// 请求网关
		try {
			PuDongHandler puDongHandler = handlerHelper.selectByWalletType(walletType);
			Tuple<GatewayMethod, PayInResp> rs = puDongHandler.pay(payInReqs);

			for (WalletLog walletLog : walletLogs) {
				walletLogDao.updateStatusAndAcceptNo(walletLog.getId(),
					WalletLogStatus.PROCESSING.getValue(), rs.right.getAcceptNo(),
					rs.left.getValue());
			}

			return rs.right;
		} catch (Exception e) {
			log.error("支付错误", e);
			throw new RuntimeException(e);
		}


	}

	/**
	 * 查询出佣结果
	 */
	public List<PayStatusResp> query(String elecChequeNo, String acceptNo) {

		WalletLogCriteria example = new WalletLogCriteria();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(elecChequeNo)) {
			criteria.andElecChequeNoEqualTo(elecChequeNo);
		}
		if (!StringUtils.isEmpty(acceptNo)) {
			criteria.andAcceptNoEqualTo(acceptNo);
		}

		List<WalletLog> walletLogs = walletLogDao.selectByExample(example);
		if (walletLogs.isEmpty()) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST);
		}

		boolean wantToQuery = walletLogs.stream().map(walletLog -> walletLog.getStatus())
			.anyMatch(status -> status.byteValue() != WalletLogStatus.SUCC.getValue());

		if (!wantToQuery) {
			// notify query
		}

		return walletLogs.stream().map(walletLog -> {
			return PayStatusResp.builder()
				.acceptNo(walletLog.getAcceptNo())
				.elecChequeNo(walletLog.getElecChequeNo())
				.transDate(DateUtil.formatDate(walletLog.getCreateTime()))
				.status(walletLog.getStatus())
				.errMsg(walletLog.getErrMsg())
				.build();
		}).collect(Collectors.toList());

	}


	/**
	 * 定时更新支付状态
	 */
	public void quartzUpdate() {

		log.info("scheduler: 开始更新支付状态[银企直连]");

		List<AcceptNo> acceptNos = walletLogDao.selectUnFinish();

		int count = 0;
		for (AcceptNo item : acceptNos) {
			PuDongHandler handler = handlerHelper.selectByMethod(item.getRefMethod());
			int c = handler.updatePayStatus(item.getAcceptNo(), item.getCreateTime());
			count += c;
		}

		log.info("更新批次状态，批次数量= {}，更新笔数= {}", acceptNos.size(), count);
		log.info("scheduler: 结束更新支付状态[银企直连]");
	}


	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}
}
