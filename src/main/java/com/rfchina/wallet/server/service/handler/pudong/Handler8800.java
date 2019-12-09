package com.rfchina.wallet.server.service.handler.pudong;

import com.alibaba.fastjson.JSON;
import com.rfchina.biztools.generate.IdGenerator;
import com.rfchina.platform.biztool.mapper.string.StringObject;
import com.rfchina.platform.common.misc.Triple;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletFinance;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQuery48Builder;
import com.rfchina.wallet.server.bank.pudong.builder.EBankQuery49Builder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayQueryBuilder;
import com.rfchina.wallet.server.bank.pudong.builder.PubPayReqBuilder;
import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
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
import com.rfchina.wallet.server.bank.yunst.exception.UnknownException;
import com.rfchina.wallet.server.mapper.ext.BankCodeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletFinanceExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.CardPro;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.GwPayeeType;
import com.rfchina.wallet.server.msic.EnumWallet.GwProgress;
import com.rfchina.wallet.server.msic.EnumWallet.OrderStatus;
import com.rfchina.wallet.server.msic.EnumWallet.OrderSubStatus;
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
public class Handler8800 extends EBankHandler {

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletCardDao walletCardDao;

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
	private CacheService cacheService;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private WalletFinanceExtDao walletFinanceDao;

	@Autowired
	private WalletOrderExtDao walletOrderDao;


	@Override
	public boolean isSupportTunnelType(Byte tunnelType) {
		return TunnelType.PUDONG.getValue().byteValue() == tunnelType.byteValue();
	}

	@Override
	public GatewayMethod getGatewayMethod() {
		return GatewayMethod.PUDONG_8800;
	}

	@Override
	public Tuple<GatewayMethod, PayTuple> finance(List<Tuple<WalletOrder, WalletFinance>> tuples)
		throws Exception {

		try {

			if (tuples == null || tuples.isEmpty() || tuples.size() > 20) {
				throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_BATCH_LIMIT);
			}

			Map<String, String> elecMap = new HashMap<>();
			List<PubPayReq> payReqs = tuples.stream().map(tuple -> {

				WalletOrder walletOrder = tuple.left;
				WalletFinance walletFinance = tuple.right;
				// 必须注意，分转换为0.00元
				BigDecimal bigAmount = new BigDecimal(walletOrder.getAmount())
					.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_DOWN);

				BankCode bankCode = bankCodeExtDao.selectByCode(walletFinance.getPayeeBankCode());
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
				elecMap.put(walletOrder.getId().toString(), elecNo);
				return PubPayReq.builder()
					.elecChequeNo(elecNo)
					.acctNo(configService.getAcctNo())
					.acctName(configService.getAcctName())
					.payeeAcctNo(walletFinance.getPayeeAccount())
					.payeeName(walletFinance.getPayeeName())
					.amount(bigAmount.toString())
					.sysFlag(sysFlag)
					.remitLocation(remitLocation)
					.note(walletOrder.getNote())
					.payeeType(walletFinance.getCardPro().byteValue() == CardPro.COMPANY.getValue()
						? GwPayeeType.COMPANY.getValue().toString()
						: GwPayeeType.PERSON.getValue().toString())
					.payeeBankName(payeeBankName)
					.payeeBankSelectFlag(isOtherRemit ? "1" : null)
					.payeeBankNo(isOtherRemit ? walletFinance.getPayeeBankCode() : null)
					.payPurpose(
						walletFinance.getPayPurpose() != null ? walletFinance.getPayPurpose()
							: null)
					.build();

			}).collect(Collectors.toList());

			String packetId = genPkgId();
			PubPayReqBuilder req = PubPayReqBuilder.builder()
				.masterId(configService.getMasterId())
				.packetId(packetId)
				.authMasterId(configService.getAuditMasterId())
				.packageNo(tuples.get(0).left.getBatchNo())
				.payList(payReqs)
				.build();

			PubPayRespBody resp = req.lanch(configService.getHostUrl(), configService.getSignUrl(),
				client);
			PayTuple payTuple = new PayTuple();
			payTuple.setAcceptNo(resp.getAcceptNo());
			payTuple.setPacketId(packetId);
			payTuple.setElecMap(elecMap);
			payTuple.setStage(req.getTransCode());

			return new Tuple<>(getGatewayMethod(), payTuple);
		} catch (Exception e) {
			List<WalletOrder> orders = tuples.stream().map(t -> t.left)
				.collect(Collectors.toList());
			dealUndefinedError(orders, e);
			return null;
		}
	}

	public List<Triple<WalletOrder, WalletFinance, GatewayTrans>> updateOrderStatus(
		List<WalletOrder> walletOrders) {
		try {
			List<Triple<WalletOrder, WalletFinance, GatewayTrans>> triples = walletOrders.stream()
				.map(walletOrder -> {
					WalletFinance walletFinance = walletFinanceDao
						.selectByOrderId(walletOrder.getId());
					GatewayTrans gatewayTrans = gatewayTransService
						.selOrCrtTrans(walletOrder, walletFinance);
					return new Triple<>(walletOrder, walletFinance, gatewayTrans);
				}).collect(Collectors.toList());

			Triple<WalletOrder, WalletFinance, GatewayTrans> firstTriple = triples.get(0);

			GatewayTrans firstTrans = firstTriple.z;

			// 如果包未授权过，则查询授权
			if (StringUtils.isBlank(firstTrans.getHostAcceptNo())) {
				// 查询包授权
				boolean audited = doEBankPackageQuery(triples);
				// 网银未授权的
				if (!audited) {
					return new ArrayList<>();
				}

				// 查询明细授权
				doEBankDetailQuery(triples);
			}

			// 查核心支付结果
			PubPayQueryBuilder req = PubPayQueryBuilder.builder()
				.masterId(configService.getMasterId())
				.packetId(genPkgId())
				.acctNo(configService.getAcctNo())
				.beginDate(DateUtil.formatDate(firstTrans.getCreateTime(), "yyyyMMdd"))
				.endDate(DateUtil.formatDate(firstTrans.getAuditTime(), "yyyyMMdd"))
				.acceptNo(firstTrans.getHostAcceptNo())
				.build();

			PubPayQueryRespBody respBody;
			try {
				respBody = req
					.lanch(configService.getHostUrl(), configService.getSignUrl(), client);
			} catch (Exception e) {
				log.error("银企直连-网关支付状态查询错误", e);
				IGatewayError err = ExceptionUtil.explain(e);
				triples.forEach(triple -> {
					WalletOrder walletOrder = triple.x;
					WalletFinance walletFinance = triple.y;
					if (OrderStatus.WAITTING.getValue() == walletOrder.getStatus()) {
						GatewayTrans gatewayTrans = gatewayTransService
							.selOrCrtTrans(walletOrder, walletFinance);
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

					Optional<Triple<WalletOrder, WalletFinance, GatewayTrans>> opt = triples
						.stream()
						.filter(triple -> {
							WalletOrder order = triple.x;
							GatewayTrans trans = triple.z;
							return rs.getElecChequeNo().equals(trans.getElecChequeNo())
								&& order.getStatus().byteValue() == OrderStatus.WAITTING.getValue();
						}).findFirst();

					if (opt.isPresent()) {
						Triple<WalletOrder, WalletFinance, GatewayTrans> tuple = opt.get();
						WalletOrder walletOrder = tuple.x;
						GatewayTrans trans = tuple.z;

						IGatewayError err = extractErrCode(rs.getNote());

						// 如果是终态, 更新到申请单
						TransStatus8804 transStatus = TransStatus8804.parse(rs.getTransStatus());
						String userErrMsg = transStatus != null ? transStatus.getDescription()
							: ("未知状态" + rs.getTransStatus());
						String sysErrMsg = StringUtils.isNotBlank(err.getErrMsg()) ? err.getErrMsg()
							: userErrMsg;
						if (transStatus.isEndStatus()) {
							OrderStatus status;
							// 核心拒绝的，需要进一步判断错误码
							if (TransStatus8804.REJECT.getValue().equals(transStatus.getValue())) {
								status = exactErrPredicate.test(err) ? OrderStatus.FAIL
									: OrderStatus.WAITTING;
							} else {
								status = OrderStatus.parsePuDong8804(rs.getTransStatus());
							}
							walletOrder.setStatus(status.getValue());
							walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
							walletOrder.setEndTime(new Date());
							walletOrder.setTunnelErrMsg(sysErrMsg);
							walletOrder.setTunnelStatus(rs.getTransStatus());
							walletOrder.setTunnelErrCode(err.getErrCode());
							if (StringUtil.isNotBlank(rs.getTransDate())) {
								Date bizTime = DateUtil
									.parse(rs.getTransDate(), DateUtil.SHORT_DTAE_PATTERN);
								trans.setBizTime(bizTime);
								if (TransStatus8804.FINISH.getValue()
									.equals(transStatus.getValue())) {
									walletOrder.setTunnelSuccTime(bizTime);
								}
							}
							walletOrderDao.updateByPrimaryKey(walletOrder);
							trans.setEndTime(new Date());
						}
						// 记录中间状态
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
		} catch (Exception e) {
			dealUndefinedError(walletOrders, e);
			return null;
		}
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


	/**
	 * 查询网银包受理状态
	 */
	private boolean doEBankPackageQuery(
		List<Triple<WalletOrder, WalletFinance, GatewayTrans>> triples) {

		Triple<WalletOrder, WalletFinance, GatewayTrans> triple = triples.get(0);
		GatewayTrans firstTrans = triple.z;

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
			triples.forEach(t -> {
				GatewayTrans trans = t.z;
				trans.setStage(req.getTransCode());
				trans.setErrStatus(audit48Result.getTransStatus());
				trans.setErrCode(audit48Result.getFailCode());
				String errMsg = status != null ? status.getValueName() : "文档未记录状态";
				trans.setUserErrMsg(errMsg);
				trans.setSysErrMsg(errMsg);

				// 如果是未成功的终态，关闭这批交易
				if (status != null && status.isEndStatus()) {

					WalletOrder walletOrder = t.x;
					walletOrder.setStatus(OrderStatus.FAIL.getValue());
					walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
					walletOrder.setTunnelErrMsg(errMsg);
					walletOrder.setTunnelErrCode(audit48Result.getFailCode());
					walletOrder.setTunnelStatus(audit48Result.getTransStatus());
					walletOrder.setEndTime(new Date());
					walletOrderDao.updateByPrimaryKeySelective(walletOrder);

					trans.setEndTime(new Date());
					trans.setAuditTime(auditTime);
				}
				gatewayTransService.updateTrans(trans);
			});
			return false;
		}

		// 网银成功之后，更新网银受理编号和授权时间
		triples.forEach(t -> {
			GatewayTrans trans = t.z;
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
	private void doEBankDetailQuery(
		List<Triple<WalletOrder, WalletFinance, GatewayTrans>> triples) {

		Triple<WalletOrder, WalletFinance, GatewayTrans> firstTuple = triples.get(0);
		GatewayTrans firstTrans = firstTuple.z;

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

				Optional<Triple<WalletOrder, WalletFinance, GatewayTrans>> opt = triples.stream()
					.filter(applyTuple -> {
						GatewayTrans trans = applyTuple.z;
						return respVo.getElecChequeNo().equals(trans.getElecChequeNo());
					}).findFirst();
				if (opt.isPresent()) {
					Triple<WalletOrder, WalletFinance, GatewayTrans> triple = opt.get();

					WalletOrder walletOrder = triple.x;
					walletOrder.setStatus(WalletApplyStatus.FAIL.getValue());
					walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
					walletOrder.setTunnelStatus(respVo.getStatus());
					walletOrder.setTunnelErrMsg(TransStatusDO49.REFUSE.getValueName());
					walletOrder.setEndTime(new Date());
					walletOrderDao.updateByPrimaryKeySelective(walletOrder);

					GatewayTrans trans = triple.z;
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

	private void dealUndefinedError(List<WalletOrder> orders, Exception e) {
		log.error("未定义异常", e);
		for (WalletOrder walletOrder : orders) {
			walletOrder.setUserErrMsg("交易异常");
			walletOrder.setProgress(GwProgress.HAS_RESP.getValue());
			walletOrder.setSubStatus(OrderSubStatus.WAIT_DEAL.getValue());
			walletOrderDao.updateByPrimaryKeySelective(walletOrder);
		}
		throw new UnknownException(EnumWalletResponseCode.UNDEFINED_ERROR);
	}
}
