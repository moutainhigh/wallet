package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-充值 */
@Builder
public class RechargeRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("金额")
  private Long amount ;

  @ApiModelProperty("银行卡id")
  private Long cardId ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("客户Ip")
  private String customerIp ;

  @ApiModelProperty("跳转地址")
  private String jumpUrl ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/recharge";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return RechargeResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(amount != null){
        parameters.put("amount", amount.toString());
      }
      if(cardId != null){
        parameters.put("card_id", cardId.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(customerIp != null){
        parameters.put("customer_ip", customerIp.toString());
      }
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
    return parameters;
  }
}
