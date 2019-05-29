package com.rfchina.wallet.server.service.handler;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQueryResp;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody.PayResult;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.mapper.ext.BankCodeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletLogExtDao;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus8804;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusDO48;
import com.rfchina.wallet.server.msic.EnumWallet.WalletLogStatus;
import com.rfchina.wallet.server.service.ConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 8800处理器
 *
 * @author nzm
 */
@Component
@Slf4j
@Data
public class Handler8800 implements EBankHandler {

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private WalletLogExtDao walletLogDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private BankCodeExtDao bankCodeExtDao;

	@Autowired
	private OkHttpClient client;

	private EBankHandler next;


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

		if (payInReqs == null || payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		List<PubPayReq> payReqs = payInReqs.stream().map(walletLog -> {

			WalletCard walletCard = getWalletCard(walletLog.getWalletId());
			if (walletCard == null) {
				throw new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(walletLog.getWalletId()));
			}
			if (walletLog.getAmount() <= 0) {
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_AMOUNT_ERROR);
			}

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(walletLog.getAmount())
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_DOWN);

			BankCode bankCode = bankCodeExtDao.selectByCode(walletCard.getBankCode());
			if (bankCode == null) {
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
			}

			String sysFlag = bankCode.getClassCode().equals(configService.getAcctBankCode()) ?
				SysFlag.SELF.getValue() : SysFlag.OTHER.getValue();

			String remitLocation = bankCode.getAreaCode().equals(configService.getAcctAreaCode()) ?
				RemitLocation.SELF.getValue() : RemitLocation.OTHER.getValue();

			boolean isOtherRemit = SysFlag.OTHER.getValue().equals(sysFlag)
				&& RemitLocation.OTHER.getValue().equals(remitLocation);

			return PubPayReq.builder()
				.elecChequeNo(walletLog.getElecChequeNo())
				.acctNo(configService.getAcctNo())
				.acctName(configService.getAcctName())
				.payeeAcctNo(walletCard.getBankAccount())
				.payeeName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.sysFlag(sysFlag)
				.remitLocation(remitLocation)
				.note(walletLog.getNote())
				.payeeType(walletCard.getIsPublic().equals("1") ? "0" : "1")
				.payeeBankSelectFlag(isOtherRemit ? "1" : null)
				.payeeBankNo(isOtherRemit ? walletCard.getBankCode() : null)
				.payPurpose(walletLog.getPayPurpose() != null ? walletLog.getPayPurpose() : null)
				.build();

		}).collect(Collectors.toList());

		String packetId = IdGenerator
			.createBizId(IdGenerator.PREFIX_WALLET, IdGenerator.DEFAULT_LENGTH, (orderId) -> true);
		PubPayReqBuilder req = PubPayReqBuilder
			.builder()
			.masterId(configService.getMasterId())
			.packetId(packetId)
			.authMasterId(configService.getAuditMasterId())
			.packageNo(payInReqs.get(0).getBatchNo())
			.payList(payReqs)
			.build();

		PubPayRespBody resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(),
			client);
		PayInResp payInResp = BeanUtil.newInstance(resp, PayInResp.class);

		return new Tuple<>(getGatewayMethod(), payInResp);
	}

	@Override
	public List<WalletLog> updatePayStatus(String acceptNo, Date createTime) {

		walletLogDao.incTryTimes(acceptNo, DateUtil.addSecs(new Date(),
			configService.getNextRoundSec()));

		HostSeqNo hostSeqNo = queryHostSeqNo(acceptNo, createTime);
		if (hostSeqNo == null) {
			return new ArrayList<>();
		}

		PubPayQueryBuilder req = PubPayQueryBuilder.builder()
			.masterId(configService.getMasterId())
			.acctNo(configService.getAcctNo())
			.beginDate(DateUtil.formatDate(hostSeqNo.getAuditTime(), "yyyyMMdd"))
			.endDate(DateUtil.formatDate(hostSeqNo.getAuditTime(), "yyyyMMdd"))
			.acceptNo(hostSeqNo.getHostAcceptNo())
			.build();

		PubPayQueryRespBody respBody;
		try {
			respBody = req.lanch(configService.getHostUrl(), configService.getSignUrl(), client);
		} catch (Exception e) {
			log.error("银企直连-网关支付状态查询错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}

		if (respBody.getLists() != null && respBody.getLists().getList() != null) {

			List<PayResult> results = respBody.getLists().getList();

			// 更新银行回单到流水表
			return results.stream().map(rs -> {
				TransStatus8804 transStatus = TransStatus8804.parse(rs.getTransStatus());
				WalletLogStatus status = WalletLogStatus.parsePuDong8804(rs.getTransStatus());
				Tuple<String, String> tuple = extractErrCode(rs.getNote());

				WalletLog walletLog = walletLogDao.selectByHostAcctAndElecNo(rs.getAcceptNo()
					, rs.getElecChequeNo(), WalletLogStatus.PROCESSING.getValue());
				if (walletLog != null) {
					walletLog.setSeqNo(rs.getSeqNo());
					walletLog.setStatus(status.getValue());
					walletLog.setErrStatus("CORE:" + rs.getTransStatus());
					walletLog.setErrCode(tuple.left);
					walletLog.setSysErrMsg(tuple.right);
					walletLog.setUserErrMsg(transStatus != null ? transStatus.getDescription()
						: ("未知状态" + rs.getTransStatus()));
					walletLog.setEndTime(new Date());
					walletLogDao.updateByPrimaryKeySelective(walletLog);
				}
				return walletLog;
			}).filter(rs -> rs != null).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private Tuple<String, String> extractErrCode(String note) {

		String errCode = null;
		String errMsg = null;

		if(StringUtils.isNotBlank(note)) {
			Pattern pattern = Pattern.compile("");
			Matcher matcher = pattern.matcher(note);
			if (matcher.matches()) {
				errCode = matcher.group(1);
			}
			errMsg = note.contains("|") ? note.split("|")[1] : note;
		}

		return new Tuple<>(errCode,errMsg);
	}

	@Override
	public void onGatewayErr(WalletLog walletLog, IGatewayError err) {
		// 确切失败的单业务会重新发起新的转账，其他的单进入待处理状态
		Byte status = err.isUserErr() ? WalletLogStatus.FAIL.getValue()
			: WalletLogStatus.WAIT_DEAL.getValue();
		walletLog.setStatus(status);
		walletLog.setErrCode(err.getErrCode());
		walletLog.setSysErrMsg(err.getErrMsg());
		walletLogDao.updateByPrimaryKeySelective(walletLog);
	}

	/**
	 * 查询网银受理号
	 */
	private HostSeqNo queryHostSeqNo(String acceptNo, Date createTime) {
		HostSeqNo hostSeqNo = walletLogDao.selectHostAcctNo(acceptNo);
		if (hostSeqNo != null) {
			return hostSeqNo;
		}

		// 查询网银受理状态
		EBankQueryBuilder eBankReq = EBankQueryBuilder.builder()
			.masterId(configService.getMasterId())
			.authMasterID(configService.getAuditMasterId())
			.beginDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.endDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.acceptNo(acceptNo)
			.build();

		EBankQueryRespBody eBankResp;
		try {
			eBankResp = eBankReq.lanch(configService.getHostUrl(), configService.getSignUrl(),
				client);
		} catch (Exception e) {
			log.error("银企直连-网银授权状态查询错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}

		// 如果网银查不到则放弃进一步查询
		if (eBankResp.getLists() == null || eBankResp.getLists().getList() == null) {
			return null;
		}
		List<EBankQueryResp> list = eBankResp.getLists().getList();
		EBankQueryResp auditResult = list.stream()
			.filter(resp -> acceptNo.equals(resp.getEntJnlSeqNo()))
			.findFirst().get();
		if (auditResult == null) {
			return null;
		}
		// 网银状态非交易成功
		Date auditTime = DateUtil.parse(auditResult.getTransDate(), "yyyyMMdd");
		if (!TransStatusDO48.SUCC.getValue().equals(auditResult.getTransStatus())) {
			TransStatusDO48 status = EnumUtil
				.parse(TransStatusDO48.class, auditResult.getTransStatus());

			walletLogDao.updateAcceptNoErrMsg(acceptNo, "EBANK:" + auditResult.getTransStatus()
				, "EBANK:" + auditResult.getFailCode(),
				status != null ? status.getValueName() : "文档未记录状态");
			if (status != null && status.isEndStatus()) {
				walletLogDao.updateAcceptNoStatus(acceptNo, WalletLogStatus.FAIL.getValue(),
					auditTime, new Date());
			}

			return null;
		}

		// 网银成功之后
		walletLogDao.updateHostAcctNo(acceptNo, auditResult.getHostJnlSeqNo(), auditTime);

		return HostSeqNo.builder()
			.hostAcceptNo(auditResult.getHostJnlSeqNo())
			.auditTime(auditTime)
			.build();
	}

	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}
}
