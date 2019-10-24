###  高级钱包-确认绑定银行卡

请求地址: /wallet_server/v1/m/senior/confirm_bind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|phone|是|银行预留手机号|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|trans_date|是|验证银行卡申请时间|
|trans_num|是|验证银行卡流水号|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|
|cvv2|否|信用卡cvv2码|
|validate|否|信用卡到期4位日期|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-预绑定银行卡

请求地址: /wallet_server/v1/m/senior/pre_bind_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|identity_no|是|身份证|
|phone|是|银行预留手机号|
|real_name|是|姓名|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|
|cvv2|否|信用卡cvv2码|
|validate|否|信用卡到期4位日期|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    bank_code:""  , //银行代码
    bank_name:""  , //银行名称
    biz_user_id:""  , //商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)
    card_type:""  , //银行卡类型 1-储蓄卡 2-信用卡
    trance_num:""  , //流水号
    trans_date:""   //申请时间 YYYYMMDD
}  
}
```

###  高级钱包-解绑银行卡

请求地址: /wallet_server/v1/m/senior/unbind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|source|是|来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

