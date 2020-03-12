package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 开通未审核的钱包 */
@Builder
public class CreateMchWalletRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("公司名称")
  private String companyName ;

  @ApiModelProperty("商家ID")
  private String mchId ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;

  @ApiModelProperty("钱包标题，通常是姓名或公司名")
  private String title ;

  @ApiModelProperty("钱包类型， 1：企业钱包，2：个人钱包")
  private Integer type ;

  @ApiModelProperty("公司邮箱")
  private String email ;

  @ApiModelProperty("公司电话")
  private String tel ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/create_mch_wallet";
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
      if(companyName != null){
        parameters.put("company_name", companyName.toString());
      }
      if(mchId != null){
        parameters.put("mch_id", mchId.toString());
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
      if(email != null){
        parameters.put("email", email.toString());
      }
      if(tel != null){
        parameters.put("tel", tel.toString());
      }
    return parameters;
  }
}
