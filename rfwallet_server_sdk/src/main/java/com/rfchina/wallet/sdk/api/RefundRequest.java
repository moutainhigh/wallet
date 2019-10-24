package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-退款 */
@Builder
public class RefundRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("代收单号")
  private String collectOrderNo ;

  @ApiModelProperty("退款清单，参考List<RefundInfo>结构体")
  private String refundList ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/refund";
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
      if(collectOrderNo != null){
        parameters.put("collect_order_no", collectOrderNo.toString());
      }
      if(refundList != null){
        parameters.put("refund_list", refundList.toString());
      }
    return parameters;
  }
}
