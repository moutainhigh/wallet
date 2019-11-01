package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-修改手机 */
@Builder
public class ResetSecurityTelRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("前端回跳地址")
  private String jumpUrl ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/reset_security_tel";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PageVo.class;
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
      if(jumpUrl != null){
        parameters.put("jump_url", jumpUrl.toString());
      }
    return parameters;
  }
}
