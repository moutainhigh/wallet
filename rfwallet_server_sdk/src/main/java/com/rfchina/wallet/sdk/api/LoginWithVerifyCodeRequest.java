package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 通过短信验证码登录 */
@Builder
public class LoginWithVerifyCodeRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("来源IP")
  private String ip ;

  @ApiModelProperty("手机号码")
  private String mobile ;

  @ApiModelProperty("短信类型, 1:登录当前钱包, 2:登录已开通钱包")
  private Integer type ;

  @ApiModelProperty("验证码")
  private String verifyCode ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/login_with_verify_code";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletUser.class;
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
      if(verifyCode != null){
        parameters.put("verify_code", verifyCode.toString());
      }
    return parameters;
  }
}
