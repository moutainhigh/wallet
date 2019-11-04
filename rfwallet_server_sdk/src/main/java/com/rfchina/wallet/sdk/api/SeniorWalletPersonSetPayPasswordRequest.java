package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-个人设置支付密码 */
@Builder
public class SeniorWalletPersonSetPayPasswordRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("身份证")
  private String identityNo ;

  @ApiModelProperty("前端跳转地址")
  private String jumpUrl ;

  @ApiModelProperty("姓名")
  private String name ;

  @ApiModelProperty("绑定手机")
  private String phone ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/person_set_paypassword";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PageVo.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(identityNo != null){
        parameters.put("identity_no", identityNo.toString());
      }
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
      if(name != null){
        parameters.put("name", name.toString());
      }
      if(phone != null){
        parameters.put("phone", phone.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
