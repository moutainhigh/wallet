package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 钱包配置-获取全局配置 */
@Builder
public class GetUniConfigRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/get_uni_config";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletConfig.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
    return parameters;
  }
}
