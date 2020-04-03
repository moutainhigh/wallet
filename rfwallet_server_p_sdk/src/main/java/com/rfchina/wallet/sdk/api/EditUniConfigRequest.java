package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 钱包配置-更新全局配置 */
@Builder
public class EditUniConfigRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("企业手动提现最低金额")
  private Long manualWithdrawCompanyMin ;

  @ApiModelProperty("个人手动提现最低金额")
  private Long manualWithdrawPersonMin ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/update_uni_config";
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
      if(manualWithdrawCompanyMin != null){
        parameters.put("manual_withdraw_company_min", manualWithdrawCompanyMin.toString());
      }
      if(manualWithdrawPersonMin != null){
        parameters.put("manual_withdraw_person_min", manualWithdrawPersonMin.toString());
      }
    return parameters;
  }
}
