package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 生成手续费报表 */
@Builder
public class ExportChargingDetailRequest extends  AbstractApiRequest {

  @ApiModelProperty("访问令牌")
  private String accessToken ;

  @ApiModelProperty("结束时间")
  private String endTime ;

  @ApiModelProperty("文件名称")
  private String fileName ;

  @ApiModelProperty("开始时间")
  private String startTime ;

  @ApiModelProperty("唯一码")
  private String uniqueCode ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/export_charging_detail";
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
      if(endTime != null){
        parameters.put("end_time", endTime.toString());
      }
      if(fileName != null){
        parameters.put("file_name", fileName.toString());
      }
      if(startTime != null){
        parameters.put("start_time", startTime.toString());
      }
      if(uniqueCode != null){
        parameters.put("unique_code", uniqueCode.toString());
      }
    return parameters;
  }
}
