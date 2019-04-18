package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 初级钱包-查询支付状态 */
@Builder
public class QueryRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("受理编号")
  private String acceptNo ;

  @ApiModelProperty("电子凭证号(业务方定义唯一)")
  private String elecChequeNo ;


  @Override
  public String getApiUrl() {
    return "/v1/junior/sl_query";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return ListPayStatusResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(acceptNo != null){
        parameters.put("accept_no", acceptNo.toString());
      }
      if(elecChequeNo != null){
        parameters.put("elec_cheque_no", elecChequeNo.toString());
      }
    return parameters;
  }
}
