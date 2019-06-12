package com.rfchina.wallet.server.service.handler;

import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQuery48Builder;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQuery49Builder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.UserRedoPredicate;
import com.rfchina.wallet.server.bank.pudong.domain.request.PubPayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery48Resp;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery48RespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49Resp;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49RespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.EBankQuery49RespVo;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayQueryRespBody.PayResult;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.util.ExceptionUtil;
import com.rfchina.wallet.server.bank.pudong.domain.util.StringObject;
import com.rfchina.wallet.server.mapper.ext.BankCodeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.model.ext.HostSeqNo;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.LancherType;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus8804;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusDO48;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusDO49;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.ConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private BankCodeExtDao bankCodeExtDao;

	@Autowired
	private OkHttpClient client;

	@Autowired
	@Qualifier("exactErrPredicate")
	private ExactErrPredicate exactErrPredicate;

	@Autowired
	@Qualifier("userRedoPredicate")
	private UserRedoPredicate userRedoPredicate;

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
	public Tuple<GatewayMethod, PayInResp> pay(List<WalletApply> payInReqs) throws Exception {

		if (payInReqs == null || payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		List<PubPayReq> payReqs = payInReqs.stream().map(walletApply -> {

			WalletCard walletCard = getWalletCard(walletApply.getWalletId());
			if (walletCard == null) {
				throw new WalletResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST
					, String.valueOf(walletApply.getWalletId()));
			}
			if (walletApply.getAmount() <= 0) {
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_AMOUNT_ERROR);
			}

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(walletApply.getAmount())
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);

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
				.elecChequeNo(walletApply.getElecChequeNo())
				.acctNo(configService.getAcctNo())
				.acctName(configService.getAcctName())
				.payeeAcctNo(walletCard.getBankAccount())
				.payeeName(walletCard.getDepositName())
				.amount(bigAmount.toString())
				.sysFlag(sysFlag)
				.remitLocation(remitLocation)
				.note(walletApply.getNote())
				.payeeType(walletCard.getIsPublic().equals("1") ? "0" : "1")
				.payeeBankSelectFlag(isOtherRemit ? "1" : null)
				.payeeBankNo(isOtherRemit ? walletCard.getBankCode() : null)
				.payPurpose(
					walletApply.getPayPurpose() != null ? walletApply.getPayPurpose() : null)
				.build();

		}).collect(Collectors.toList());

		String packetId = IdGenerator
			.createBizId(IdGenerator.PREFIX_WALLET, IdGenerator.DEFAULT_LENGTH, (orderId) -> true);
		PubPayReqBuilder req = PubPayReqBuilder.builder()
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
	public List<WalletApply> updatePayStatus(String acceptNo, Date createTime) {

		walletApplyDao.incTryTimes(acceptNo, DateUtil.addSecs(new Date(),
			configService.getNextRoundSec()));

		HostSeqNo hostSeqNo = walletApplyDao.selectHostAcctNo(acceptNo);
		hostSeqNo = (hostSeqNo == null) ? new HostSeqNo() : hostSeqNo;
		hostSeqNo.setHostAcceptNo(null);
		// 数据库没有网银受理号
		if (StringUtils.isBlank(hostSeqNo.getHostAcceptNo())) {
			// 查询包授权
			Tuple<String, Date> tuple = doEBankPackageQuery(acceptNo, createTime);
			// 网银未授权的
			if (tuple == null) {
				return new ArrayList<>();
			}
			hostSeqNo.setHostAcceptNo(tuple.left);
			hostSeqNo.setAuditTime(tuple.right);
			// 查询明细授权
			doEBankDetailQuery(acceptNo, hostSeqNo);
		}

		// 查核心支付结果
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

				WalletApply walletApply = walletApplyDao.selectByHostAcctAndElecNo(rs.getAcceptNo()
					, rs.getElecChequeNo(), WalletApplyStatus.PROCESSING.getValue());
				if (walletApply != null) {
					IGatewayError err = extractErrCode(rs.getNote());
					// 如果是终态
					TransStatus8804 transStatus = TransStatus8804.parse(rs.getTransStatus());
					if (transStatus.isEndStatus()) {
						WalletApplyStatus status;
						// 核心拒绝的，需要进一步判断错误码
						if (TransStatus8804.REJECT.getValue().equals(transStatus.getValue())) {
							if (exactErrPredicate.test(err)) {
								boolean userRedo = userRedoPredicate.test(err);
								status = userRedo ? WalletApplyStatus.REDO : WalletApplyStatus.FAIL;
								walletApply.setLancher(userRedo ? LancherType.USER.getValue() : 0);
							} else {
								status = WalletApplyStatus.WAIT_DEAL;
							}
						} else {
							status = WalletApplyStatus.parsePuDong8804(rs.getTransStatus());
						}
						walletApply.setStatus(status.getValue());
						walletApply.setEndTime(new Date());
					}

					walletApply.setSeqNo(rs.getSeqNo());
					walletApply.setStage(req.getTransCode());
					walletApply.setErrStatus(rs.getTransStatus());
					walletApply.setErrCode(err.getErrCode());
					walletApply.setSysErrMsg(err.getErrMsg());
					walletApply.setUserErrMsg(transStatus != null ? transStatus.getDescription()
						: ("未知状态" + rs.getTransStatus()));
					walletApplyDao.updateByPrimaryKey(walletApply);
				}
				return walletApply;
			}).filter(rs -> rs != null).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}


	/**
	 * 识别错误码
	 */
	public IGatewayError extractErrCode(String note) {

		String errCode = ExceptionUtil.extractErrCode(note);
		String errMsg = StringUtils.isNotBlank(note) && note.contains("|") ?
			note.split("|")[1] : null;
		return GatewayError.builder()
			.errCode(errCode)
			.errMsg(errMsg)
			.build();
	}

	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	@Override
	public WalletApply onAskErr(WalletApply walletApply, IGatewayError err) {
		// 确切失败的单业务会重新发起新的转账，其他的单进入待处理状态
		boolean exactErr = exactErrPredicate.test(err);
		boolean userRedo = userRedoPredicate.test(err);

		Byte status = !exactErr ? WalletApplyStatus.WAIT_DEAL.getValue() :
			(userRedo ? WalletApplyStatus.REDO.getValue() : WalletApplyStatus.FAIL.getValue());
		walletApply.setStatus(status);
		walletApply.setStage(err.getTransCode());
		walletApply.setErrCode(err.getErrCode());
		walletApply.setSysErrMsg(err.getErrMsg());
		walletApply.setUserErrMsg("发起交易异常");
		if (exactErr) {
			walletApply.setLancher(userRedo ? LancherType.USER.getValue()
				: LancherType.SYS.getValue());
		}
		walletApplyDao.updateByPrimaryKeySelective(walletApply);
		return walletApply;
	}

	/**
	 * 查询网银包受理状态
	 */
	private Tuple<String, Date> doEBankPackageQuery(String acceptNo, Date createTime) {

		// 查询网银受理状态
		EBankQuery48Builder req = EBankQuery48Builder.builder()
			.masterId(configService.getMasterId())
			.authMasterID(configService.getAuditMasterId())
			.beginDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.endDate(DateUtil.formatDate(createTime, "yyyyMMdd"))
			.acceptNo(acceptNo)
			.build();
		EBankQuery48RespBody resp;
		try {
			resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(), client);
		} catch (Exception e) {
			log.error("银企直连-网银授权状态查询错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}
		// 如果网银查不到则放弃进一步查询
		if (resp.getLists() == null || resp.getLists().getList() == null) {
			return null;
		}
		List<EBankQuery48Resp> list = resp.getLists().getList();
		EBankQuery48Resp audit48Result = list.stream()
			.filter(result -> acceptNo.equals(result.getEntJnlSeqNo()))
			.findFirst().get();
		if (audit48Result == null) {
			return null;
		}
		// 网银状态非交易成功
		Date auditTime = StringUtils.isNotBlank(audit48Result.getTransDate()) ?
			DateUtil.parse(audit48Result.getTransDate(), "yyyyMMdd") : null;
		if (!TransStatusDO48.SUCC.getValue().equals(audit48Result.getTransStatus())) {
			TransStatusDO48 status = EnumUtil
				.parse(TransStatusDO48.class, audit48Result.getTransStatus());
			// 更新中间状态和错误码
			walletApplyDao.updateAcceptNoErrMsg(acceptNo, req.getTransCode()
				, "EBANK:" + audit48Result.getTransStatus()
				, "EBANK:" + audit48Result.getFailCode()
				, status != null ? status.getValueName() : "文档未记录状态");
			// 如果是未成功的终态，关闭这批交易
			if (status != null && status.isEndStatus()) {
				walletApplyDao.updateAcceptNoStatus(acceptNo, WalletApplyStatus.FAIL.getValue(),
					auditTime, new Date());
			}

			return null;
		}
		// 网银成功之后
		walletApplyDao.updateHostAcctNo(acceptNo, audit48Result.getHostJnlSeqNo(), auditTime);

		return new Tuple<>(audit48Result.getHostJnlSeqNo(), auditTime);
	}

	/**
	 * 查询网银明细授权
	 */
	private void doEBankDetailQuery(String acceptNo, HostSeqNo hostSeqNo) {
		// 网银授权明细
		EBankQuery49Builder req = EBankQuery49Builder.builder()
			.masterId(configService.getMasterId())
			.authMasterID(configService.getAuditMasterId())
			.entJnlSeqNo(acceptNo)
			.build();
		EBankQuery49RespBody resp;
		try {
			resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(), client);
		} catch (Exception e) {
			log.error("银企直连-网银授权状态查询错误", e);
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}
		// 如果网银查不到则放弃进一步查询
		if (resp.getLists() == null || resp.getLists().getList() == null) {
			return;
		}
		for (EBankQuery49Resp resp49 : resp.getLists().getList()) {

			EBankQuery49RespVo respVo = StringObject.parseStringObject(resp49.getDetailedContent()
				, EBankQuery49RespVo.class, "\\|");
			// 关闭授权失败的单
			if (TransStatusDO49.REFUSE.getValue().equals(respVo.getStatus())) {
				WalletApply walletApply = walletApplyDao.selectByAcctAndElecNo(acceptNo
					, respVo.getElecChequeNo(), WalletApplyStatus.PROCESSING.getValue());
				walletApply.setStage(req.getTransCode());
				walletApply.setStatus(WalletApplyStatus.FAIL.getValue());
				walletApply.setErrStatus(respVo.getStatus());
				walletApply.setUserErrMsg(TransStatusDO49.REFUSE.getValueName());
				walletApply.setAuditTime(hostSeqNo.getAuditTime());
				walletApply.setEndTime(new Date());
				walletApplyDao.updateByPrimaryKeySelective(walletApply);
			}
		}
	}

	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByDef(walletId);
	}
}
