package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-订单结果查询 */
@Builder
public class OrderQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("统一订单号")
  private String orderNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/order_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletOrder.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(orderNo != null){
        parameters.put("order_no", orderNo.toString());
      }
    return parameters;
  }
}
