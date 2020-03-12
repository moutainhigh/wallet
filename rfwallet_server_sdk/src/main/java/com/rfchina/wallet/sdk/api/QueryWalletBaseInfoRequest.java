package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 查询钱包信息（企业or个人） */
@Builder
public class QueryWalletBaseInfoRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("钱包ID")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/u/wallet/query_wallet_base_info";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletBaseInfoVo.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
