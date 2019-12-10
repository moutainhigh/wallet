package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 银行地区列表 */
@Builder
public class BankAreaListRequest extends  AbstractApiRequest {

  @ApiModelProperty("银行类型编码")
  private String classCode ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/wallet/bank/area_list";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListBankArea.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(classCode != null){
        parameters.put("class_code", classCode.toString());
      }
    return parameters;
  }
}
