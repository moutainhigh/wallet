package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-短信确认 */
@Builder
public class SmsConfirmRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("客户ip")
  private String customerIp ;

  @ApiModelProperty("业务令牌")
  private String ticket ;

  @ApiModelProperty("短信验证码")
  private String verifyCode ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/sms_confirm";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return Map.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(customerIp != null){
        parameters.put("customer_ip", customerIp.toString());
      }
      if(ticket != null){
        parameters.put("ticket", ticket.toString());
      }
      if(verifyCode != null){
        parameters.put("verify_code", verifyCode.toString());
      }
    return parameters;
  }
}
