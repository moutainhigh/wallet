package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-对账文件 */
@Builder
public class BalanceFileRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("开始时间")
  private String beginDate ;

  @ApiModelProperty("结束时间")
  private String endDate ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/balance_file";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListBalanceJob.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(beginDate != null){
        parameters.put("begin_date", beginDate.toString());
      }
      if(endDate != null){
        parameters.put("end_date", endDate.toString());
      }
    return parameters;
  }
}
