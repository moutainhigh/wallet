package com.rfchina.wallet.server.adapter;

import com.rfchina.api.response.ResponseData;
import com.rfchina.platform.common.exception.RfchinaResponseException;
import com.rfchina.platform.common.http.HttpUtil;
import com.rfchina.platform.common.misc.ResponseCode;
import com.rfchina.platform.common.security.SecurityCoder;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.platform.common.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class UserAdapter {

    @Value(value = "${app.base.url}")
    private String appBaseUrl;

    @Value(value = "${app.id}")
    private Long appId;

    @Value(value = "${app.secret}")
    private String appSecret;

    /**
     * 用户账号实名认证
     *
     * @param accessToken	用户令牌
     * @param accountType	账号类型, 1:手机, 2:银行卡, 3:存折, 4:信用卡
     * @param name			身份证姓名, 用户未进行过实名认证时必须, 如果已实名, 以之前实名信息为准
     * @param idNo			身份证号, 用户未进行过实名认证时必须, 以之前实名信息为准
     * @param bankCode		银行类必须, 银行代码
     * @param accountNo		账号, 银行类必须, 手机类型自动获取当前账号绑定的手机号码
     */
    public void userRealAccount(String accessToken, Integer accountType, String name, String idNo, String bankCode, String accountNo){
        Map<String, String> params = new HashMap<>();

        params.put("access_token", accessToken);
        params.put("account_type", String.valueOf(accountType));

        Optional.ofNullable(name).ifPresent( o -> params.put("name", name));

        Optional.ofNullable(idNo).ifPresent( o -> params.put("id_no", idNo));

        Optional.ofNullable(bankCode).ifPresent(o->params.put("bank_code", bankCode));
        Optional.ofNullable(accountNo).ifPresent(o->params.put("account_no", accountNo));

        params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = SignUtil.sign(params, SecurityCoder.md5((appSecret + appId).getBytes()));
        params.put("sign", sign);

        String response = null;

        try {
            response = HttpUtil
                    .post(appBaseUrl + "/v1/user/real/account", params);

        } catch(Exception e){
            log.error("",e);
        } finally {
            log.info("个人身份验证, request: {}, response: {}", params, response);
        }

        ResponseData responseData = JsonUtil.toObject(response, ResponseData.class, objectMapper -> {
            objectMapper.setPropertyNamingStrategy(
                    com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE);
        });

        if(responseData.getCode() != ResponseCode.EnumResponseCode.COMMON_SUCCESS.getValue()){
            log.error("userRealAccount error, response: {}", JsonUtil.toJSON(responseData));
            throw new RfchinaResponseException(responseData.getCode(), responseData.getMsg());
        }
    }
}
