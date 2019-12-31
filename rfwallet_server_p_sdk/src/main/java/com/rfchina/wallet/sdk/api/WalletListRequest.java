package com.rfchina.wallet.sdk.api;

import java.util.HashMap;
import java.util.Map;
import com.rfchina.wallet.sdk.model.*;
import com.rfchina.platform.sdk2.request.AbstractApiRequest;
import lombok.Builder;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/** 钱包列表 */
@Builder
public class WalletListRequest extends  AbstractApiRequest {

  @ApiModelProperty("访问令牌")
  private String accessToken ;

  @ApiModelProperty("必填，需要查询的数量（数量最大50）")
  private Integer limit ;

  @ApiModelProperty("必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。")
  private Integer offset ;

  @ApiModelProperty("非必填, false:否, true:是, 是否返回数据总量, 默认false")
  private Boolean stat ;

  @ApiModelProperty("钱包状态: 1:待审核，2：激活,3：禁用")
  private Integer status ;

  @ApiModelProperty("钱包名字")
  private String title ;

  @ApiModelProperty("钱包类型， 1：企业钱包，2：个人钱包")
  private Integer type ;

  @ApiModelProperty("钱包等级 1： 初级钱包，2： 高级钱包")
  private Integer walletLevel ;


  @Override
  public String getApiUrl() {
    return "/wallet_server/v1/m/wallet/wallet_list";
  }

  @Override
  public Class<?> getResponseModelClass() {
    return PaginationWallet.class;
  }

  @Override
  public Map<String, String> getTextParmas() {
    Map<String, String> parameters = new HashMap<>(2);
      if(accessToken != null){
        parameters.put("access_token", accessToken.toString());
      }
      if(limit != null){
        parameters.put("limit", limit.toString());
      }
      if(offset != null){
        parameters.put("offset", offset.toString());
      }
      if(stat != null){
        parameters.put("stat", stat.toString());
      }
      if(status != null){
        parameters.put("status", status.toString());
      }
      if(title != null){
        parameters.put("title", title.toString());
      }
      if(type != null){
        parameters.put("type", type.toString());
      }
      if(walletLevel != null){
        parameters.put("wallet_level", walletLevel.toString());
      }
    return parameters;
  }
}
