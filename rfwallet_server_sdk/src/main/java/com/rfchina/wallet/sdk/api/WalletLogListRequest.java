package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 钱包流水 */
@Builder
public class WalletLogListRequest extends  AbstractApiRequest {

  @ApiModelProperty("需要查询的数量（数量最大50）")
  private Integer limit ;

  @ApiModelProperty("查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取")
  private Long offset ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("结束时间")
  private String endTime ;

  @ApiModelProperty("开始时间")
  private String startTime ;

  @ApiModelProperty("非必填, false:否, true:是, 是否返回数据总量, 默认false")
  private Boolean stat ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/wallet/log/list";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWalletLog.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(endTime != null){
        parameters.put("end_time", endTime.toString());
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
