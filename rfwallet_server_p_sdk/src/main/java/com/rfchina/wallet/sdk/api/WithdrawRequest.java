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

  @ApiModelProperty("金额")
  private Long amount ;

  @ApiModelProperty("银行卡id")
  private Long cardId ;

  @ApiModelProperty("客户Ip")
  private String customerIp ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("跳转地址")
  private String jumpUrl ;

  @ApiModelProperty("交易验证方式 0：无验证 1：短信 2：密码")
  private Integer validateType ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/withdraw";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WithdrawResp.class;
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
      if(customerIp != null){
        parameters.put("customer_ip", customerIp.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
      if(validateType != null){
        parameters.put("validate_type", validateType.toString());
      }
    return parameters;
  }
}
