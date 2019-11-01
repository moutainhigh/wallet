package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-充值确认 */
@Builder
public class PassWordConfirmRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("客户ip")
  private String customerIp ;

  @ApiModelProperty("回调url")
  private String jumpUrl ;

  @ApiModelProperty("业务令牌")
  private String ticket ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/password_confirm";
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
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
      if(ticket != null){
        parameters.put("ticket", ticket.toString());
      }
    return parameters;
  }
}
