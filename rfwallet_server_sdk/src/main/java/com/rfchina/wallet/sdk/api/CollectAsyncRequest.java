package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-定时代收 */
@Builder
public class CollectAsyncRequest extends  AbstractApiRequest {

  @ApiModelProperty("collectReq")
  private CollectReq collectReq ;

  @ApiModelProperty("应用令牌")
  private String accessToken ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/collect_async";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletCollect.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(collectReq != null){
        parameters.put("collectReq", collectReq.toString());
      }
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
    return parameters;
  }
}
