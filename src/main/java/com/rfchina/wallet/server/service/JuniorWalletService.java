package com.rfchina.wallet.server.service;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogType;
import com.rfchina.wallet.server.service.handler.HandlerHelper;

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
	private WalletLogExtDao walletLogDao;

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

			WalletCard walletCard = getWalletCard(payInReq.getWalletId());
			if (walletCard == null) {
				throw new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(payInReq.getWalletId()));
			}

			payInReq.setElecChequeNo(IdGenerator.createBizId("", 16, id -> true));
			payInReq.setBatchNo(batchNo);

			WalletLog walletLog = WalletLog.builder()
				.walletId(payInReq.getWalletId())
				.type(WalletLogType.TRANSFER.getValue())
				.amount(payInReq.getAmount())
				.payerAccount(cmpAcctNo)
				.payeeType(walletCard.getIsPublic())
				.payeeAccount(walletCard.getBankAccount())
				.batchNo(payInReq.getBatchNo())
				.bizNo(payInReq.getBizNo())
				.elecChequeNo(payInReq.getElecChequeNo())
				.payPurpose(payInReq.getPayPurpose() != null ?
					String.valueOf(payInReq.getPayPurpose()) : null)
				.note(payInReq.getNote())
				.status(WalletLogStatus.SENDING.getValue())
				.queryTime(DateUtil.addSecs(new Date(), configService.getNextRoundSec()))
				.createTime(new Date())
				.build();

			walletLogDao.insertSelective(walletLog);
		});

		return PayInResp.builder()
			.batchNo(batchNo)
			.build();
	}


	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}


}
