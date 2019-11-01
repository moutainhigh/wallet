package com.rfchina.wallet.server.service.handler.yunst;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSetPayPwd;
import com.rfchina.wallet.domain.misc.EnumDef.WalletChannelSignContract;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyRefType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyType;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import com.rfchina.wallet.server.bank.yunst.response.RecallResp;
import com.rfchina.wallet.server.bank.yunst.response.RpsResp;
import com.rfchina.wallet.server.bank.yunst.response.YunstNotify;
import com.rfchina.wallet.server.mapper.ext.WalletChannelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletVerifyHisExtDao;
import com.rfchina.wallet.server.model.ext.SLWalletMqMessage;
import java.util.Date;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YunstNotifyHandler {

	public static final String YUNST_SIGN_SUCCESS = "success";
	@Autowired
	private WalletChannelExtDao walletChannelExtDao;
	@Autowired
	private WalletVerifyHisExtDao walletVerifyHisExtDao;
	@Autowired
	private YunstBizHandler yunstBizHandler;

	public SLWalletMqMessage handleVerfiyResult(ChannelNotify channelNotify,
		YunstNotify.CompanyAuditResult rtnVal) {
		log.info("处理企业信息审核结果通知 rtnVal:{}", rtnVal);

		String bizUserId = rtnVal.getBizUserId();
		String checkTime = rtnVal.getCheckTime();
		long result = rtnVal.getResult();
		String failReason = rtnVal.getFailReason();
		String remark = rtnVal.getRemark();

		channelNotify.setBizUserId(bizUserId);

		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		if (result == 2L) {
			walletChannel.setStatus(
				EnumDef.WalletChannelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
			walletChannel.setFailReason(null);
			walletVerifyHisExtDao.insertSelective(
				WalletVerifyHis.builder().walletId(walletChannel.getWalletId())
					.refId(walletChannel.getId()).type(
					WalletVerifyRefType.COMPANY.getValue().byteValue()).verifyType(
					WalletVerifyChannel.TONGLIAN.getValue().byteValue()).verifyType(
					WalletVerifyType.COMPANY_VERIFY.getValue().byteValue())
					.verifyTime(DateUtil.parse(checkTime, DateUtil.STANDARD_DTAETIME_PATTERN))
					.createTime(new Date()).build());
		} else if (result == 3L) {
			walletChannel
				.setStatus(EnumDef.WalletChannelAuditStatus.AUDIT_FAIL.getValue().byteValue());
			walletChannel.setFailReason(failReason);
		}
		walletChannel.setRemark(remark);
		walletChannel
			.setCheckTime(DateUtil.parse(checkTime, DateUtil.STANDARD_DTAETIME_PATTERN));

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理企业信息审核结果通知-更新审核状态状态失败:bizUserId:{}", bizUserId);
		}

		return SLWalletMqMessage.builder().walletId(walletChannel.getWalletId())
			.isPass(result == 2L).checkTime(checkTime).failReason(failReason).build();
	}

	public void handleSignContractResult(ChannelNotify channelNotify,
		YunstNotify.SignContractResult rtnVal) {
		log.info("处理会员电子签约通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setIsSignContact(WalletChannelSignContract.MEMBER.getValue().byteValue());

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("更新电子签约状态失败:bizUserId:{}", bizUserId);
		}
	}

	public void handleSignBalanceContractResult(ChannelNotify channelNotify,
		YunstNotify.BalanceContractResult rtnVal) {
		log.info("处理扣款协议签约通知 rtnVal:{}", rtnVal);
		String reqSn = rtnVal.getProtocolReqSn();
		String signStatus = rtnVal.getSignStatus();

		WalletChannel walletChannel = walletChannelExtDao.selectByBanlceProtocolReqSn(reqSn);
		if (Objects.isNull(walletChannel)) {
			return;
		}
		String bizUserId = walletChannel.getBizUserId();
		channelNotify.setBizUserId(bizUserId);

		if (YUNST_SIGN_SUCCESS.equalsIgnoreCase(signStatus)) {
			walletChannel
				.setIsSignContact(WalletChannelSignContract.BALANCE.getValue().byteValue());
			int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
			if (effectRows != 1) {
				log.error("更新扣款协议状态失败:bizUserId:{}", bizUserId);
			}
		}
	}


	public void handleChangeBindPhoneResult(ChannelNotify channelNotify,
		YunstNotify.UpdatePhoneResult rtnVal) {
		log.info("处理个人会员修改绑定手机通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		String newPhone = rtnVal.getNewPhone();
		channelNotify.setBizUserId(bizUserId);
		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setSecurityTel(newPhone);

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人会员修改绑定手机失败:bizUserId:{},newPhone:{}", bizUserId, newPhone);
		}
	}

	public void handleSetPayPwdResult(ChannelNotify channelNotify,
		YunstNotify.SetPayPwd rtnVal) {
		log.info("处理个人设置支付密码通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletChannel walletChannel = walletChannelExtDao.selectByChannelTypeAndBizUserId(
			EnumDef.ChannelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setHasPayPassword(WalletChannelSetPayPwd.YES.getValue().byteValue());

		int effectRows = walletChannelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人设置支付密码失败:bizUserId:{}", bizUserId);
		}
	}

}
