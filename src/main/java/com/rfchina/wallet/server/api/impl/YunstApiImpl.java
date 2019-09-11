package com.rfchina.wallet.server.api.impl;

import com.rfchina.passport.token.EnumTokenType;
import com.rfchina.passport.token.TokenVerify;
import com.rfchina.platform.common.annotation.Log;
import com.rfchina.platform.common.annotation.SignVerify;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.mapper.YunstMemberMapper;
import com.rfchina.wallet.domain.model.YunstMember;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.service.yunst.handler.YunstHandler;
import com.rfchina.wallet.server.bank.yunst.response.YunstCreateMemberResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YunstApiImpl implements YunstApi {
	@Autowired
	private YunstHandler yunstHandler;
	@Autowired
	private YunstMemberMapper yunstMemberMapper;

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public YunstCreateMemberResp createYunstMember(String accessToken, String bizUserId, Integer type)
			throws Exception {
		YunstCreateMemberResp response = yunstHandler.createMember(bizUserId, type);
		if (response.getData() != null) {
			YunstCreateMemberResp.CreateMemeberResult data = response.getData();
			int effectRows = yunstMemberMapper.insertSelective(
					YunstMember.builder().memberId(data.getUserId()).type(type.byteValue()).build());

			if (effectRows != 1) {
				log.error("记录云商通会员失败: 云商通会员id:{},业务用户id:{}", data.getUserId(), data.getBizUserId());
				throw new WalletResponseException(ResponseCode.EnumResponseCode.COMMON_FAILURE, "记录云商通会员失败");
			}
		}
		return response;
	}

	@Log
	@TokenVerify(verifyAppToken = true, accept = { EnumTokenType.APP_MANAGER })
	@SignVerify
	@Override
	public Tuple<Boolean, String> requestSmsVerifyCode(String accessToken, String bizUserId, Integer type,
			String phone,
			Integer bizType) throws Exception {
		Tuple<Boolean, String> tuple = yunstHandler.sendVerificationCode(bizUserId, type, phone, bizType);
		if (!tuple.left) {
			log.error("云商通请求发送短信验证码失败, 业务用户id:{},电话:{},短信验证码业务类型:{},原因:{}", bizUserId, phone, bizType, tuple.right);
		}
		return tuple;
	}

	@Override
	public Tuple<Boolean, String> bindPhone(String accessToken, String bizUserId, Integer type, String phone,
			String verificationCode) throws Exception {
		Tuple<Boolean, String> tuple = yunstHandler.bindPhone(bizUserId, type, phone, verificationCode);
		if (!tuple.left) {
			log.error("云商通绑定手机失败, 业务用户id:{},电话:{},原因:{}", bizUserId, phone, tuple.right);
		}
		return tuple;
	}

	@Override
	public Tuple<Boolean, String> modifyPhone(String accessToken, String bizUserId, Integer type, String oldPhone,
			String newPhone, String verificationCode) throws Exception {
		Tuple<Boolean, String> tuple = yunstHandler.modifyPhone(bizUserId, type, oldPhone, newPhone, verificationCode);
		if (!tuple.left) {
			log.error("云商通修改绑定手机失败, 业务用户id:{},旧电话:{},新电话:{},原因:{}", bizUserId, oldPhone, newPhone, tuple.right);
		}
		return tuple;
	}

}
