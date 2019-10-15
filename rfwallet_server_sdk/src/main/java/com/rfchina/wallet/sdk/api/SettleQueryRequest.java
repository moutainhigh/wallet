package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-清算结果查询 */
@Builder
public class SettleQueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("工单ID")
  private Long applyId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/settle_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListWalletClearing.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(applyId != null){
        parameters.put("apply_id", applyId.toString());
      }
    return parameters;
  }
}
