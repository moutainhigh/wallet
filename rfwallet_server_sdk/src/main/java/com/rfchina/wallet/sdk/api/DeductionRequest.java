package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-代扣 */
@Builder
public class DeductionRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("消费内容，参考DeductionReq结构体")
  private String deductionReq ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/deduction";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletCollectResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(deductionReq != null){
        parameters.put("deduction_req", deductionReq.toString());
      }
    return parameters;
  }
}
