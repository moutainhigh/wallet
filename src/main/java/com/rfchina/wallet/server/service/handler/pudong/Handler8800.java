package com.rfchina.wallet.server.service.handler.pudong;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
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
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.LancherType;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatus8804;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusDO48;
import com.rfchina.wallet.server.msic.EnumWallet.TransStatusDO49;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.service.CacheService;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
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

	@Autowired
	private CacheService cacheService;

	@Autowired
	private GatewayTransService gatewayTransService;


	@Override
	public boolean isSupportWalletLevel(Byte walletType) {
		return EnumWalletLevel.JUNIOR.getValue().byteValue() == walletType;
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
	public Tuple<GatewayMethod, PayTuple> pay(List<WalletApply> payInReqs) throws Exception {

		if (payInReqs == null || payInReqs.isEmpty() || payInReqs.size() > 20) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
		}

		Map<String, String> elecMap = new HashMap<>();
		List<PubPayReq> payReqs = payInReqs.stream().map(walletApply -> {

			// 必须注意，分转换为0.00元
			BigDecimal bigAmount = new BigDecimal(walletApply.getAmount())
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);

			BankCode bankCode = bankCodeExtDao.selectByCode(walletApply.getPayeeBankCode());
			if (bankCode == null) {
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
			}

			String sysFlag = bankCode.getClassCode().equals(configService.getAcctBankCode()) ?
				SysFlag.SELF.getValue() : SysFlag.OTHER.getValue();

//			项目vivi需求，所有同城单改成异地，同城不支持自动处理
//			String remitLocation = bankCode.getAreaCode().equals(configService.getAcctAreaCode()) ?
//				RemitLocation.SELF.getValue() : RemitLocation.OTHER.getValue();
			String remitLocation = RemitLocation.OTHER.getValue();

			boolean isOtherRemit = SysFlag.OTHER.getValue().equals(sysFlag)
				&& RemitLocation.OTHER.getValue().equals(remitLocation);

			String payeeBankName =
				SysFlag.OTHER.getValue().equals(sysFlag) ? bankCode.getBankName() : null;

			String elecNo = IdGenerator.createBizId("", 16, id -> {
				return !gatewayTransService.existElecNo(id);
			});
			elecMap.put(walletApply.getId().toString(), elecNo);
			return PubPayReq.builder()
				.elecChequeNo(elecNo)
				.acctNo(configService.getAcctNo())
				.acctName(configService.getAcctName())
				.payeeAcctNo(walletApply.getPayeeAccount())
				.payeeName(walletApply.getPayeeName())
				.amount(bigAmount.toString())
				.sysFlag(sysFlag)
				.remitLocation(remitLocation)
				.note(walletApply.getNote())
				.payeeType(walletApply.getPayeeType().equals("1") ? "0" : "1")
				.payeeBankName(payeeBankName)
				.payeeBankSelectFlag(isOtherRemit ? "1" : null)
				.payeeBankNo(isOtherRemit ? walletApply.getPayeeBankCode() : null)
				.payPurpose(
					walletApply.getPayPurpose() != null ? walletApply.getPayPurpose() : null)
				.build();

		}).collect(Collectors.toList());

		String packetId = genPkgId();
		PubPayReqBuilder req = PubPayReqBuilder.builder()
			.masterId(configService.getMasterId())
			.packetId(packetId)
			.authMasterId(configService.getAuditMasterId())
			.packageNo(payInReqs.get(0).getBatchNo())
			.payList(payReqs)
			.build();

		PubPayRespBody resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(),
			client);
		PayTuple payTuple = new PayTuple();
		payTuple.setAcceptNo(resp.getAcceptNo());
		payTuple.setPacketId(packetId);
		payTuple.setElecMap(elecMap);

		return new Tuple<>(getGatewayMethod(), payTuple);
	}

	@Override
	public List<Tuple<WalletApply, GatewayTrans>> updatePayStatus(
		List<Tuple<WalletApply, GatewayTrans>> applyTuples) {

		Tuple<WalletApply, GatewayTrans> firstTuple = applyTuples.get(0);
		GatewayTrans firstTrans = firstTuple.right;

		// 如果包未授权过，则查询授权
		if (StringUtils.isBlank(firstTrans.getHostAcceptNo())) {
			// 查询包授权
			boolean audited = doEBankPackageQuery(applyTuples);
			// 网银未授权的
			if (!audited) {
				return new ArrayList<>();
			}

			// 查询明细授权
			doEBankDetailQuery(applyTuples);
		}

		// 查核心支付结果
		PubPayQueryBuilder req = PubPayQueryBuilder.builder()
			.masterId(configService.getMasterId())
			.packetId(genPkgId())
			.acctNo(configService.getAcctNo())
			.beginDate(DateUtil.formatDate(firstTrans.getAuditTime(), "yyyyMMdd"))
			.endDate(DateUtil.formatDate(firstTrans.getAuditTime(), "yyyyMMdd"))
			.acceptNo(firstTrans.getHostAcceptNo())
			.build();

		PubPayQueryRespBody respBody;
		try {
			respBody = req.lanch(configService.getHostUrl(), configService.getSignUrl(), client);
		} catch (Exception e) {
			log.error("银企直连-网关支付状态查询错误", e);
			IGatewayError err = ExceptionUtil.explain(e);
			applyTuples.forEach(tuple -> {
				WalletApply walletApply = tuple.left;
				if (WalletApplyStatus.PROCESSING.getValue() == walletApply.getStatus()) {
					GatewayTrans gatewayTrans = gatewayTransService.selOrCrtTrans(walletApply);
					gatewayTrans.setStage(err.getTransCode());
					gatewayTrans.setErrCode(err.getErrCode());
					gatewayTrans.setSysErrMsg(err.getErrMsg());
					gatewayTrans.setUserErrMsg("查询交易异常");
					gatewayTransService.updateTrans(gatewayTrans);
				}
			});

			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_STATUS_QUERY_ERROR);
		}

		if (respBody.getLists() != null && respBody.getLists().getList() != null) {

			List<PayResult> results = respBody.getLists().getList();
			// 更新银行回单到流水表
			return results.stream().map(rs -> {

				Optional<Tuple<WalletApply, GatewayTrans>> opt = applyTuples.stream()
					.filter(tuple -> {
						WalletApply apply = tuple.left;
						GatewayTrans trans = tuple.right;
						return rs.getElecChequeNo().equals(trans.getElecChequeNo())
							&& apply.getStatus().byteValue() == WalletApplyStatus.PROCESSING
							.getValue();
					}).findFirst();

				if (opt.isPresent()) {
					Tuple<WalletApply, GatewayTrans> tuple = opt.get();
					WalletApply walletApply = tuple.left;
					GatewayTrans trans = tuple.right;

					IGatewayError err = extractErrCode(rs.getNote());
					// 如果是终态, 更新到申请单
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
						if (StringUtil.isNotBlank(rs.getTransDate())) {
							Date bizTime = DateUtil
								.parse(rs.getTransDate(), DateUtil.SHORT_DTAE_PATTERN);
							walletApply.setBizTime(bizTime);
							trans.setBizTime(bizTime);
						}
						walletApplyDao.updateByPrimaryKey(walletApply);

						trans.setEndTime(new Date());
					}
					// 记录中间状态
					String userErrMsg = transStatus != null ? transStatus.getDescription()
						: ("未知状态" + rs.getTransStatus());
					String sysErrMsg = StringUtils.isNotBlank(err.getErrMsg()) ? err.getErrMsg()
						: userErrMsg;
					trans.setSeqNo(rs.getSeqNo());
					trans.setStage(req.getTransCode());
					trans.setErrStatus(rs.getTransStatus());
					trans.setErrCode(err.getErrCode());
					trans.setSysErrMsg(sysErrMsg);
					trans.setUserErrMsg(userErrMsg);
					gatewayTransService.updateTrans(trans);
					return tuple;
				}
				return null;
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
			note.split("\\|")[1] : null;
		errMsg = StringUtils.isNotBlank(errMsg) ? errMsg.replaceAll("&lt;|&gt;", "") : null;
		return GatewayError.builder()
			.errCode(errCode)
			.errMsg(errMsg)
			.build();
	}

	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	@Override
	public PayStatusResp onAskErr(WalletApply walletApply, IGatewayError err) {
		// 确切失败的单业务会重新发起新的转账，其他的单进入待处理状态
		boolean exactErr = exactErrPredicate.test(err);
		boolean userRedo = userRedoPredicate.test(err);
		Byte status = !exactErr ? WalletApplyStatus.WAIT_DEAL.getValue() :
			(userRedo ? WalletApplyStatus.REDO.getValue() : WalletApplyStatus.FAIL.getValue());
		walletApply.setStatus(status);
		if (exactErr) {
			walletApply.setLancher(userRedo ? LancherType.USER.getValue() : null);
		}
		walletApplyDao.updateByPrimaryKeySelective(walletApply);

		GatewayTrans gatewayTrans = gatewayTransService.selOrCrtTrans(walletApply);
		gatewayTrans.setStage(err.getTransCode());
		gatewayTrans.setErrCode(err.getErrCode());
		gatewayTrans.setSysErrMsg(err.getErrMsg());
		gatewayTrans.setUserErrMsg("发起交易异常");
		gatewayTransService.updateTrans(gatewayTrans);

		PayStatusResp resp = BeanUtil.newInstance(walletApply, PayStatusResp.class);
		resp.setErrCode(gatewayTrans.getErrCode());
		resp.setUserErrMsg(gatewayTrans.getUserErrMsg());
		resp.setSysErrMsg(gatewayTrans.getSysErrMsg());
		resp.setEndTime(gatewayTrans.getEndTime());
		return resp;
	}

	/**
	 * 查询网银包受理状态
	 */
	private boolean doEBankPackageQuery(
		List<Tuple<WalletApply, GatewayTrans>> applyTuples) {

		Tuple<WalletApply, GatewayTrans> tuple = applyTuples.get(0);
		GatewayTrans firstTrans = tuple.right;

		if (StringUtils.isBlank(firstTrans.getAcceptNo())) {
			log.error("交易的受理号为空 {}", JSON.toJSONString(firstTrans));
			return false;
		}

		// 查询网银受理状态
		EBankQuery48Builder req = EBankQuery48Builder.builder()
			.masterId(configService.getMasterId())
			.packetId(genPkgId())
			.authMasterID(configService.getAuditMasterId())
			.beginDate(DateUtil.formatDate(firstTrans.getLanchTime(), DateUtil.SHORT_DTAE_PATTERN))
			.endDate(DateUtil.formatDate(firstTrans.getLanchTime(), DateUtil.SHORT_DTAE_PATTERN))
			.acceptNo(firstTrans.getAcceptNo())
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
			log.error("网银查询不到该笔交易 req = {} , resp = {}", JSON.toJSONString(req)
				, JSON.toJSONString(resp));
			return false;
		}
		List<EBankQuery48Resp> list = resp.getLists().getList();
		EBankQuery48Resp audit48Result = list.stream()
			.filter(result -> result.getEntJnlSeqNo().equals(firstTrans.getAcceptNo()))
			.findFirst().orElse(null);
		if (audit48Result == null) {
			log.error("网银查询不到该笔交易 req = {} , resp = {}", JSON.toJSONString(req)
				, JSON.toJSONString(resp));
			return false;
		}
		// 网银状态非交易成功
		Date auditTime = new Date();
		if (!TransStatusDO48.SUCC.getValue().equals(audit48Result.getTransStatus())) {
			TransStatusDO48 status = EnumUtil
				.parse(TransStatusDO48.class, audit48Result.getTransStatus());
			// 未成功的时候，记录中间状态和错误码
			applyTuples.forEach(applyTuple -> {
				GatewayTrans trans = applyTuple.right;
				trans.setStage(req.getTransCode());
				trans.setErrStatus(audit48Result.getTransStatus());
				trans.setErrCode(audit48Result.getFailCode());
				String errMsg = status != null ? status.getValueName() : "文档未记录状态";
				trans.setUserErrMsg(errMsg);
				trans.setSysErrMsg(errMsg);
				gatewayTransService.updateTrans(trans);
			});

			// 如果是未成功的终态，关闭这批交易
			if (status != null && status.isEndStatus()) {

				applyTuples.forEach(applyTuple -> {
					WalletApply apply = applyTuple.left;
					apply.setStatus(WalletApplyStatus.FAIL.getValue());
					walletApplyDao.updateByPrimaryKeySelective(apply);

					GatewayTrans trans = applyTuple.right;
					trans.setEndTime(new Date());
					trans.setAuditTime(auditTime);
					gatewayTransService.updateTrans(trans);
				});
			}

			return false;

		}

		// 网银成功之后，更新网银受理编号和授权时间
		applyTuples.forEach(applyTuple -> {
			GatewayTrans trans = applyTuple.right;
			trans.setStage(req.getTransCode());
			trans.setHostAcceptNo(audit48Result.getHostJnlSeqNo());
			trans.setAuditTime(auditTime);
			gatewayTransService.updateTrans(trans);
		});

		return true;
	}

	/**
	 * 查询网银明细授权
	 */
	private void doEBankDetailQuery(List<Tuple<WalletApply, GatewayTrans>> applyTuples) {
		Tuple<WalletApply, GatewayTrans> firstTuple = applyTuples.get(0);
		GatewayTrans firstTrans = firstTuple.right;

		// 网银授权明细
		EBankQuery49Builder req = EBankQuery49Builder.builder()
			.masterId(configService.getMasterId())
			.packetId(genPkgId())
			.authMasterID(configService.getAuditMasterId())
			.entJnlSeqNo(firstTrans.getAcceptNo())
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

				Optional<Tuple<WalletApply, GatewayTrans>> opt = applyTuples.stream()
					.filter(applyTuple -> {
						GatewayTrans trans = applyTuple.right;
						return respVo.getElecChequeNo().equals(trans.getElecChequeNo());
					}).findFirst();
				if (opt.isPresent()) {
					Tuple<WalletApply, GatewayTrans> tuple = opt.get();

					WalletApply apply = tuple.left;
					apply.setStatus(WalletApplyStatus.FAIL.getValue());
					walletApplyDao.updateByPrimaryKeySelective(apply);

					GatewayTrans trans = tuple.right;
					trans.setStage(req.getTransCode());
					trans.setErrStatus(respVo.getStatus());
					trans.setUserErrMsg(TransStatusDO49.REFUSE.getValueName());
					trans.setSysErrMsg(TransStatusDO49.REFUSE.getValueName());
					trans.setEndTime(new Date());
					gatewayTransService.updateTrans(trans);
				}
			}
		}
	}

	private WalletCard getWalletCard(Long walletId) {
		return walletCardDao.selectByWalletId(walletId,1,0).get(0);
	}


	/**
	 * 获取报文流水号
	 */
	public String genPkgId() {
		Long pkgSegment = configService.getPkgSegment();
		Long currPkgId = cacheService.getCurrPkgId();
		Long idx = pkgSegment + currPkgId;

		StringBuilder builder = new StringBuilder("SLP");
		builder.append(idx.toString());
		return builder.toString();
	}
}
