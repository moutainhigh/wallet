package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 高级钱包用户修改手机认证 */
@Builder
public class SeniorWalletPersonChangeBindPhoneRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("渠道类型 1:浦发银企直连,2:通联云商通")
  private Integer channelType ;

  @ApiModelProperty("身份证号")
  private String idNo ;

  @ApiModelProperty("手机号码")
  private String oldPhone ;

  @ApiModelProperty("姓名")
  private String realName ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/senior_person_change_bind_phone";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return Map.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(channelType != null){
        parameters.put("channel_type", channelType.toString());
      }
      if(idNo != null){
        parameters.put("id_no", idNo.toString());
      }
      if(oldPhone != null){
        parameters.put("old_phone", oldPhone.toString());
      }
      if(realName != null){
        parameters.put("real_name", realName.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
