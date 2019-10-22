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

  @ApiModelProperty("退款单号")
  private String refundOrderNo ;

  @ApiModelProperty("应用令牌")
  private String accessToken ;


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
      if(refundOrderNo != null){
        parameters.put("refund_order_no", refundOrderNo.toString());
      }
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
    return parameters;
  }
}
