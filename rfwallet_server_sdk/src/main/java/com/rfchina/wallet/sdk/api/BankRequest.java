package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 银行支行信息 */
@Builder
public class BankRequest extends  AbstractApiRequest {

  @ApiModelProperty("银行编码")
  private String bankCode ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/wallet/bank/bank";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return BankCode.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(bankCode != null){
        parameters.put("bank_code", bankCode.toString());
      }
    return parameters;
  }
}
