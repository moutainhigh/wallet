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

  @ApiModelProperty("客户Ip")
  private String customerIp ;

  @ApiModelProperty("跳转地址")
  private String jumpUrl ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/collect_sync";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletCollectResp.class;
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
      if(customerIp != null){
        parameters.put("customer_ip", customerIp.toString());
      }
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
    return parameters;
  }
}
