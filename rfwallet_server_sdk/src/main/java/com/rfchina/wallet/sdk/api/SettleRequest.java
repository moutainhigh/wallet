package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-清算 */
@Builder
public class SettleRequest extends  AbstractApiRequest {

  @ApiModelProperty("代收单据ID")
  private Long collectId ;

  @ApiModelProperty("代付列表（与代收的分账规则对应）")
  private SettleReq settleReq ;

  @ApiModelProperty("应用令牌")
  private String accessToken ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/settle";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return SettleResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(collectId != null){
        parameters.put("collect_id", collectId.toString());
      }
      if(settleReq != null){
        parameters.put("settleReq", settleReq.toString());
      }
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
    return parameters;
  }
}
