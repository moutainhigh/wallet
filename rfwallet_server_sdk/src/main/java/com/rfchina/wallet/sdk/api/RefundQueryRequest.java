package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-退款结果查询 */
@Builder
public class RefundQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("退款单号")
  private String refundOrderNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/refund_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletRefund.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(refundOrderNo != null){
        parameters.put("refund_order_no", refundOrderNo.toString());
      }
    return parameters;
  }
}
