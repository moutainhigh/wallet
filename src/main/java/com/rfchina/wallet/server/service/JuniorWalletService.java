package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.EnumDef.OrderType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class JuniorWalletService {

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private ConfigService configService;

	@Value("${wlpay.pudong.acctno}")
	private String cmpAcctNo;

	/**
	 * 出佣到个人钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public PayInResp payIn(List<PayInReq> payInReqs) {
		// 出佣请求不能为空, 数量不能大于20
		if (payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		// 记录钱包流水
		String batchNo = IdGenerator.createBizId(IdGenerator.PREFIX_WALLET, 20, id -> true);
		payInReqs.forEach(payInReq -> {

			// 金额大于零
			if (payInReq.getAmount() <= 0) {
				throw new WalletResponseException(
					EnumWalletResponseCode.PAY_IN_AMOUNT_ERROR);
			}
			// 用户必须已经绑卡
			WalletCard walletCard = getDefWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(payInReq.getWalletId()));
			}
			payInReq.setBatchNo(batchNo);

			WalletApply walletApply = WalletApply.builder()
				.walletId(payInReq.getWalletId())
				.type(OrderType.SETTLE.getValue())
				.amount(payInReq.getAmount())
				.payerAccount(cmpAcctNo)
				.batchNo(payInReq.getBatchNo())
				.bizNo(payInReq.getBizNo())
				.payPurpose(payInReq.getPayPurpose() != null ?
					String.valueOf(payInReq.getPayPurpose()) : null)
				.note(payInReq.getNote())
				.status(WalletApplyStatus.WAIT_SEND.getValue())
				.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
				.createTime(new Date())
				.build();

			walletApplyDao.insertSelective(walletApply);
		});

		return PayInResp.builder()
			.batchNo(batchNo)
			.build();
	}


	private WalletCard getDefWalletCard(Long walletId) {
		return walletCardDao.selectDefCardByWalletId(walletId);
	}


}
