package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-个人认证 */
@Builder
public class SeniorWalletPersonAuthenticationRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("渠道类型 1:浦发银企直连,2:通联云商通")
  private Integer channelType ;

  @ApiModelProperty("身份证号")
  private String idNo ;

  @ApiModelProperty("前端跳转地址")
  private String jumpUrl ;

  @ApiModelProperty("手机号码")
  private String mobile ;

  @ApiModelProperty("姓名")
  private String realName ;

  @ApiModelProperty("短信验证码")
  private String verifyCode ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/senior_person_authentication";
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
      if(channelType != null){
        parameters.put("channel_type", channelType.toString());
      }
      if(idNo != null){
        parameters.put("id_no", idNo.toString());
      }
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
      if(mobile != null){
        parameters.put("mobile", mobile.toString());
      }
      if(realName != null){
        parameters.put("real_name", realName.toString());
      }
      if(verifyCode != null){
        parameters.put("verify_code", verifyCode.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
