package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 当月手续费报表 */
@Builder
public class CurMonthChargingQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("访问令牌")
  private String accessToken ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/cur_charging_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return StatCharging.class;
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
