package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 重做问题单 */
@Builder
public class RedoWalletApplyRequest extends  AbstractApiRequest {

  @ApiModelProperty("流水id")
  private Long walletLogId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/redo_pay";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return Map.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(walletLogId != null){
        parameters.put("wallet_log_id", walletLogId.toString());
      }
    return parameters;
  }
}
