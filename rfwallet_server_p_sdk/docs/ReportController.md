###  手续费报表

请求地址: /wallet_server/v1/m/report/charging_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|end_time|是|end_time|
|limit|是|limit|
|offset|是|offset|
|start_time|是|start_time|
|stat|是|stat|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    biz_no:""  , //业务凭证号
    biz_time:""  , //业务时间
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    local_tunnel_fee:""  , //本地的通道手续费
    method_name:""  , //方法名
    order_no:""  , //钱包订单号
    service_name:""  , //服务名
    third_tunnel_fee:""  , //第三方的通道手续费
    tunnel_count:""  , //通道次数
    tunnel_type:""   //渠道类型。1: 浦发银企直连，2：通联云商通
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  手续费报表

请求地址: /wallet_server/v1/m/report/charging_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|limit|是|limit|
|offset|是|offset|
|stat|是|stat|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    charging_date:""  , //统计日期
    company_verify_count:""  , //公司验证次数
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    local_pay_fee:""  , //本地的支付手续费,单位分
    local_recharge_fee:""  , //本地的充值手续费,单位分
    person_verify_count:""  , //个人验证次数
    third_pay_fee:""  , //第三方的支付手续费,单位分
    third_recharge_fee:""  , //第三方的充值手续费,单位分
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    withdraw_count:""   //提现次数
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  手续费重做

请求地址: /wallet_server/v1/m/report/charging_redo

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|end_time|是|end_time|
|start_time|是|start_time|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

