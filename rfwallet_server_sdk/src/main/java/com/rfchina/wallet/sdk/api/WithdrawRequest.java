package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-提现 */
@Builder
public class WithdrawRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("充值内容，参考WithdrawReq结构体")
  private String withdrawReq ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/withdraw";
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
      if(withdrawReq != null){
        parameters.put("withdraw_req", withdrawReq.toString());
      }
    return parameters;
  }
}
