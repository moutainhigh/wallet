package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-商家资料审核（通道） */
@Builder
public class SeniorWalletCompanyInfoAuditRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("审核方式")
  private Integer auditType ;

  @ApiModelProperty("渠道类型 1:浦发银企直连,2:通联云商通")
  private Integer channelType ;

  @ApiModelProperty("企业信息(json)")
  private String companyBasicInfo ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/company_info_audit";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletTunnel.class;
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
      if(channelType != null){
        parameters.put("channel_type", channelType.toString());
      }
      if(companyBasicInfo != null){
        parameters.put("company_basic_info", companyBasicInfo.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
