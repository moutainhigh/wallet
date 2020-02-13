package com.rfchina.wallet.server.api.impl;

import com.rfchina.biztools.lock.SimpleExclusiveLock;
import com.rfchina.biztools.mq.PostMq;
import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.EnumParamValid;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.ParamValid;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.platform.common.page.Pagination;
import com.rfchina.platform.common.utils.BeanUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.platform.common.utils.RegexUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.ext.WalletCardDao;
import com.rfchina.wallet.domain.mapper.ext.WalletDao;
import com.rfchina.wallet.domain.mapper.ext.WalletUserDao;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.OrderStatus;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.domain.model.ApplyStatusChange;
import com.rfchina.wallet.domain.model.BankCode;
import com.rfchina.wallet.domain.model.Wallet;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletUser;
import com.rfchina.wallet.domain.model.ext.Bank;
import com.rfchina.wallet.domain.model.ext.BankArea;
import com.rfchina.wallet.domain.model.ext.BankClass;
import com.rfchina.wallet.domain.model.ext.WalletCardExt;
import com.rfchina.wallet.server.api.WalletApi;
import com.rfchina.wallet.server.mapper.ext.ApplyStatusChangeExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletOrderExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.WalletCardVo;
import com.rfchina.wallet.server.model.ext.WalletInfoResp;
import com.rfchina.wallet.server.service.ConfigService;
import com.rfchina.wallet.server.service.JuniorPayService;
import com.rfchina.wallet.server.service.MqService;
import com.rfchina.wallet.server.service.UserService;
import com.rfchina.wallet.server.service.WalletService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WalletApiImpl implements WalletApi {

	@Autowired
	private WalletService walletService;

	@Autowired
	private JuniorPayService juniorPayService;

	@Autowired
	private SimpleExclusiveLock lock;

	@Autowired
	private WalletUserDao walletUserDao;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private WalletCardDao walletCardDao;


	@Autowired
	private ApplyStatusChangeExtDao applyStatusChangeExtDao;

	@Autowired
	private MqService mqService;

	@Autowired
	private ExecutorService walletApiExecutor;

	@Autowired
	private WalletOrderExtDao walletOrderDao;

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public List<PayStatusResp> queryWalletApply(String accessToken,
		@ParamValid(nullable = true) String bizNo,
		@ParamValid(nullable = true) String batchNo) {
		return walletService.queryWalletApply(bizNo, batchNo);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER, EnumTokenType.APP})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfo(String accessToken,
		@ParamValid(nullable = false) Long walletId) {
		return walletService.queryWalletInfo(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER, EnumTokenType.APP})
	@SignVerify
	@Override
	public WalletInfoResp queryWalletInfoByUserId(String accessToken, Long userId) {
		return walletService.queryWalletInfoByUserId(userId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<Wallet> walletList(String accessToken, String title, Byte type,
		Byte walletLevel,
		Byte status, Integer limit, Integer offset, Boolean stat) {
		return walletService.walletList(title, type, walletLevel, status, limit, offset, stat);
	}

	@Log
	@PostMq(routingKey = MqConstant.WALLET_CREATE)
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Wallet createMchWallet(String accessToken,
		@ParamValid(nullable = false) Byte type,
		@ParamValid(nullable = false) String title,
		@ParamValid(nullable = false) Byte source,
		@ParamValid(nullable = false) String mchId,
		@ParamValid(nullable = false) String companyName,
		@ParamValid(nullable = true) String tel,
		@ParamValid(nullable = true) String email
	) {
		try {
			return walletService.createMchWallet(type, title, source, mchId,companyName,tel,email);
		} catch (Exception e) {
			log.error("创建钱包失败", e);
			throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
				"创建钱包失败");
		}
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public Pagination<WalletOrder> walletApplyList(String accessToken, Long walletId,
		Date startTime,
		Date endTime, int limit, int offset, Boolean stat) {
		return walletService.walletOrderList(walletId, startTime, endTime,
			limit, offset, stat);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER, EnumTokenType.APP})
	@SignVerify
	@Override
	public List<WalletCard> bankCardList(String accessToken, Long walletId) {
		return walletService.bankCardList(walletId);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	@PostMq(routingKey = MqConstant.WALLET_BANKCARD_BIND)
	public WalletCardExt bindBankCard(String accessToken, Long walletId, String bankCode,
		String bankAccount,
		String depositName, Integer isDef, String telephone) {
		return walletService
			.bindBankCard(walletId, bankCode, bankAccount, depositName, isDef, telephone);
	}

	@Override
	public List<BankClass> bankClassList() {
		return walletService.bankClassList();
	}

	@Override
	public List<BankArea> bankAreaList(@ParamValid(nullable = false) String classCode) {
		return walletService.bankAreaList(classCode);
	}

	@Override
	public List<Bank> bankList(@ParamValid(nullable = false) String classCode,
		@ParamValid(nullable = false) String areaCode) {
		return walletService.bankList(classCode, areaCode);
	}

	@Override
	public BankCode bank(String bankCode) {
		return walletService.bank(bankCode);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void auditWalletCompany(Long walletId, String companyName, Byte status,
		Long auditType, String phone, String email) {
		walletService.auditWalletCompany(walletId, companyName, status, auditType, phone, email);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public ResponseValue sendVerifyCode(String accessToken, Long userId,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		@EnumParamValid(valuableEnumClass = com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class)
			Integer type, @ParamValid(nullable = false) String verifyToken, String redirectUrl,
		String ip) {

		com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType enumSendSmsType = EnumUtil.parse(
			com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.class, type);

		if (enumSendSmsType
			== com.rfchina.wallet.domain.misc.EnumDef.EnumSendSmsType.VERIFY_BINDING_ACCOUNT) {
			//检查已绑定钱包的手机号
			WalletUser walletUser = Optional.ofNullable(walletUserDao.selectByMobile(mobile))
				.orElseThrow(() -> new WalletResponseException(
					WalletResponseCode.EnumWalletResponseCode.WALLET_NOT_BINDING));

			if (walletUser.getStatus()
				== com.rfchina.wallet.domain.misc.EnumDef.EnumWalletStatus.DISABLE.getValue()
				.byteValue()) {
				throw new WalletResponseException(
					WalletResponseCode.EnumWalletResponseCode.WALLET_DISABLE);
			}
		}
		//直接发送短信
		return userService
			.sendSmsVerifyCode(com.rfchina.wallet.domain.misc.EnumDef.EnumVerifyCodeType.LOGIN,
				mobile,
				verifyToken, redirectUrl, enumSendSmsType.msg(), ip);
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public WalletUser loginWithVerifyCode(String accessToken,
		@ParamValid(pattern = RegexUtil.REGEX_MOBILE) String mobile,
		@ParamValid(nullable = false, min = 1, max = 6) String verifyCode,
		@EnumParamValid(valuableEnumClass = EnumDef.EnumVerifyType.class) Integer type, String ip) {
		//通过短信验证码登录
		userService.userLoginWithVerifyCode(mobile, verifyCode, ip);

		//检查帐号是否已开通钱包
		WalletUser walletUser = walletUserDao.selectByMobile(mobile);
		if (null == walletUser) {
			throw new WalletResponseException(EnumWalletResponseCode.WALLET_ACCOUNT_NOT_EXIST);
		}

		return walletUser;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER})
	@SignVerify
	@Override
	public void setStatusFailWithApplyBill(String accessToken, String batchNo, String bizNo,
		String auditUserId, String auditUser, String auditComment) {

		WalletOrder order = walletOrderDao.selectByBatchNoAndBizNo(batchNo, bizNo);
		if (null == order) {
			throw new WalletResponseException(
				ResponseCode.EnumResponseCode.COMMON_DATA_DOES_NOT_EXIST,
				"batch_no: " + batchNo + ", biz_no: " + bizNo);
		}

		Date[] changeTime = {new Date()};
		ApplyStatusChange statusChange;
		if (order.getStatus().intValue() != OrderStatus.FAIL.getValue().intValue()) {

			if (order.getStatus().intValue() != OrderStatus.SUCC.getValue().intValue()) {
				throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"申请单状态为成功时才能进行重置为失败操作");
			}

			statusChange = applyStatusChangeExtDao.selectByApplyId(order.getId());
			if (null != statusChange) {
				throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE,
					"该申请单已进行过状态调整");
			}

			walletService.setWalletApplyStatus(order.getId(), auditUserId, auditUser, auditComment);
		} else {
			statusChange = applyStatusChangeExtDao.selectByApplyId(order.getId());
			changeTime[0] = statusChange.getCreateTime();
		}

		walletApiExecutor.execute(() -> mqService.publish(MqService.MqApplyStatusChange.builder()
			.applyId(order.getId())
			.tradeNo(order.getBatchNo())
			.orderNo(order.getBizNo())
			.newStatus(OrderStatus.FAIL.getValue().intValue())
			.oldStatus(OrderStatus.SUCC.getValue().intValue())
			.changeTime(changeTime[0])
			.msg(auditComment)
			.build(), MqConstant.WALLET_APPLY_BILL_STATUS_CHANGE));

	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = {EnumTokenType.APP_MANAGER, EnumTokenType.APP})
	@SignVerify
	@Override
	public List<WalletCardVo> queryWalletCard(String accessToken, Long walletId) {
		List<WalletCard> walletCards = walletCardDao.selectByWalletId(walletId);
		return walletCards.stream()
			.map(card -> BeanUtil.newInstance(card, WalletCardVo.class))
			.collect(Collectors.toList());
	}


}
