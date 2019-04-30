package com.rfchina.wallet.server.service.handler;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody.PayResult;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.mapper.ext.BankCodeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus8800;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.msic.MqConstant;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 8800处理器
 *
 * @author nzm
 */
@Component
@Slf4j
@Data
public class Handler8800 implements PuDongHandler {

	@Value("${wlpay.pudong.masterid}")
	private String cmpMasterId;

	@Value("${wlpay.pudong.acctno}")
	private String cmpAcctNo;

	@Value("${wlpay.pudong.acctname}")
	private String cmpAcctName;

	@Value("${wlpay.pudong.acctAreaCode}")
	private String cmpAcctAreaCode;

	@Value("${wlpay.pudong.acctBankCode}")
	private String cmpAcctBankCode;

	@Value("${wlpay.pudong.auditMasterId}")
	private String auditMasterId;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletLogExtDao walletLogDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private BankCodeExtDao bankCodeExtDao;

	private PuDongHandler next;


	@Override
	public boolean isSupportWalletType(Byte walletType) {
//		return WalletType.COMPANY.getValue().byteValue() == walletType;
		return true;
	}

	@Override
	public boolean isSupportMethod(Byte method) {
		return GatewayMethod.PUDONG_8800.getValue().byteValue() == method;
	}

	@Override
	public GatewayMethod getGatewayMethod() {
		return GatewayMethod.PUDONG_8800;
	}


	@Override
	public Tuple<GatewayMethod, PayInResp> pay(List<WalletLog> payInReqs) throws Exception {
		int limit = 20;
		if (payInReqs.size() > limit) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		List<PubPayReq> payReqs = payInReqs.stream().map(walletLog -> {

			WalletCard walletCard = getWalletCard(walletLog.getWalletId());
			if (walletCard == null) {
				throw new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(walletLog.getWalletId()));
			}

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(walletLog.getAmount())
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_DOWN);

			BankCode bankCode = bankCodeExtDao.selectByCode(walletCard.getBankCode());
			if(bankCode == null){
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
			}

			String sysFlag = bankCode.getClassCode().equals(cmpAcctBankCode) ?
				SysFlag.SELF.getValue() : SysFlag.OTHER.getValue();

			String remitLocation = bankCode.getAreaCode().equals(cmpAcctAreaCode) ?
				RemitLocation.SELF.getValue() : RemitLocation.OTHER.getValue();

			boolean isOtherRemit = SysFlag.OTHER.getValue().equals(sysFlag)
				&& RemitLocation.OTHER.getValue().equals(remitLocation);

			return PubPayReq.builder()
				.elecChequeNo(walletLog.getElecChequeNo())
				.acctNo(cmpAcctNo)
				.acctName(cmpAcctName)
				.payeeAcctNo(walletCard.getBankAccount())
				.payeeName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.sysFlag(sysFlag)
				.remitLocation(remitLocation)
				.note(walletLog.getNote())
				.payeeBankSelectFlag(isOtherRemit ? "1" : null)
				.payeeBankNo(isOtherRemit ? walletCard.getBankCode() : null)
				.payPurpose(
					walletLog.getPayPurpose() != null ? walletLog.getPayPurpose().toString() : null)
				.build();

		}).collect(Collectors.toList());

		String packetId = IdGenerator
			.createBizId(IdGenerator.PREFIX_WALLET, IdGenerator.DEFAULT_LENGTH, (orderId) -> true);
		PubPayReqBuilder req = PubPayReqBuilder
			.builder()
			.masterId(cmpMasterId)
			.packetId(packetId)
			.authMasterId(auditMasterId)
			.packageNo(packetId)
			.payList(payReqs)
			.build();

		PubPayRespBody resp = req.lanch(new Builder().build());
		PayInResp payInResp = BeanUtil.newInstance(resp, PayInResp.class);

		return new Tuple<>(getGatewayMethod(), payInResp);
	}

	@Override
	public List<WalletLog> updatePayStatus(String acceptNo, Date createTime) {
		Date endDate = DateUtil.addDate2(createTime, 7);

		PubPayQueryBuilder req = PubPayQueryBuilder.builder()
			.masterId(cmpMasterId)
			.acctNo(cmpAcctNo)
			.beginDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.endDate(DateUtil.formatDate(endDate, "yyyyMMdd"))
			.queryNumber("30")
			.beginNumber("1")
			.acceptNo(acceptNo)
			.build();

		PubPayQueryRespBody respBody;
		try {
			respBody = req.lanch(new Builder().build());
		} catch (Exception e) {
			log.error("银行网关支付状态查询错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}

		walletLogDao.updateTryTimes(acceptNo);

		if (respBody.getLists() != null && respBody.getLists().getList() != null) {

			List<PayResult> results = respBody.getLists().getList();

			// 更新银行回单到流水表
			return results.stream().map(rs -> {
				WalletLogStatus status = WalletLogStatus.parsePuDong8804(rs.getTransStatus());
				TransStatus8800 transStatus = TransStatus8800.parse(rs.getTransStatus());

				WalletLog walletLog = walletLogDao.selectByAcctAndElecNo(rs.getAcceptNo()
					, rs.getElecChequeNo(), WalletLogStatus.PROCESSING.getValue());
				if (walletLog != null) {
					walletLog.setSeqNo(rs.getSeqNo());
					walletLog.setStatus(status.getValue());
					walletLog.setErrMsg(transStatus != null ? transStatus.getDescription()
						: ("未知状态" + rs.getTransStatus()));
					walletLog.setEndTime(new Date());
					walletLogDao.updateByPrimaryKeySelective(walletLog);
				}
				return walletLog;
			}).filter(rs -> rs != null).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}
}
