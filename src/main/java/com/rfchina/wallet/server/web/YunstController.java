package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.util.CommonGatewayException;
import com.rfchina.wallet.server.msic.UrlConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class YunstController {
	@Autowired
	private YunstApi yunstApi;

	@ApiOperation("云商通-创建会员")
	@PostMapping(UrlConstant.YUNST_CREATE_MEMBER)
	public ResponseValue<String> createYunstMember(@RequestParam("access_token") String accessToken,
			@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
			@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type)
			throws Exception {
		YunstCreateMemberResult result = null;
		try {
			result = yunstApi.createYunstMember(accessToken, bizUserId, type);
		} catch (CommonGatewayException e) {
			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE.getValue(), e.getBankErrMsg());
		}
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, result.getUserId());
	}

//	@ApiOperation("云商通-请求短信验证码")
//	@PostMapping(UrlConstant.YUNST_SMS_VERIFY_CODE)
//	public ResponseValue<String> sendSMSVerificationCode(@RequestParam("access_token") String accessToken,
//			@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
//			@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type,
//			@ApiParam(value = "手机号", required = true, example = "13800138000") @RequestParam("phone") String phone,
//			@ApiParam(value = "验证码业务类型", required = true, example = "1") @RequestParam("biz_type") Integer bizType)
//			throws Exception {
//		Tuple<Boolean, String> resp = yunstApi.requestSmsVerifyCode(accessToken, bizUserId, type, phone, bizType);
//		if (!resp.left) {
//			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE, resp.right);
//		}
//		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, "OK");
//	}

//	@ApiOperation("云商通-绑定手机")
//	@PostMapping(UrlConstant.YUNST_BIND_PHONE)
//	public ResponseValue<String> bindPhone(@RequestParam("access_token") String accessToken,
//			@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
//			@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type,
//			@ApiParam(value = "手机号", required = true, example = "13800138000") @RequestParam("phone") String phone,
//			@ApiParam(value = "验证码", required = true, example = "123456") @RequestParam("verification_code")
//					String verificationCode) throws Exception {
//		Tuple<Boolean, String> resp = yunstApi.bindPhone(accessToken, bizUserId, type, phone, verificationCode);
//		if (!resp.left) {
//			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE, resp.right);
//		}
//		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, "OK");
//	}

//	@ApiOperation("云商通-修改绑定手机")
//	@PostMapping(UrlConstant.YUNST_MODIFY_PHONE)
//	public ResponseValue<String> bindPhone(@RequestParam("access_token") String accessToken,
//			@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
//			@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type,
//			@ApiParam(value = "旧手机号", required = true, example = "13800138000") @RequestParam("old_phone")
//					String oldPhone,
//			@ApiParam(value = "新手机号", required = true, example = "13800138000") @RequestParam("new_phone")
//					String newPhone,
//			@ApiParam(value = "验证码", required = true, example = "123456") @RequestParam("verification_code")
//					String verificationCode) throws Exception {
//		Tuple<Boolean, String> resp = yunstApi.modifyPhone(accessToken, bizUserId, type, oldPhone, newPhone,
//				verificationCode);
//		if (!resp.left) {
//			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE, resp.right);
//		}
//		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, "OK");
//	}

//	@ApiOperation("云商通-查询会员信息")
//	@PostMapping(UrlConstant.YUNST_MEMBER_INFO)
//	public ResponseValue<Object> memberInfo(@RequestParam("access_token") String accessToken,
//			@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
//			@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type)
//			throws Exception {
//		YunstMemberInfoResp resp = yunstApi.getMemberInfo(accessToken, bizUserId, type);
//		if (resp.getData() == null) {
//			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE, resp.getErrorMsg());
//		}
//		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp.getData());
//	}
}
