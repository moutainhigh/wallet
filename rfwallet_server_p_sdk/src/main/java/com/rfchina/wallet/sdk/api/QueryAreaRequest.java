package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 终端管理-地区商户号列表 */
@Builder
public class QueryAreaRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("limit")
  private Integer limit ;

  @ApiModelProperty("offset")
  private Integer offset ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/query_area";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWalletArea.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
    return parameters;
  }
}
