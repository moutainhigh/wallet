package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 终端管理-地区绑定终端 */
@Builder
public class BindTerminalRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("地区码")
  private String areaCode ;

  @ApiModelProperty("创建人id")
  private String creatorId ;

  @ApiModelProperty("创建人名称")
  private String creatorName ;

  @ApiModelProperty("终端号")
  private String vspTermid ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/bind_terminal";
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
      if(areaCode != null){
        parameters.put("area_code", areaCode.toString());
      }
      if(creatorId != null){
        parameters.put("creator_id", creatorId.toString());
      }
      if(creatorName != null){
        parameters.put("creator_name", creatorName.toString());
      }
      if(vspTermid != null){
        parameters.put("vsp_termid", vspTermid.toString());
      }
    return parameters;
  }
}
