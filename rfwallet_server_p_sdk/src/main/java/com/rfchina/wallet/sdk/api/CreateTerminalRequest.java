package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 高级钱包-新增终端 */
@Builder
public class CreateTerminalRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("商家id")
  private String mchId ;

  @ApiModelProperty("商家名称")
  private String mchName ;

  @ApiModelProperty("省份")
  private String province ;

  @ApiModelProperty("门店地址")
  private String shopAddress ;

  @ApiModelProperty("通联appId")
  private String vspCusid ;

  @ApiModelProperty("子商户号")
  private String vspCusid2 ;

  @ApiModelProperty("集团商户号")
  private String vspMerchantid ;

  @ApiModelProperty("终端号")
  private String vspTermid ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/senior/wallet/create_terminal";
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
      if(mchId != null){
        parameters.put("mch_id", mchId.toString());
      }
      if(mchName != null){
        parameters.put("mch_name", mchName.toString());
      }
      if(province != null){
        parameters.put("province", province.toString());
      }
      if(shopAddress != null){
        parameters.put("shop_address", shopAddress.toString());
      }
      if(vspCusid != null){
        parameters.put("vsp_cusid", vspCusid.toString());
      }
      if(vspCusid2 != null){
        parameters.put("vsp_cusid", vspCusid2.toString());
      }
      if(vspMerchantid != null){
        parameters.put("vsp_merchantid", vspMerchantid.toString());
      }
      if(vspTermid != null){
        parameters.put("vsp_termid", vspTermid.toString());
      }
    return parameters;
  }
}
