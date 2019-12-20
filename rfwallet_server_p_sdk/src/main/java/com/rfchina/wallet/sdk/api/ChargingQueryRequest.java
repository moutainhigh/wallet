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

  @ApiModelProperty("访问令牌")
  private String accessToken ;

  @ApiModelProperty("必填，需要查询的数量（数量最大50）")
  private Integer limit ;

  @ApiModelProperty("必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。")
  private Integer offset ;

  @ApiModelProperty("非必填, false:否, true:是, 是否返回数据总量, 默认false")
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
