package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-代付结果查询 */
@Builder
public class AgentPayQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("代付单号")
  private String payOrderNo ;

  @ApiModelProperty("应用令牌")
  private String accessToken ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/agent_pay_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletClearing.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(payOrderNo != null){
        parameters.put("pay_order_no", payOrderNo.toString());
      }
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
    return parameters;
  }
}
