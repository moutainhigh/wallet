package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-退款 */
@Builder
public class RefundRequest extends  AbstractApiRequest {

  @ApiModelProperty("应用令牌")
  private String accessToken ;

  @ApiModelProperty("业务方单号")
  private String bizNo ;

  @ApiModelProperty("代收单号")
  private String collectOrderNo ;

  @ApiModelProperty("退款清单，参考List<RefundInfo>结构体")
  private String refundList ;

  @ApiModelProperty("备注")
  private String note ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/pay/refund";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletOrder.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(bizNo != null){
        parameters.put("biz_no", bizNo.toString());
      }
      if(collectOrderNo != null){
        parameters.put("collect_order_no", collectOrderNo.toString());
      }
      if(refundList != null){
        parameters.put("refund_list", refundList.toString());
      }
      if(note != null){
        parameters.put("note", note.toString());
      }
    return parameters;
  }
}
