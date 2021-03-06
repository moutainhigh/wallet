package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 终端管理-终端列表 */
@Builder
public class QueryTerminalRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("limit")
  private Integer limit ;

  @ApiModelProperty("offset")
  private Integer offset ;

  @ApiModelProperty("地区码")
  private String areaCode ;

  @ApiModelProperty("主收款钱包id")
  private Long proxyWalletId ;

  @ApiModelProperty("状态： 0：未绑定，1：已绑定，2：已解绑")
  private Integer status ;

  @ApiModelProperty("子商户号")
  private String vspCusid ;

  @ApiModelProperty("终端号")
  private String vspTermid ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/query_terminal";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWalletTerminalExt.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(areaCode != null){
        parameters.put("area_code", areaCode.toString());
      }
      if(proxyWalletId != null){
        parameters.put("proxy_wallet_id", proxyWalletId.toString());
      }
      if(status != null){
        parameters.put("status", status.toString());
      }
      if(vspCusid != null){
        parameters.put("vsp_cusid", vspCusid.toString());
      }
      if(vspTermid != null){
        parameters.put("vsp_termid", vspTermid.toString());
      }
    return parameters;
  }
}
