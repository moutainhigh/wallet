package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 银行类别列表 */
@Builder
public class BankClassListRequest extends  AbstractApiRequest {


  @Override
  public String getApiUrl() {
    return "/wallet/bank/class_list";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListBankClass.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
    return parameters;
  }
}
