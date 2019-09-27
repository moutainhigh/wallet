package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 开通未审核的钱包 */
@Builder
public class CreateWalletRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;

  @ApiModelProperty("钱包标题，通常是姓名或公司名")
  private String title ;

  @ApiModelProperty("钱包类型， 1：企业钱包，2：个人钱包")
  private Integer type ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/create_wallet";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return Wallet.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
      if(title != null){
        parameters.put("title", title.toString());
      }
      if(type != null){
        parameters.put("type", type.toString());
      }
    return parameters;
  }
}
