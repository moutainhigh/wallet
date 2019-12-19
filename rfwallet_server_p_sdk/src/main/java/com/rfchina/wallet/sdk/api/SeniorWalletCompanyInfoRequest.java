package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-企业信息 */
@Builder
public class SeniorWalletCompanyInfoRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("手动更新开关")
  private Boolean isManualRefresh ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("新对公账号")
  private String publicAccountNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/company_info";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return CompanyInfoResult.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(isManualRefresh != null){
        parameters.put("is_manual_refresh", isManualRefresh.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(publicAccountNo != null){
        parameters.put("public_account_no", publicAccountNo.toString());
      }
    return parameters;
  }
}
