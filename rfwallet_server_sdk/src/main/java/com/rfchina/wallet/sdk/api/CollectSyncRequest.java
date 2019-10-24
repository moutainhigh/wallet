package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-即刻代收 */
@Builder
public class CollectSyncRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("代收内容，参考CollectReq结构体")
  private String collectReq ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/collect_sync";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletCollect.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(collectReq != null){
        parameters.put("collect_req", collectReq.toString());
      }
    return parameters;
  }
}
