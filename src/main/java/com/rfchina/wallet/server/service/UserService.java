package com.rfchina.wallet.server.service;

import com.rfchina.api.response.model.user.SendSmsVerifyToMobileResponseModel;
import com.rfchina.api.response.model.user.UserLoginTokenResponseModel;
import com.rfchina.internal.api.request.user.SendSmsVerifyToMobileRequest;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.domain.misc.EnumDef;
import com.rfchina.wallet.domain.misc.MsgConstant;
import com.rfchina.wallet.domain.misc.WalletResponseCode;
import com.rfchina.wallet.server.adapter.UserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppService appService;

    /**
     * 发送短信验证码
     *
     * @param enumVerifyCodeType	短信验证码类型
     * @param mobile				手机号码
     * @param token					反作弊结果查询token
     * @param redirectUrl			触发图形验证码并验证成功后重定向地址
     * @return
     */
    public ResponseValue sendSmsVerifyCode(EnumDef.EnumVerifyCodeType enumVerifyCodeType, String mobile, String token, String redirectUrl, String msg, String ip) {
        SendSmsVerifyToMobileResponseModel sendSmsVerifyToMobileResponseModel = userAdapter.sendSmsToMobileWithCheck(appService.getWalletAccessToken(), mobile, EnumDef.EnumVerifyCodeType.LOGIN.getValue(), msg,
                MsgConstant.SMS_EFFECTIVE_TIME, redirectUrl, packageClientInfo(token, ip));

        cacheService.setVerifyCodeToken(enumVerifyCodeType.getValue(), mobile, sendSmsVerifyToMobileResponseModel.getVerifyToken());

        Map<String, Object> data = new HashMap<>();
        if(sendSmsVerifyToMobileResponseModel.getCaptchaUrl() != null){
            data.put("captcha_url", sendSmsVerifyToMobileResponseModel.getCaptchaUrl());
        }

        return new ResponseValue<>(data.isEmpty()? ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue(): WalletResponseCode.EnumWalletResponseCode.NEED_CAPTCHA.getValue(), null, data);
    }

    /**
     * 构造client_info客户端信息
     * @param token		反作弊结果查询token
     * @return
     */
    private Map<String, String> packageClientInfo(String token, String ip){
        Map<String, String> clientInfo = new HashMap<>();
        clientInfo.put("token", token);
        clientInfo.put("ip", ip);
        return clientInfo;
    }

    /**
     * 用户通过短信验证码登录
     * @param mobile		用户手机
     * @param verifyCode	短信验证码
     * @return
     */
    public UserLoginTokenResponseModel userLoginWithVerifyCode(String mobile, String verifyCode, String ip){
        String token = cacheService.getVerifyCodeToken(SendSmsVerifyToMobileRequest.Type.VERIFY_CODE_LOGIN.getValue(), mobile);
        if (null == token) {
            throw new RfchinaResponseException(ResponseCode.EnumResponseCode.COMMON_EXPIRED_VERIFY_CODE);
        }

        return userAdapter
                .userLoginWithVerifyRequest(appService.getWalletAccessToken(), mobile, verifyCode, token, ip);
    }
}
