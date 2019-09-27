package com.rfchina.wallet.server.service;

import com.alibaba.fastjson.JSON;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.*;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.*;
import com.rfchina.wallet.domain.model.WalletApplyCriteria.Criteria;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.adapter.UserAdapter;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.predicate.ExactErrPredicate;
import com.rfchina.wallet.server.bank.pudong.domain.util.ExceptionUtil;
import com.rfchina.wallet.server.bank.yunst.request.YunstSetCompanyInfoReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstSetCompanyInfoResult;
import com.rfchina.wallet.server.mapper.ext.*;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.model.ext.WalletInfoResp.WalletInfoRespBuilder;
import com.rfchina.wallet.server.msic.EnumWallet;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.LockStatus;
import com.rfchina.wallet.server.msic.EnumWallet.NotifyType;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletStatus;
import com.rfchina.wallet.server.msic.EnumWallet.WalletType;
import com.rfchina.wallet.server.service.handler.common.HandlerHelper;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import java.util.stream.Collectors;
import javax.mail.internet.MimeMessage;

import com.rfchina.wallet.server.service.handler.yunst.YunstBaseHandler;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class WalletService {

	@Autowired
	private WalletExtDao walletDao;

	@Autowired
	private WalletCompanyExtDao walletCompanyDao;

	@Autowired
	private WalletPersonExtDao walletPersonDao;

	@Autowired
	private WalletUserExtDao walletUserDao;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private WalletCardDao walletCardDao;

	@Autowired
	private UserAdapter userAdapter;

	@Autowired
	private AppService appService;

	@Autowired
	private BankCodeDao bankCodeDao;

	@Autowired
	private HandlerHelper handlerHelper;

	@Autowired
	private WalletApplyExtDao walletApplyExtDao;

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	@Autowired
	private ConfigService configService;

	@Autowired
	private ExactErrPredicate exactErrPredicate;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Autowired
	private GatewayTransExtDao gatewayTransDao;

	@Autowired
	private YunstUserHandler yunstUserHandler;

	@Autowired
	private WalletChannelExtDao walletChannelDao;

	/**
	 * 查询出佣结果
	 */
	public List<PayStatusResp> queryWalletApply(String bizNo, String batchNo) {

		WalletApplyCriteria example = new WalletApplyCriteria();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(bizNo)) {
			criteria.andBizNoEqualTo(bizNo);
		}
		if (!StringUtils.isEmpty(batchNo)) {
			criteria.andBatchNoEqualTo(batchNo);
		}

		List<WalletApply> walletApplies = walletApplyDao.selectByExample(example);
		if (walletApplies.isEmpty()) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST, batchNo + "_" + bizNo);
		}

		// 查询支付单对应交易
		List<Long> tranIds = walletApplies.stream().map(apply -> apply.getCurrTransId()).collect(Collectors.toList());
		List<GatewayTrans> gatewayTrans = gatewayTransService.getTransIds(tranIds);
		Map<String, GatewayTrans> transMap = gatewayTrans.stream()
				.collect(Collectors.toMap(trans -> trans.getWalletApplyId().toString(), trans -> trans));

		return walletApplies.stream().map(walletApply -> {

			PayStatusResp resp = BeanUtil.newInstance(walletApply, PayStatusResp.class);
			String key = walletApply.getId().toString();
			if (transMap.containsKey(key)) {
				GatewayTrans trans = transMap.get(key);
				resp.setElecChequeNo(trans.getElecChequeNo());
				resp.setErrCode(trans.getErrCode());
				resp.setUserErrMsg(trans.getUserErrMsg());
				resp.setSysErrMsg(trans.getSysErrMsg());
				resp.setEndTime(trans.getEndTime());
				resp.setTransDate(DateUtil.formatDate(walletApply.getCreateTime()));
			}
			if (!StringUtils.isEmpty(walletApply.getPayeeBankCode())) {
				BankCode bankCode = bankCodeDao.selectByBankCode(walletApply.getPayeeBankCode());
				resp.setPayeeBankInfo(bankCode);
			}
			return resp;
		}).collect(Collectors.toList());

	}

	/**
	 * 重做问题单
	 */
	public void redo(Long walletApplyId) {
		log.info("重做问题单 [{}]", walletApplyId);
		WalletApply walletApply = walletApplyExtDao.selectByPrimaryKey(walletApplyId);
		if (walletApply == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST, walletApplyId.toString());
		}

		if (walletApply.getStatus().byteValue() != WalletApplyStatus.REDO.getValue()) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_APPLY_STATUS_ERROR);
		}

		GatewayTrans trans = gatewayTransService.createTrans(walletApply);
		walletApply.setCurrTransId(trans.getId());
		walletApply.setStatus(WalletApplyStatus.SENDING.getValue());
		walletApplyExtDao.updateByPrimaryKey(walletApply);
	}

	/**
	 * 定时支付
	 */
	public void quartzPay(Integer batchSize) {

		List<String> batchNos = walletApplyExtDao.selectUnSendBatchNo(batchSize);
		batchNos.forEach(batchNo -> {

			int c = walletApplyExtDao.updateLock(batchNo, LockStatus.UNLOCK.getValue(), LockStatus.LOCKED.getValue());
			if (c <= 0) {
				log.error("锁定记录失败, batchNo = {}", batchNo);
			} else {
				log.info("开始更新批次号 [{}]", batchNo);
				try {
					List<WalletApply> walletApplies = walletApplyExtDao.selectByBatchNo(batchNo,
							WalletApplyStatus.SENDING.getValue());
					if (StringUtils.isEmpty(batchNo) || walletApplies.isEmpty()) {
						return;
					}

					for (WalletApply walletApply : walletApplies) {

						WalletCard walletCard = walletCardDao.selectByDef(walletApply.getWalletId());
						if (walletCard == null) {
							log.warn("钱包[{}]没有绑定银行卡，跳过申请单[{}]", walletApply.getWalletId(), walletApply.getId());
							return;
						}
						fillCardInfo(walletApply, walletCard);
					}
					// 请求网关
					EBankHandler handler = null;
					try {

						handler = handlerHelper.selectByWalletLevel(null);
						Tuple<GatewayMethod, PayTuple> rs = handler.pay(walletApplies);

						// 记录结果
						GatewayMethod method = rs.left;
						PayTuple payInResp = rs.right;
						for (WalletApply walletApply : walletApplies) {
							// 更新申请单
							walletApply.setStatus(WalletApplyStatus.PROCESSING.getValue());
							walletApply.setLanchTime(new Date());
							walletApplyExtDao.updateByPrimaryKeySelective(walletApply);

							// 更新交易记录
							GatewayTrans gatewayTrans = gatewayTransService.selOrCrtTrans(walletApply);
							gatewayTrans.setAcceptNo(payInResp.getAcceptNo());
							gatewayTrans.setPacketId(payInResp.getPacketId());
							gatewayTrans.setElecChequeNo(
									payInResp.getElecMap().get(gatewayTrans.getWalletApplyId().toString()));
							gatewayTrans.setRefMethod(method.getValue());
							gatewayTrans.setLanchTime(new Date());
							gatewayTransService.updateTrans(gatewayTrans);
						}
					} catch (Exception e) {

						IGatewayError err = ExceptionUtil.explain(e);
						if (handler != null) {
							for (WalletApply walletApply : walletApplies) {
								try {
									handler.onAskErr(walletApply, err);
								} catch (Exception ex) {
									log.error("", ex);
								}
							}
						}
						//						if (err instanceof UnknownError) {
						log.error("银行网关支付错误", e);
						//						}
					}
				} catch (Exception e) {
					log.error("", e);
				} finally {
					walletApplyExtDao.updateLock(batchNo, LockStatus.LOCKED.getValue(), LockStatus.UNLOCK.getValue());
				}
			}
		});

	}

	/**
	 * 卡信息填入钱包申请
	 */
	private void fillCardInfo(WalletApply walletApply, WalletCard walletCard) {
		// 卡信息填入钱包申请
		walletApply.setPayerAccount(configService.getAcctNo());
		walletApply.setPayeeAccount(walletCard.getBankAccount());
		walletApply.setPayeeName(walletCard.getDepositName());
		walletApply.setPayeeType(walletCard.getIsPublic());
		walletApply.setPayeeBankCode(walletCard.getBankCode());
		walletApplyDao.updateByPrimaryKeySelective(walletApply);
	}

	/**
	 * 定时更新支付状态
	 */
	@PostMq(routingKey = MqConstant.WALLET_PAY_RESULT)
	public List<PayStatusResp> quartzUpdate(Integer batchSize) {

		log.info("scheduler: 开始更新支付状态[银企直连]");

		List<String> batchNos = walletApplyExtDao.selectUnFinishBatchNo(batchSize);

		List<Tuple<WalletApply, GatewayTrans>> result = new ArrayList<>();
		for (String batchNo : batchNos) {
			List<WalletApply> walletApplies = walletApplyDao.selectByBatchNo(batchNo,
					WalletApplyStatus.PROCESSING.getValue());
			// 如果没有处理中，则结束
			if (walletApplies.isEmpty()) {
				continue;
			}

			List<Tuple<WalletApply, GatewayTrans>> applyTuples = walletApplies.stream().map(apply -> {
				GatewayTrans trans = gatewayTransService.selOrCrtTrans(apply);
				return new Tuple<>(apply, trans);
			}).collect(Collectors.toList());

			// 选择处理器
			Byte level = walletApplies.stream().map(WalletApply::getWalletLevel).distinct().findFirst().orElse(null);
			EBankHandler handler = handlerHelper.selectByWalletLevel(level);
			try {
				log.info("开始更新批次号 [{}]", batchNo);
				walletApplyDao.incTryTimes(batchNo, DateUtil.addSecs(new Date(), configService.getNextRoundSec()));
				List<Tuple<WalletApply, GatewayTrans>> tuples = handler.updatePayStatus(applyTuples);
				result.addAll(tuples);
			} catch (Exception e) {
				log.error("定时更新支付状态, 异常！", e);
			}
		}

		log.info("更新批次状态，批次数量= {}，更新笔数= {}，批次号={}", batchNos.size(), result.size(), JSON.toJSONString(batchNos));
		log.info("scheduler: 结束更新支付状态[银企直连]");

		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}

		return result.stream().map(rs -> {
			WalletApply apply = rs.left;
			GatewayTrans trans = rs.right;
			PayStatusResp resp = BeanUtil.newInstance(apply, PayStatusResp.class);
			resp.setErrCode(trans.getErrCode());
			resp.setUserErrMsg(trans.getUserErrMsg());
			resp.setSysErrMsg(trans.getSysErrMsg());
			resp.setEndTime(trans.getEndTime());
			return resp;
		}).collect(Collectors.toList());
	}

	/**
	 * 通知研发
	 */
	public void notifyDeveloper(List<WalletApply> walletApplies) {

		if (walletApplies.isEmpty()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("<table border='1'>")
				.append("<tr>")
				.append("<td>")
				.append("流水")
				.append("</td>")
				.append("<td>")
				.append("业务号")
				.append("</td>")
				.append("<td>")
				.append("金额")
				.append("</td>")
				.append("<td>")
				.append("错误原因")
				.append("</td>")
				.append("<td>")
				.append("下单时间")
				.append("</td>")
				.append("<td>")
				.append("备注")
				.append("</td>")
				.append("</tr>");
		for (WalletApply walletApply : walletApplies) {
			GatewayTrans trans = gatewayTransService.selOrCrtTrans(walletApply);
			BigDecimal amount = new BigDecimal(walletApply.getAmount());
			amount = amount.divide(new BigDecimal("100"), 2, RoundingMode.DOWN);
			builder.append("<tr>")
					.append("<td>")
					.append(walletApply.getId())
					.append("</td>")
					.append("<td>")
					.append(walletApply.getBizNo())
					.append("</td>")
					.append("<td>")
					.append(amount)
					.append("</td>")
					.append("<td>")
					.append(trans.getSysErrMsg())
					.append("</td>")
					.append("<td>")
					.append(DateUtil.formatDate(walletApply.getCreateTime()))
					.append("</td>")
					.append("<td>")
					.append(JSON.toJSONString(walletApply))
					.append("</td>")
					.append("</tr>");
		}
		builder.append("</table>");

		String title = String.format("*******[技术邮件][银企直连][%s]转账失败，等待人工处理", configService.getEnv());
		String msg = builder.toString();
		sendEmail(title, msg, configService.getNotifyDevEmail());

		List<Long> ids = walletApplies.stream().map(walletApply -> walletApply.getId()).collect(Collectors.toList());
		if (!ids.isEmpty()) {
			walletApplyExtDao.updateNotified(ids, NotifyType.DEVELOPER.getValue());
		}
	}

	/**
	 * 通知业务
	 */
	public void notifyBusiness(List<WalletApply> walletApplies) {

		if (walletApplies.isEmpty()) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("注意：先确定问题单的失败原因，属于不可能成功的终态，再重新发起").append("</br>");
		builder.append("<table border='1'>")
				.append("<tr>")
				.append("<td>")
				.append("流水")
				.append("</td>")
				.append("<td>")
				.append("业务号")
				.append("</td>")
				.append("<td>")
				.append("金额")
				.append("</td>")
				.append("<td>")
				.append("错误原因")
				.append("</td>")
				.append("<td>")
				.append("下单时间")
				.append("</td>")
				.append("<td>")
				.append("备注")
				.append("</td>")
				.append("</tr>");

		for (WalletApply walletApply : walletApplies) {
			GatewayTrans trans = gatewayTransService.selOrCrtTrans(walletApply);
			BigDecimal amount = new BigDecimal(walletApply.getAmount());
			amount = amount.divide(new BigDecimal("100"), 2, RoundingMode.DOWN);
			builder.append("<tr>")
					.append("<td>")
					.append(walletApply.getId())
					.append("</td>")
					.append("<td>")
					.append(walletApply.getBizNo())
					.append("</td>")
					.append("<td>")
					.append(amount)
					.append("</td>")
					.append("<td>")
					.append(trans.getSysErrMsg())
					.append("</td>")
					.append("<td>")
					.append(DateUtil.formatDate(walletApply.getCreateTime()))
					.append("</td>")
					.append("<td>")
					.append("常见失败进入重新发起通道")
					.append("</td>")
					.append("</tr>");
		}
		builder.append("</table>");

		String title = String.format("*******[业务邮件][银企直连][%s]转账失败，处理主账户/客户问题后，手动重新发起", configService.getEnv());
		String msg = builder.toString();
		sendEmail(title, msg, configService.getNotifyBizEmail());

		List<Long> ids = walletApplies.stream().map(walletApply -> walletApply.getId()).collect(Collectors.toList());
		if (!ids.isEmpty()) {
			walletApplyExtDao.updateNotified(ids, NotifyType.BUSINESS.getValue());
		}

	}

	/**
	 * 发送邮件
	 */
	public void sendEmail(String title, String msg, String emails) {
		if (StringUtil.isBlank(emails)) {
			return;
		}
		String[] receives = emails.split(",");
		if (receives == null || receives.length == 0) {
			return;
		}
		log.info("发送邮件给 [{}] , 内容 {}", emails, msg);
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(configService.getEmailSender());
			helper.setTo(receives);
			helper.setSubject(title);
			helper.setText(msg, true);
			javaMailSender.send(message);
		} catch (Exception e) {
			log.error("邮件发送失败", e);
		}
	}

	/**
	 * 查询钱包明细
	 */
	public WalletInfoResp queryWalletInfo(Long walletId) {
		WalletInfoRespBuilder builder = WalletInfoResp.builder();

		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST, String.valueOf(walletId));
		}

		if (WalletType.COMPANY.getValue().byteValue() == wallet.getType()) {
			WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);
			builder.companyInfo(walletCompany);
		} else if (WalletType.PERSON.getValue().byteValue() == wallet.getType()) {
			WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
			builder.personInfo(walletPerson);
		}

		WalletCard walletCard = walletCardDao.selectByDef(walletId);

		return builder.wallet(wallet).defWalletCard(walletCard).bankCardCount(walletCardDao.count(walletId)).build();
	}

	public WalletInfoResp queryWalletInfoByUserId(Long userId) {
		WalletUser walletUser = walletUserDao.selectByPrimaryKey(userId);

		if (walletUser == null) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST, "user_id");
		}

		return queryWalletInfo(walletUser.getWalletId());
	}

	/**
	 * 开通未审核的高级钱包
	 */
	public Wallet createWallet(Byte type, String title, Byte source) {

		Wallet wallet = Wallet.builder()
				.type(type)
				.title(title)
				.walletBalance(0L)
				.rechargeAmount(0L)
				.rechargeCount(0)
				.payAmount(0L)
				.payCount(0)
				.source(source)
				.status(WalletStatus.WAIT_AUDIT.getValue())
				.createTime(new Date())
				.level(EnumDef.EnumWalletLevel.JUNIOR.getValue())
				.build();
		walletDao.insertSelective(wallet);

		return wallet;
	}

	/**
	 * 查詢钱包流水
	 *
	 * @param walletId  钱包ID
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 */
	public Pagination<WalletApply> walletApplyList(@ParamValid(nullable = false) Long walletId, Date startTime,
			Date endTime, @ParamValid(min = 1, max = SymbolConstant.QUERY_LIMIT) int limit,
			@ParamValid(min = 0) long offset, Boolean stat) {
		Date queryStartTime = null;

		if (null != startTime) {
			queryStartTime = DateUtil.getDate2(startTime);
		}

		Date queryEndTime = null;

		if (null != startTime) {
			queryEndTime = DateUtil.getDate(endTime);
		}

		List<WalletApply> data = walletApplyDao.selectList(walletId, queryStartTime, queryEndTime, null, limit,
				offset);
		long total = Optional.ofNullable(stat).orElse(false) ? walletApplyDao.selectCount(walletId, queryStartTime,
				queryEndTime, null) : 0L;

		return new Pagination.PaginationBuilder<WalletApply>().offset(offset)
				.pageLimit(limit)
				.data(data)
				.total(total)
				.build();
	}

	/**
	 * 查询绑定的银行卡列表
	 *
	 * @param walletId 钱包ID
	 */
	public List<WalletCard> bankCardList(@ParamValid(nullable = false) Long walletId) {
		return walletCardDao.selectByWalletId(walletId);
	}

	/**
	 * 绑定对工银行卡
	 *
	 * @param walletId    钱包id
	 * @param bankCode    银行代码
	 * @param bankAccount 银行帐号
	 * @param depositName 开户名
	 * @param isDef       是否默认银行卡: 1:是，2：否
	 * @param telephone   预留手机号
	 */
	public WalletCardExt bindBankCard(@ParamValid(nullable = false) Long walletId,
			@ParamValid(nullable = false, min = 12, max = 12) String bankCode,
			@ParamValid(nullable = false, min = 20, max = 32) String bankAccount,
			@ParamValid(nullable = false, min = 1, max = 256) String depositName,
			@EnumParamValid(valuableEnumClass = EnumDef.EnumDefBankCard.class) Integer isDef,
			@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String telephone) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (null == wallet) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
		}

		BankCode bankCodeResult = bankCodeDao.selectByBankCode(bankCode);
		if (null == bankCodeResult) {
			throw new RfchinaResponseException(EnumResponseCode.COMMON_INVALID_PARAMS, "bank_code");
		}

		//更新已绑定的银行卡状态为已解绑
		int effectRows = walletCardDao.updateWalletCard(walletId, EnumDef.EnumCardBindStatus.UNBIND.getValue(),
				EnumDef.EnumCardBindStatus.BIND.getValue(), null, EnumDef.EnumDefBankCard.NO.getValue());

		//首次绑定银行卡
		int firstBind =
				(0 == effectRows) ? EnumDef.FirstBindBankCard.YES.getValue() : EnumDef.FirstBindBankCard.NO.getValue();

		Date now = new Date();

		WalletCard walletCard = WalletCard.builder()
				.walletId(walletId)
				.bankAccount(bankAccount)
				.bankCode(bankCode)
				.bankName(bankCodeResult.getClassName())
				.depositName(depositName)
				.depositBank(bankCodeResult.getBankName())
				.isDef(isDef.byteValue())
				.isPublic(EnumDef.EnumPublicAccount.YES.getValue().byteValue())
				.telephone(telephone)
				.lastUpdTime(now)
				.build();

		effectRows = walletCardDao.insertSelective(walletCard);
		if (effectRows < 1) {
			log.error("绑定银行卡失败, wallet: {}, effectRows: {}", JsonUtil.toJSON(wallet), effectRows);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}

		WalletCardExt walletCardExt = new WalletCardExt(walletCard);
		walletCardExt.setFirstBind(firstBind);
		return walletCardExt;
	}

	/**
	 * 查询银行类别列表
	 */
	public List<BankClass> bankClassList() {
		return bankCodeDao.selectBankClassList();
	}

	/**
	 * 查询银行地区列表
	 *
	 * @param classCode 类别编码
	 */
	public List<BankArea> bankAreaList(String classCode) {
		return bankCodeDao.selectBankAreaList(classCode);
	}

	/**
	 * 查询银行支行列表
	 *
	 * @param classCode 类别编码
	 * @param areaCode  地区编码
	 */
	public List<Bank> bankList(String classCode, String areaCode) {
		return bankCodeDao.selectBankList(classCode, areaCode);
	}

	public BankCode bank(String bankCode) {
		BankCodeCriteria bankCodeCriteria = new BankCodeCriteria();
		bankCodeCriteria.createCriteria().andBankCodeEqualTo(bankCode);
		List<BankCode> bankCodeList = bankCodeDao.selectByExample(bankCodeCriteria);
		if (bankCodeList.isEmpty()) {
			return null;
		}
		return bankCodeList.get(0);
	}

	/**
	 * 富慧通审核通过个人商家钱包
	 */
	public void activeWalletPerson(Long walletId, String name, Byte idType, String idNo, Long auditType) {

		WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);

		boolean isUpdate = walletPerson != null;
		walletPerson = isUpdate ? walletPerson : new WalletPerson();
		walletPerson.setWalletId(walletId);
		walletPerson.setName(name);
		walletPerson.setIdType(idType);
		walletPerson.setIdNo(idNo);

		if (isUpdate) {
			walletPersonDao.updateByPrimaryKeySelective(walletPerson);
		} else {
			walletPersonDao.insertSelective(walletPerson);
		}

		walletDao.updateActiveStatus(walletId, auditType);
	}

	/**
	 * 富慧通审核通过企业商家钱包
	 */
	public void activeWalletCompany(Long walletId, String companyName, Long auditType) {

		WalletCompany walletCompany = walletCompanyDao.selectByWalletId(walletId);

		boolean isUpdate = walletCompany != null;
		walletCompany = isUpdate ? walletCompany : new WalletCompany();
		walletCompany.setWalletId(walletId);
		walletCompany.setCompanyName(companyName);

		if (isUpdate) {
			walletCompanyDao.updateByPrimaryKeySelective(walletCompany);
		} else {
			walletCompanyDao.insertSelective(walletCompany);
		}

		walletDao.updateActiveStatus(walletId, auditType);
	}

	/**
	 * 升级高级钱包
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public WalletChannel createSeniorWallet(Integer channelType, Long walletId, Byte source) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("开通高级钱包失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		WalletChannel.WalletChannelBuilder builder = WalletChannel.builder()
				.channelType(channelType.byteValue())
				.status(EnumDef.WalletChannelAuditStatus.NOT_COMMIT.getValue().byteValue())
				.walletId(walletId)
				.createTime(new Date());
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			Tuple<YunstCreateMemberResult, YunstBaseHandler.YunstMemberType> member = null;
			try {
				member = yunstUserHandler.createMember(walletId, source);
			} catch (Exception e) {
				log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType, walletId, source);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
			}
			builder.bizUserId(member.left.getBizUserId())
					.channelUserId(member.left.getUserId())
					.memberType(member.right.getValue().byteValue());
		}
		WalletChannel walletChannel = builder.build();
		int effectRows = walletChannelDao.insertSelective(walletChannel);
		if (effectRows != 1) {
			log.error("开通高级钱包失败, channelType: {}, walletId: {}, source:{}", channelType, walletId, source);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
		effectRows = walletDao.updateByPrimaryKeySelective(wallet);
		if (effectRows != 1) {
			log.error("更新钱包等级失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return walletChannel;
	}

	/**
	 * 更新钱包等级
	 */
	public Wallet upgradeWalletLevel(Long walletId) {
		Wallet wallet = walletDao.selectByPrimaryKey(walletId);
		if (wallet == null) {
			log.error("更新钱包等级失败, 查无此钱包, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		wallet.setLevel(EnumDef.EnumWalletLevel.SENIOR.getValue());
		int effctRows = walletDao.updateByPrimaryKeySelective(wallet);
		if (effctRows != 1) {
			log.error("更新钱包等级失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return wallet;
	}

	/**
	 * 高级钱包绑定手机
	 */
	public boolean seniorWalletBindPhone(Integer channelType, Long walletId, Byte source, String mobile,
			String verifyCode) {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
			WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
					transformBizUserId);
			if (walletChannel == null) {
				log.error("未创建云商通用户: bizUserId:{}", transformBizUserId);
				return false;
			}
			try {
				if (StringUtils.isEmpty(walletChannel.getSecurityTel())) {
					return true;
				}
				if (!yunstUserHandler.bindPhone(walletId, source, mobile, verifyCode)) {
					log.error("高级钱包绑定手机失败, channelType: {}, walletId: {},source: {}", channelType, walletId, source);
					return false;
				}
				walletChannel.setSecurityTel(mobile);
				int effectRows = walletChannelDao.updateByPrimaryKeySelective(walletChannel);
				if (effectRows != 1) {
					log.error("更新高级钱包手机信息失败:effectRows:{},walletChannel: {}", effectRows,
							JsonUtil.toJSON(walletChannel));
					return false;
				}
			} catch (Exception e) {
				log.error("更新高级钱包手机信息失败 msg:{}", e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 高级钱包个人修改绑定手机
	 */
	public String seniorWalletPersonChangeBindPhone(Integer channelType, Long walletId, Byte source, String realName,
			String idNo, String mobile) {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
			WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
					transformBizUserId);
			if (walletChannel == null) {
				log.error("未创建云商通用户: bizUserId:{}", transformBizUserId);
				throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
			}
			try {
				if (!mobile.equals(walletChannel.getSecurityTel())) {
					log.error("旧手机与已绑定高级钱包手机不符 oldPhone:{},current binded phone:{}", mobile,
							walletChannel.getSecurityTel());
					throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
				}
				return yunstUserHandler.modifyPhone(walletId, source, realName, mobile,
						EnumDef.EnumIdType.ID_CARD.getValue().longValue(), RSAUtil.encrypt(idNo));
			} catch (Exception e) {
				log.error("更新高级钱包手机信息失败 msg:{}", e);
			}
		}
		return "";
	}

	/**
	 * 高级钱包个人认证
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String seniorWalletPersonAuth(Integer channelType, Long walletId, Byte source, String realName, String idNo,
			String mobile) {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
			WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
					transformBizUserId);
			if (walletChannel == null) {
				log.error("未创建云商通用户: bizUserId:{}", transformBizUserId);
				throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
			}
			try {
				if (StringUtils.isEmpty(walletChannel.getSecurityTel())) {
					log.error("高级钱包未绑定手机: walletId:{}", walletId);
					throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
				}
				boolean result = yunstUserHandler.personCertification(walletId, source, realName,
						EnumDef.EnumIdType.ID_CARD.getValue().longValue(), RSAUtil.encrypt(idNo));
				if (!result) {
					log.error("个人实名认证失败, channelType: {}, walletId: {},source: {}", channelType, walletId, source);
					throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
				}
				walletChannel.setStatus(EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
				walletChannel.setCheckTime(new Date());
				int effectRows = walletChannelDao.updateByPrimaryKeySelective(walletChannel);
				if (effectRows != 1) {
					log.error("更新高级钱包审核状态信息失败:effectRows:{},walletChannel: {}", effectRows,
							JsonUtil.toJSON(walletChannel));
					throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
				}
				WalletPerson walletPerson = walletPersonDao.selectByWalletId(walletId);
				if (walletPerson == null) {
					Date curDate = new Date();
					effectRows = walletPersonDao.insertSelective(WalletPerson.builder()
							.walletId(walletId)
							.idType(EnumDef.EnumIdType.ID_CARD.getValue().byteValue())
							.idNo(idNo)
							.name(realName)
							.realLevel(EnumDef.EnumUserRealType.ID_CARD.getValue().byteValue())
							.tel(mobile)
							.createTime(curDate)
							.lastUpdTime(curDate)
							.build());
					if (effectRows != 1) {
						log.error("更新个人钱包个人信息表失败:channelType: {}, walletId:{}", channelType, walletId);
						throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
					}
				}

				return this.signMemberProtocol(source, walletId);
			} catch (Exception e) {
				log.error("高级钱包个人认证失败 msg:{}", e);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
			}
		}
		return "";
	}

	/**
	 * 高级钱包企业资料审核
	 */
	public Integer seniorWalletCompanyAudit(Integer channelType, Long walletId, Byte source, Integer auditType,
			YunstSetCompanyInfoReq.CompanyBasicInfo companyBasicInfo) {
		if (channelType == EnumDef.ChannelType.YUNST.getValue().intValue()) {
			String transformBizUserId = YunstBaseHandler.transferToYunstBizUserFormat(walletId, source);
			WalletChannel walletChannel = walletChannelDao.selectByChannelTypeAndBizUserId(channelType,
					transformBizUserId);
			if (walletChannel == null) {
				log.error("未创建云商通用户: bizUserId:{}", transformBizUserId);
				throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
			}
			try {
				boolean isAuth = auditType == EnumDef.WalletChannelAuditType.AUTO.getValue().intValue();
				YunstSetCompanyInfoResult yunstSetCompanyInfoResult = yunstUserHandler.setCompanyInfo(walletId, source,
						isAuth, companyBasicInfo);
				if (Objects.nonNull(yunstSetCompanyInfoResult) && yunstSetCompanyInfoResult.getBizUserId()
						.equals(transformBizUserId)) {
					Long result = yunstSetCompanyInfoResult.getResult();
					String failReason = yunstSetCompanyInfoResult.getFailReason();
					String remark = yunstSetCompanyInfoResult.getRemark();
					if (Objects.nonNull(result)) {
						if (2L == result.longValue()) {
							walletChannel.setStatus(
									EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
							walletChannel.setFailReason(null);
						} else if (3L == result.longValue()) {
							walletChannel.setStatus(EnumDef.WalletChannelAuditStatus.AUDIT_FAIL.getValue().byteValue());
							walletChannel.setFailReason(failReason);
						}
						walletChannel.setCheckTime(new Date());
					} else {
						walletChannel.setStatus(EnumDef.WalletChannelAuditStatus.WAITING_AUDIT.getValue().byteValue());
					}
					walletChannel.setRemark(remark);
					int effectRows = walletChannelDao.updateByPrimaryKeySelective(walletChannel);
					if (effectRows != 1) {
						log.error("更新高级钱包企业信息审核状态失败:channelType: {}, walletId:{}", channelType, walletId);
						throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
					}
					return walletChannel.getStatus().intValue();
				} else {
					log.error("高级钱包企业信息审核失败:channelType: {}, walletId:{}", channelType, walletId);
					throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
				}
			} catch (Exception e) {
				log.error("高级钱包企业信息审核失败 msg:{}", e);
				throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
			}
		}
		return EnumDef.WalletChannelAuditStatus.WAITING_AUDIT.getValue();
	}

	/**
	 * 高级钱包绑定申请绑定手机
	 */
	public String seniorWalletApplyBindPhone(Integer channelType, Long walletId, Byte source, String telephone) {
		try {
			if (channelType.intValue() == EnumDef.ChannelType.YUNST.getValue().intValue()) {
				return yunstUserHandler.sendVerificationCode(walletId, source, telephone, 9);
			}
		} catch (Exception e) {
			log.error("高级钱包绑定申请绑定手机发送验证码失败, 查无此钱包, telephone: {}", telephone);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
		return "";
	}

	/**
	 * 高级钱包扣款协议地址
	 */
	public String signBalanceProtocol(Byte source, Long walletId) {
		try {
			return yunstUserHandler.generateBalanceProtocolUrl(walletId, source);
		} catch (Exception e) {
			log.error("高级钱包生成扣款协议连接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
	}

	/**
	 * 高级钱包会员协议地址
	 */
	public String signMemberProtocol(Byte source, Long walletId) {
		try {
			return yunstUserHandler.generateSignContractUrl(walletId, source);
		} catch (Exception e) {
			log.error("高级钱包生成会员协议连接失败, walletId: {}", walletId);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE);
		}
	}
}
