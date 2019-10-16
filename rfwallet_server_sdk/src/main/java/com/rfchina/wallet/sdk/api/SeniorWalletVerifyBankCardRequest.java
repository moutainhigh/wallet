package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包验证银行卡 */
@Builder
public class SeniorWalletVerifyBankCardRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("银行卡号")
  private String cardNo ;

  @ApiModelProperty("身份证")
  private String identityNo ;

  @ApiModelProperty("银行预留手机号")
  private String phone ;

  @ApiModelProperty("姓名")
  private String realName ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;

  @ApiModelProperty("钱包id")
  private Long walletId ;

  @ApiModelProperty("信用卡cvv2码")
  private String cvv2 ;

  @ApiModelProperty("信用卡到期4位日期")
  private String validate ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/senior_verify_bank_card";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return YunstApplyBindBankCardResult.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(cardNo != null){
        parameters.put("card_no", cardNo.toString());
      }
      if(identityNo != null){
        parameters.put("identity_no", identityNo.toString());
      }
      if(phone != null){
        parameters.put("phone", phone.toString());
      }
      if(realName != null){
        parameters.put("real_name", realName.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(cvv2 != null){
        parameters.put("cvv2", cvv2.toString());
      }
      if(validate != null){
        parameters.put("validate", validate.toString());
      }
    return parameters;
  }
}
