package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-余额明细 */
@Builder
public class SeniorOrderDetailRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("关联订单号")
  private String bizNo ;

  @ApiModelProperty("交易时间开始")
  private String fromTime ;

  @ApiModelProperty("每页限制")
  private Integer limit ;

  @ApiModelProperty("起始页偏移量")
  private Integer offset ;

  @ApiModelProperty("钱包订单号")
  private String orderNo ;

  @ApiModelProperty("是否统计")
  private Boolean stat ;

  @ApiModelProperty("状态")
  private Integer status ;

  @ApiModelProperty("交易时间结束")
  private String toTime ;

  @ApiModelProperty("交易类型")
  private Integer tradeType ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/order_detail";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWalletOrder.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(bizNo != null){
        parameters.put("biz_no", bizNo.toString());
      }
      if(fromTime != null){
        parameters.put("from_time", fromTime.toString());
      }
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(orderNo != null){
        parameters.put("order_no", orderNo.toString());
      }
      if(stat != null){
        parameters.put("stat", stat.toString());
      }
      if(status != null){
        parameters.put("status", status.toString());
      }
      if(toTime != null){
        parameters.put("to_time", toTime.toString());
      }
      if(tradeType != null){
        parameters.put("trade_type", tradeType.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
