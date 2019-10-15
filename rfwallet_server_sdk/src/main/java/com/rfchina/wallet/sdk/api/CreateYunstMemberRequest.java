package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 云商通-创建会员 */
@Builder
public class CreateYunstMemberRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("业务用户id")
  private String bizUserId ;

  @ApiModelProperty("业务用户类型")
  private Integer type ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/yunst/create_member";
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
      if(bizUserId != null){
        parameters.put("biz_user_id", bizUserId.toString());
      }
      if(type != null){
        parameters.put("type", type.toString());
      }
    return parameters;
  }
}
