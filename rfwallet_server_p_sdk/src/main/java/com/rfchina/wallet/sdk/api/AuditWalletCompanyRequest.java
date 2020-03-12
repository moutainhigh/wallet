package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 富慧通审核企业商家钱包 */
@Builder
public class AuditWalletCompanyRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("审核方式，1：运营，2：银企直连，4：通联")
  private Long auditType ;

  @ApiModelProperty("公司名称")
  private String companyName ;

  @ApiModelProperty("钱包状态: 1:待审核，2：激活,3：禁用")
  private Integer status ;

  @ApiModelProperty("钱包ID")
  private Long walletId ;

  @ApiModelProperty("公司邮箱")
  private String email ;

  @ApiModelProperty("公司电话")
  private String phone ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/audit_wallet_company";
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
      if(auditType != null){
        parameters.put("audit_type", auditType.toString());
      }
      if(companyName != null){
        parameters.put("company_name", companyName.toString());
      }
      if(status != null){
        parameters.put("status", status.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(email != null){
        parameters.put("email", email.toString());
      }
      if(phone != null){
        parameters.put("phone", phone.toString());
      }
    return parameters;
  }
}
