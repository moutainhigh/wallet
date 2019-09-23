package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

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

  @ApiModelProperty("业务用户id")
  private Long bizUserId ;

  @ApiModelProperty("渠道类型 1:浦发银企直连,2:通联云商通")
  private Integer channelType ;

  @ApiModelProperty("触发图形验证码并验证成功后重定向地址")
  private String redirectUrl ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;


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
      if(bizUserId != null){
        parameters.put("biz_user_id", bizUserId.toString());
      }
      if(channelType != null){
        parameters.put("channel_type", channelType.toString());
      }
      if(redirectUrl != null){
        parameters.put("redirect_url", redirectUrl.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
    return parameters;
  }
}
