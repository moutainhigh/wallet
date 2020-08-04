package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 终端管理-地区绑定子商户 */
@Builder
public class BindVspCusidRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("通联AppId")
  private String appId ;

  @ApiModelProperty("地区码")
  private String areaCode ;

  @ApiModelProperty("创建人id")
  private String creatorId ;

  @ApiModelProperty("创建人名称")
  private String creatorName ;

  @ApiModelProperty("主收款人钱包id")
  private Long proxyWalletId ;

  @ApiModelProperty("子商户号")
  private String vspCusid ;

  @ApiModelProperty("集团商户号")
  private String vspMerchantid ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/bind_vsp_cusid";
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
      if(appId != null){
        parameters.put("app_id", appId.toString());
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
      if(proxyWalletId != null){
        parameters.put("proxy_wallet_id", proxyWalletId.toString());
      }
      if(vspCusid != null){
        parameters.put("vsp_cusid", vspCusid.toString());
      }
      if(vspMerchantid != null){
        parameters.put("vsp_merchantid", vspMerchantid.toString());
      }
    return parameters;
  }
}
