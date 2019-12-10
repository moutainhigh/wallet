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

  @ApiModelProperty("对账日期 yyyy-MM-dd")
  private String balanceDate ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/balance_file";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return BalanceJob.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(balanceDate != null){
        parameters.put("balance_date", balanceDate.toString());
      }
    return parameters;
  }
}
