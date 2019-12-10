package com.rfchina.wallet.sdk.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;

    
/**
* PersonInfoResult
*/
@Data
public class PersonInfoResult  {
    @ApiModelProperty("地址")
    private String address ;

    @ApiModelProperty("县市")
    private String area ;

    @ApiModelProperty("国家")
    private String country ;

    @ApiModelProperty("身份证号码")
    private String identityCardNo ;

    @ApiModelProperty("是否进行实名认证")
    private String isIdentityChecked ;

    @ApiModelProperty("是否绑定手机")
    private Boolean isPhoneChecked ;

    @ApiModelProperty("是否已设置支付密码")
    private String isSetPayPwd ;

    @ApiModelProperty("是否已签电子协议")
    private String isSignContract ;

    @ApiModelProperty("姓名")
    private String name ;

    @ApiModelProperty("支付失败次数")
    private String payFailAmount ;

    @ApiModelProperty("手机号码")
    private String phone ;

    @ApiModelProperty("省份")
    private String province ;

    @ApiModelProperty("实名认证时间")
    private String realNameTime ;

    @ApiModelProperty("创建ip")
    private String registerIp ;

    @ApiModelProperty("创建时间")
    private String registerTime ;

    @ApiModelProperty("备注")
    private String remark ;

    @ApiModelProperty("访问终端类型")
    private Long source ;

    @ApiModelProperty("云商通用户id")
    private String userId ;

    @ApiModelProperty("用户状态")
    private Long userState ;


}

