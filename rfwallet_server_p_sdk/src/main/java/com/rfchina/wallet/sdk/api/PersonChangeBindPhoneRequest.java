package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-个人用户修改手机 */
@Builder
public class PersonChangeBindPhoneRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("身份证号")
  private String idNo ;

  @ApiModelProperty("手机号码")
  private String oldPhone ;

  @ApiModelProperty("姓名")
  private String realName ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("前端回跳地址")
  private String jumpUrl ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/senior_person_change_bind_phone";
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
      if(idNo != null){
        parameters.put("id_no", idNo.toString());
      }
      if(oldPhone != null){
        parameters.put("old_phone", oldPhone.toString());
      }
      if(realName != null){
        parameters.put("real_name", realName.toString());
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
