package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-充值 */
@Builder
public class RechargeRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("充值内容，参考RechargeReq结构体")
  private String rechargeReq ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/recharge";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return RechargeResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(rechargeReq != null){
        parameters.put("recharge_req", rechargeReq.toString());
      }
    return parameters;
  }
}
