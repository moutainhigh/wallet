package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-商家绑定终端 */
@Builder
public class BindTerminal2Request extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("APPID")
  private String appId ;

  @ApiModelProperty("渠道用户ID")
  private String bizUserId ;

  @ApiModelProperty("子商户号")
  private String vspCusid ;

  @ApiModelProperty("集团号")
  private String vspMerchantid ;

  @ApiModelProperty("终端号")
  private String vspTermid ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/bind_terminal2";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return VspTermidResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(appId != null){
        parameters.put("app_id", appId.toString());
      }
      if(bizUserId != null){
        parameters.put("biz_user_id", bizUserId.toString());
      }
      if(vspCusid != null){
        parameters.put("vsp_cusid", vspCusid.toString());
      }
      if(vspMerchantid != null){
        parameters.put("vsp_merchantid", vspMerchantid.toString());
      }
      if(vspTermid != null){
        parameters.put("vsp_termid", vspTermid.toString());
      }
    return parameters;
  }
}
