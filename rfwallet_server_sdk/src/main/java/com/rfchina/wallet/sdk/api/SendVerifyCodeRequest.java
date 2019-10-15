package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 发送短信验证码 */
@Builder
public class SendVerifyCodeRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("来源IP")
  private String ip ;

  @ApiModelProperty("手机号码")
  private String mobile ;

  @ApiModelProperty("验证码类型, 1:登录, 2:验证已开通钱包帐号")
  private Integer type ;

  @ApiModelProperty("反作弊结果查询token")
  private String verifyToken ;

  @ApiModelProperty("触发图形验证码并验证成功后重定向地址")
  private String redirectUrl ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/send_verify_code";
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
      if(ip != null){
        parameters.put("ip", ip.toString());
      }
      if(mobile != null){
        parameters.put("mobile", mobile.toString());
      }
      if(type != null){
        parameters.put("type", type.toString());
      }
      if(verifyToken != null){
        parameters.put("verify_token", verifyToken.toString());
      }
      if(redirectUrl != null){
        parameters.put("redirect_url", redirectUrl.toString());
      }
    return parameters;
  }
}
