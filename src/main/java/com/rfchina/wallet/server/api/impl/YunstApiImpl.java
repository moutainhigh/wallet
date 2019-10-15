package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.WalletChannelMapper;
import com.rfchina.wallet.domain.model.WalletChannel;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.msic.EnumWallet.TunnelType;
import com.rfchina.wallet.server.service.handler.yunst.YunstUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class YunstApiImpl implements YunstApi {
	@Autowired
	private YunstUserHandler yunstHandler;
	@Autowired
	private WalletChannelMapper walletChannelMapper;

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public YunstCreateMemberResult createYunstMember(String accessToken, String bizUserId, Integer type)
			throws Exception {
		YunstCreateMemberResult result = yunstHandler.createMember(bizUserId, type);
		if (result != null) {
			log.info("云商通创建会员成功: 云商通会员id:{},业务用户id:{}", result.getUserId(), result.getBizUserId());
			int effectRows = walletChannelMapper.insertSelective(WalletChannel.builder()
					.channelType(TunnelType.YUNST.getValue())
					.channelUserId(result.getUserId())
					.channelType(type == 1 ? YunstUserHandler.YunstMemberType.COMPANY.getValue().byteValue():YunstUserHandler.YunstMemberType.PERSON.getValue().byteValue())
					.bizUserId(result.getBizUserId())
					.createTime(new Date())
					.build());

			if (effectRows != 1) {
				log.error("记录云商通会员失败: 云商通会员id:{},业务用户id:{}", result.getUserId(), result.getBizUserId());
				throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE, "记录云商通会员失败");
			}
		}
		return result;
	}

//	@Log
//	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
//	@SignVerify
//	@Override
//	public Tuple<Boolean, String> requestSmsVerifyCode(String accessToken, String bizUserId, Integer type,
//			String phone,
//			Integer bizType) throws Exception {
//		Tuple<Boolean, String> tuple = yunstHandler.sendVerificationCode(bizUserId, type, phone, bizType);
//		if (!tuple.left) {
//			log.error("云商通请求发送短信验证码失败, 业务用户id:{},电话:{},短信验证码业务类型:{},原因:{}", bizUserId, phone, bizType, tuple.right);
//		}
//		return tuple;
//	}

//	@Override
//	public Tuple<Boolean, String> bindPhone(String accessToken, String bizUserId, Integer type, String phone,
//			String verificationCode) throws Exception {
//		Tuple<Boolean, String> tuple = yunstHandler.bindPhone(bizUserId, type, phone, verificationCode);
//		if (!tuple.left) {
//			log.error("云商通绑定手机失败, 业务用户id:{},电话:{},原因:{}", bizUserId, phone, tuple.right);
//		}
//		return tuple;
//	}

//	@Override
//	public Tuple<Boolean, String> modifyPhone(String accessToken, String bizUserId, Integer type, String oldPhone,
//			String newPhone, String verificationCode) throws Exception {
//		Tuple<Boolean, String> tuple = yunstHandler.modifyPhone(bizUserId, type, oldPhone, newPhone, verificationCode);
//		if (!tuple.left) {
//			log.error("云商通修改绑定手机失败, 业务用户id:{},旧电话:{},新电话:{},原因:{}", bizUserId, oldPhone, newPhone, tuple.right);
//		}
//		return tuple;
//	}

//	@Override
//	public YunstMemberInfoResp getMemberInfo(String accessToken, String bizUserId, Integer type) throws Exception {
//		return null;
//	}

}
