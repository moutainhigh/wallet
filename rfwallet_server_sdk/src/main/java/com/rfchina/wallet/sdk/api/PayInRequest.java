package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 初级钱包-思力出钱 */
@Builder
public class PayInRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("支付金额(单位分)")
  private Long amount ;

  @ApiModelProperty("电子凭证号(业务方定义唯一)")
  private String elecChequeNo ;

  @ApiModelProperty("钱包ID")
  private Long walletId ;

  @ApiModelProperty("附言")
  private String note ;

  @ApiModelProperty("支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入")
  private Integer payPurpose ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/junior/sl_pay_in";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PayInResp.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(amount != null){
        parameters.put("amount", amount.toString());
      }
      if(elecChequeNo != null){
        parameters.put("elec_cheque_no", elecChequeNo.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
      if(note != null){
        parameters.put("note", note.toString());
      }
      if(payPurpose != null){
        parameters.put("pay_purpose", payPurpose.toString());
      }
    return parameters;
  }
}
