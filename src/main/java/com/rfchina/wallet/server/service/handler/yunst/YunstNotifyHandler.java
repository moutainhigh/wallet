package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.biztools.mq.PostMq;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.EnumDef.TunnelType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelSetPayPwd;
import com.rfchina.wallet.domain.misc.EnumDef.WalletTunnelSignContract;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyChannel;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyRefType;
import com.rfchina.wallet.domain.misc.EnumDef.WalletVerifyType;
import com.rfchina.wallet.domain.misc.MqConstant;
import com.rfchina.wallet.domain.model.ChannelNotify;
import com.rfchina.wallet.domain.model.WalletCard;
import com.rfchina.wallet.domain.model.WalletTunnel;
import com.rfchina.wallet.domain.model.WalletVerifyHis;
import com.rfchina.wallet.server.bank.yunst.response.YunstNotify;
import com.rfchina.wallet.server.mapper.ext.WalletCardExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletTunnelExtDao;
import com.rfchina.wallet.server.mapper.ext.WalletVerifyHisExtDao;
import com.rfchina.wallet.server.model.ext.SLWalletMqMessage;
import com.rfchina.wallet.server.msic.EnumWallet.YunstCompanyInfoAuditStatus;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YunstNotifyHandler {

	public static final String YUNST_SIGN_SUCCESS = "success";
	@Autowired
	private WalletTunnelExtDao walletTunnelExtDao;
	@Autowired
	private WalletVerifyHisExtDao walletVerifyHisExtDao;
	@Autowired
	private WalletCardExtDao walletCardDao;
	@Autowired
	private YunstBizHandler yunstBizHandler;

	@PostMq(routingKey = MqConstant.WALLET_SENIOR_COMPANY_AUDIT)
	public SLWalletMqMessage handleVerfiyResult(ChannelNotify channelNotify,
		YunstNotify.CompanyAuditResult rtnVal) {
		log.info("处理企业信息审核结果通知 rtnVal:{}", rtnVal);

		String bizUserId = rtnVal.getBizUserId();
		String checkTime = rtnVal.getCheckTime();
		long result = rtnVal.getResult();
		String failReason = rtnVal.getFailReason();
		String remark = rtnVal.getRemark();

		channelNotify.setBizUserId(bizUserId);

		WalletTunnel walletTunnel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);

		walletTunnel.setRemark(remark);
		walletTunnel
			.setCheckTime(DateUtil.parse(checkTime, DateUtil.STANDARD_DTAETIME_PATTERN));

		WalletCard walletCard = walletCardDao
			.selectNonVerifyPubAccountByWalletId(walletTunnel.getWalletId());

		boolean isPass = false;

		if (result == YunstCompanyInfoAuditStatus.SUCCESS.getValue().longValue()) {
			isPass = true;
			walletTunnel.setStatus(
				EnumDef.WalletTunnelAuditStatus.AUDIT_SUCCESS.getValue().byteValue());
			walletTunnel.setFailReason(null);
			WalletVerifyHis walletVerifyHis = walletVerifyHisExtDao
				.selectByWalletIdAndRefIdAndType(walletTunnel.getWalletId(), walletTunnel.getId(),
					WalletVerifyRefType.COMPANY.getValue().byteValue());
			if (null == walletVerifyHis
				|| walletVerifyHis.getVerifyTime().compareTo(walletTunnel.getCheckTime()) == -1) {
				walletVerifyHisExtDao.insertSelective(
					WalletVerifyHis.builder().walletId(walletTunnel.getWalletId())
						.refId(walletTunnel.getId()).type(
						WalletVerifyRefType.COMPANY.getValue().byteValue())
						.verifyChannel(
							WalletVerifyChannel.TONGLIAN.getValue().byteValue())
						.verifyType(
							WalletVerifyType.COMPANY_VERIFY.getValue().byteValue())
						.verifyTime(walletTunnel.getCheckTime())
						.createTime(walletTunnel.getCheckTime()).build());
			}
			walletCard.setVerifyTime(DateUtil.parse(checkTime, DateUtil.STANDARD_DTAETIME_PATTERN));
		} else if (result == YunstCompanyInfoAuditStatus.FAIL.getValue().longValue()) {
			walletTunnel
				.setStatus(EnumDef.WalletTunnelAuditStatus.AUDIT_FAIL.getValue().byteValue());
			walletTunnel.setFailReason(failReason);
		}

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletTunnel);
		if (effectRows != 1) {
			log.error("处理企业信息审核结果通知-更新审核状态状态失败:bizUserId:{}", bizUserId);
		}

		effectRows = walletCardDao.updateByPrimaryKeySelective(walletCard);
		if (effectRows != 1) {
			log.error("处理企业信息审核结果通知-更新银行卡信息失败:bizUserId:{}", bizUserId);
		}
		return SLWalletMqMessage.builder().walletId(walletTunnel.getWalletId())
			.isPass(isPass).checkTime(checkTime).failReason(failReason).build();
	}

	public void handleSignContractResult(ChannelNotify channelNotify,
		YunstNotify.SignContractResult rtnVal) {
		log.info("处理会员电子签约通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setIsSignContact(WalletTunnelSignContract.MEMBER.getValue().byteValue());

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("更新电子签约状态失败:bizUserId:{}", bizUserId);
		}
	}

	public void handleSignBalanceContractResult(ChannelNotify channelNotify,
		YunstNotify.BalanceContractResult rtnVal) {
		log.info("处理扣款协议签约通知 rtnVal:{}", rtnVal);
		String reqSn = rtnVal.getProtocolReqSn();
		String signStatus = rtnVal.getSignStatus();
		String protocolNo = rtnVal.getProtocolNo();

		WalletTunnel walletTunnel = walletTunnelExtDao.selectByBanlceProtocolReqSn(reqSn);
		if (Objects.isNull(walletTunnel)) {
			log.error("查无对应walletTunnel, reqSn:{}", reqSn);
			return;
		}
		String bizUserId = walletTunnel.getBizUserId();
		channelNotify.setBizUserId(bizUserId);

		if (YUNST_SIGN_SUCCESS.equalsIgnoreCase(signStatus)) {
			walletTunnel
				.setIsSignContact(WalletTunnelSignContract.BALANCE.getValue().byteValue());
			walletTunnel.setBalanceProtocolNo(protocolNo);
			int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletTunnel);
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
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setSecurityTel(newPhone);

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人会员修改绑定手机失败:bizUserId:{},newPhone:{}", bizUserId, newPhone);
		}
	}

	public void handleSetPayPwdResult(ChannelNotify channelNotify,
		YunstNotify.SetPayPwd rtnVal) {
		log.info("处理个人设置支付密码通知 rtnVal:{}", rtnVal);
		String bizUserId = rtnVal.getBizUserId();
		channelNotify.setBizUserId(bizUserId);
		WalletTunnel walletChannel = walletTunnelExtDao.selectByTunnelTypeAndBizUserId(
			TunnelType.YUNST.getValue().intValue(), bizUserId);

		walletChannel.setHasPayPassword(WalletTunnelSetPayPwd.YES.getValue().byteValue());

		int effectRows = walletTunnelExtDao.updateByPrimaryKeySelective(walletChannel);
		if (effectRows != 1) {
			log.error("处理个人设置支付密码失败:bizUserId:{}", bizUserId);
		}
	}

}
