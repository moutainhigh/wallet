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
public class ChargingDetailQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("访问令牌")
  private String accessToken ;

  @ApiModelProperty("升序排序")
  private Boolean asc ;

  @ApiModelProperty("结束时间")
  private String endTime ;

  @ApiModelProperty("必填，需要查询的数量（数量最大50）")
  private Integer limit ;

  @ApiModelProperty("必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。")
  private Integer offset ;

  @ApiModelProperty("开始时间")
  private String startTime ;

  @ApiModelProperty("非必填, false:否, true:是, 是否返回数据总量, 默认false")
  private Boolean stat ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/charging_detail";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationStatChargingDetailVo.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(asc != null){
        parameters.put("asc", asc.toString());
      }
      if(endTime != null){
        parameters.put("end_time", endTime.toString());
      }
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(startTime != null){
        parameters.put("start_time", startTime.toString());
      }
      if(stat != null){
        parameters.put("stat", stat.toString());
      }
    return parameters;
  }
}
