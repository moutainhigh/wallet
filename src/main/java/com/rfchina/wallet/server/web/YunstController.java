package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.api.YunstApi;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.yunst.response.YunstCreateMemberResp;
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
	@PostMapping(UrlConstant.JUNIOR_WALLET_PAY_IN)
	public ResponseValue<String> createYunstMember(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "业务用户id", required = true, example = "123") @RequestParam("biz_user_id") String bizUserId,
		@ApiParam(value = "业务用户类型", required = true, example = "1") @RequestParam("type") Integer type
	) throws Exception {
		YunstCreateMemberResp resp = yunstApi.createYunstMember(accessToken, bizUserId, type);
		if ("1".equals(resp.getStatus())){
			return new ResponseValue<>(EnumResponseCode.COMMON_FAILURE, resp.getErrorMsg());
		}
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp.getData().getUserId());
	}




}
