package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 绑定银行卡(对公) */
@Builder
public class BindBankCardRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("银行帐号")
  private String bankAccount ;

  @ApiModelProperty("开户支行")
  private String bankCode ;

  @ApiModelProperty("开户名")
  private String depositName ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("是否默认银行卡: 1:是，2：否")
  private Integer isDef ;

  @ApiModelProperty("预留手机号")
  private String telephone ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/bank_card/bind";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletCard.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(bankAccount != null){
        parameters.put("bank_account", bankAccount.toString());
      }
      if(bankCode != null){
        parameters.put("bank_code", bankCode.toString());
      }
      if(depositName != null){
        parameters.put("deposit_name", depositName.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(isDef != null){
        parameters.put("is_def", isDef.toString());
      }
      if(telephone != null){
        parameters.put("telephone", telephone.toString());
      }
    return parameters;
  }
}
