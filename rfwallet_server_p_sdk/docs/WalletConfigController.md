###  钱包配置-更新全局配置

请求地址: /wallet_server/v1/m/senior/wallet/update_uni_config

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|manual_withdraw_company_min|否|企业手动提现最低金额|
|manual_withdraw_person_min|否|个人手动提现最低金额|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  钱包配置-获取全局配置

请求地址: /wallet_server/v1/m/senior/wallet/get_uni_config

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    audit_user_id:""  , //审批用户id
    audit_user_name:""  , //审批用户名称
    audit_user_role:""  , //审批用户角色
    auto_withdraw_status:""  , //自动提现状态。1：开启，2：关闭
    auto_withdraw_threshold:""  , //自动提现金额
    auto_withdraw_type:""  , //自动提现类型。1：按余额提现 2：按支付单提现
    create_time:""  , //创建时间
    id:""  , //id
    manual_withdraw_company_min:""  , //企业手动提现最低金额
    manual_withdraw_person_min:""  , //个人手动提现最低金额
    status:""  , //配置状态。1：正常，2：失效
    wallet_id:""   //钱包ID
}  
}
```

