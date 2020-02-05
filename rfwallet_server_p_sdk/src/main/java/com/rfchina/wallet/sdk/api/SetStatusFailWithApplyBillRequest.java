package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 设置出款申请单状态为失败 */
@Builder
public class SetStatusFailWithApplyBillRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("备注")
  private String auditComment ;

  @ApiModelProperty("设置人")
  private String auditUser ;

  @ApiModelProperty("设置人ID")
  private String auditUserId ;

  @ApiModelProperty("批次号")
  private String batchNo ;

  @ApiModelProperty("业务单号")
  private String bizNo ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/apply_bill/set_status_fail";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return Wallet.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(auditComment != null){
        parameters.put("audit_comment", auditComment.toString());
      }
      if(auditUser != null){
        parameters.put("audit_user", auditUser.toString());
      }
      if(auditUserId != null){
        parameters.put("audit_user_id", auditUserId.toString());
      }
      if(batchNo != null){
        parameters.put("batch_no", batchNo.toString());
      }
      if(bizNo != null){
        parameters.put("biz_no", bizNo.toString());
      }
    return parameters;
  }
}
