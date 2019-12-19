package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 手续费报表 */
@Builder
public class ChargingQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("limit")
  private Integer limit ;

  @ApiModelProperty("offset")
  private Integer offset ;

  @ApiModelProperty("stat")
  private Boolean stat ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/charging_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationStatCharging.class;
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
      if(stat != null){
        parameters.put("stat", stat.toString());
      }
    return parameters;
  }
}
