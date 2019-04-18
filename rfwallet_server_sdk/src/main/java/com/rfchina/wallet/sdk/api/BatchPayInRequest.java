package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 初级钱包-思力批量出钱（最多20笔） */
@Builder
public class BatchPayInRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("json数组，参考思力出钱单笔接口，拼装成数组即可")
  private String jsonArry ;


  @Override
  public String getApiUrl() {
    return "/v1/junior/sl_batch_pay_in";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PubPayRespBody.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(jsonArry != null){
        parameters.put("jsonArry", jsonArry.toString());
      }
    return parameters;
  }
}
