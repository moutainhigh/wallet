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

  @ApiModelProperty("代收单据ID")
  private Long collectId ;

  @ApiModelProperty("refundList")
  private List<RefundInfo> refundList ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/refund";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return RefundResult.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(collectId != null){
        parameters.put("collect_id", collectId.toString());
      }
      if(refundList != null){
        parameters.put("refundList", refundList.toString());
      }
    return parameters;
  }
}
