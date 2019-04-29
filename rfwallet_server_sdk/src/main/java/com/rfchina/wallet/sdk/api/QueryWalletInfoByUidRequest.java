package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 通过UID查询钱包信息（企业or个人） */
@Builder
public class QueryWalletInfoByUidRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("用户ID")
  private Long userId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/query_wallet_info_by_uid";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletInfoResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(userId != null){
        parameters.put("user_id", userId.toString());
      }
    return parameters;
  }
}
