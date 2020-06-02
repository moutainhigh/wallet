###  对公账户-申请钱包

请求地址: /mch/v1/m/account/apply_wallet

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|public_id|是|对公账户ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    wallet_id:""   //钱包id
}  
}
```

