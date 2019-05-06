package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 初级钱包-查询支付状态 */
@Builder
public class QueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("钱包批次号")
  private String batchNo ;

  @ApiModelProperty("业务凭证号(业务方定义唯一)")
  private String bizNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/sl_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListPayStatusResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(batchNo != null){
        parameters.put("batch_no", batchNo.toString());
      }
      if(bizNo != null){
        parameters.put("biz_no", bizNo.toString());
      }
    return parameters;
  }
}
