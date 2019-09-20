package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;

/** 升级高级钱包 */
@Builder
public class UpgradeSeniorWalletRequest extends  AbstractApiRequest {

  @ApiModelProperty("access_token")
  private String accessToken ;

  @ApiModelProperty("业务用户id")
  private String bizUserId ;

  @ApiModelProperty("渠道类型 1:浦发银企直连,2:通联云商通")
  private Integer channelType ;

  @ApiModelProperty("钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户")
  private Integer source ;

  @ApiModelProperty("钱包id")
  private Long walletId ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/upgrade_wallet";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return WalletChannel.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(bizUserId != null){
        parameters.put("biz_user_id", bizUserId.toString());
      }
      if(channelType != null){
        parameters.put("channel_type", channelType.toString());
      }
      if(source != null){
        parameters.put("source", source.toString());
      }
      if(walletId != null){
        parameters.put("wallet_id", walletId.toString());
      }
    return parameters;
  }
}
