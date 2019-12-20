package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 手续费重做 */
@Builder
public class ChargingRedoRequest extends  AbstractApiRequest {

  @ApiModelProperty("访问令牌")
  private String accessToken ;

  @ApiModelProperty("结束时间")
  private String endTime ;

  @ApiModelProperty("开始时间")
  private String startTime ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/charging_redo";
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
      if(endTime != null){
        parameters.put("end_time", endTime.toString());
      }
      if(startTime != null){
        parameters.put("start_time", startTime.toString());
      }
    return parameters;
  }
}
