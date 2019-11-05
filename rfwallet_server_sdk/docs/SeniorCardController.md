###  高级钱包-确认绑定银行卡

请求地址: /wallet_server/v1/m/senior/card/confirm_bind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|
|pre_bind_ticket|否|预绑卡票据|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-预绑定银行卡

请求地址: /wallet_server/v1/m/senior/card/pre_bind_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|identity_no|是|身份证|
|phone|是|银行预留手机号|
|real_name|是|姓名|
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

###  高级钱包-解绑银行卡

请求地址: /wallet_server/v1/m/senior/card/unbind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_id|是|银行卡id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

