package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-终端列表 */
@Builder
public class QueryTerminalRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("limit")
  private Integer limit ;

  @ApiModelProperty("商家id")
  private String mchId ;

  @ApiModelProperty("offset")
  private Integer offset ;

  @ApiModelProperty("省份")
  private String province ;

  @ApiModelProperty("子商户号")
  private String vspCusid ;

  @ApiModelProperty("终端号")
  private String vspTermid ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/query_terminal";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWalletTerminal.class;
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
      if(mchId != null){
        parameters.put("mch_id", mchId.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(province != null){
        parameters.put("province", province.toString());
      }
      if(vspCusid != null){
        parameters.put("vsp_cusid", vspCusid.toString());
      }
      if(vspTermid != null){
        parameters.put("vsp_termid", vspTermid.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
