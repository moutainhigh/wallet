package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-导出余额明细 */
@Builder
public class ExportOrderDetailRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("交易时间开始")
  private String beginTime ;

  @ApiModelProperty("交易时间结束")
  private String endTime ;

  @ApiModelProperty("文件名称")
  private String fileName ;

  @ApiModelProperty("唯一码")
  private String uniqueCode ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("状态")
  private Integer status ;

  @ApiModelProperty("交易类型")
  private Integer tradeType ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/report/export_order_detail";
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
      if(beginTime != null){
        parameters.put("begin_time", beginTime.toString());
      }
      if(endTime != null){
        parameters.put("end_time", endTime.toString());
      }
      if(fileName != null){
        parameters.put("file_name", fileName.toString());
      }
      if(uniqueCode != null){
        parameters.put("unique_code", uniqueCode.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(status != null){
        parameters.put("status", status.toString());
      }
      if(tradeType != null){
        parameters.put("trade_type", tradeType.toString());
      }
    return parameters;
  }
}
