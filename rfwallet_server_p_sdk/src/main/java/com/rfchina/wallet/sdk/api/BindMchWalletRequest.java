package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 绑定商家钱包 */
@Builder
public class BindMchWalletRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("商家ID")
  private String mchId ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/bind_mch_wallet";
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
      if(mchId != null){
        parameters.put("mch_id", mchId.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
    return parameters;
  }
}
