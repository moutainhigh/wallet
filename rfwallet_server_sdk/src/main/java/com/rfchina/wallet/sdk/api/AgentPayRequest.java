package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-代付 */
@Builder
public class AgentPayRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("代付列表（与代收的分账规则对应），参考AgentPayReq结构体")
  private String agentPayReq ;

  @ApiModelProperty("代收单号")
  private String collectOrderNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/agent_pay";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return SettleResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(agentPayReq != null){
        parameters.put("agent_pay_req", agentPayReq.toString());
      }
      if(collectOrderNo != null){
        parameters.put("collect_order_no", collectOrderNo.toString());
      }
    return parameters;
  }
}
