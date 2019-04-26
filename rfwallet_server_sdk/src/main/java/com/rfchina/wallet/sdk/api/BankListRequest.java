package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 银行支行列表 */
@Builder
public class BankListRequest extends  AbstractApiRequest {

  @ApiModelProperty("地区编码")
  private String areacode ;

  @ApiModelProperty("银行类型编码")
  private String classCode ;


  @Override
  public String getApiUrl() {
    return "/wallet/bank/bank_list";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListBank.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(areacode != null){
        parameters.put("area＿code", areacode.toString());
      }
      if(classCode != null){
        parameters.put("class_code", classCode.toString());
      }
    return parameters;
  }
}
