###  手续费报表

请求地址: /wallet_server/v1/m/report/charging_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|访问令牌|
|asc|是|升序排序|
|end_time|是|结束时间|
|limit|是|必填，需要查询的数量（数量最大50）|
|offset|是|必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|start_time|是|开始时间|
|stat|是|非必填, false:否, true:是, 是否返回数据总量, 默认false|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    biz_no:""  , //业务凭证号
    biz_time:""  , //业务时间
    event:""  , //事件
    id:""  , //id
    local_tunnel_fee:""  , //本地的通道手续费
    method_name:""  , //方法名
    order_no:""  , //钱包订单号
    third_tunnel_fee:""   //第三方的通道手续费
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
|access_token|是|访问令牌|
|limit|是|必填，需要查询的数量（数量最大50）|
|offset|是|必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|stat|是|非必填, false:否, true:是, 是否返回数据总量, 默认false|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    charging_date:""  , //统计日期
    company_verify_count:""  , //公司验证次数
    company_verify_fee:""  , //公司认证总费用
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    local_pay_fee:""  , //本地的支付手续费,单位分
    local_recharge_fee:""  , //本地的充值手续费,单位分
    person_verify_count:""  , //个人验证次数
    person_verify_fee:""  , //个人认证总费用
    third_pay_fee:""  , //第三方的支付手续费,单位分
    third_recharge_fee:""  , //第三方的充值手续费,单位分
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    withdraw_count:""  , //提现次数
    withdraw_fee:""   //提现总费用
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
|access_token|是|访问令牌|
|end_time|是|结束时间|
|start_time|是|开始时间|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  当月手续费报表

请求地址: /wallet_server/v1/m/report/cur_charging_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|访问令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    charging_date:""  , //统计日期
    company_verify_count:""  , //公司验证次数
    company_verify_fee:""  , //公司认证总费用
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    local_pay_fee:""  , //本地的支付手续费,单位分
    local_recharge_fee:""  , //本地的充值手续费,单位分
    person_verify_count:""  , //个人验证次数
    person_verify_fee:""  , //个人认证总费用
    third_pay_fee:""  , //第三方的支付手续费,单位分
    third_recharge_fee:""  , //第三方的充值手续费,单位分
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    withdraw_count:""  , //提现次数
    withdraw_fee:""   //提现总费用
}  
}
```

###  生成手续费报表

请求地址: /wallet_server/v1/m/report/export_charging_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|访问令牌|
|end_time|是|结束时间|
|file_name|是|文件名称|
|start_time|是|开始时间|
|unique_code|是|唯一码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-导出余额明细

请求地址: /wallet_server/v1/m/report/export_order_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|begin_time|是|交易时间开始|
|end_time|是|交易时间结束|
|file_name|是|文件名称|
|unique_code|是|唯一码|
|wallet_id|是|钱包id|
|status|否|状态|
|trade_type|否|交易类型|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  导出通道明细文件

请求地址: /wallet_server/v1/m/report/export_tunnel_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|访问令牌|
|end_time|是|结束时间|
|export_type|是|导出类型。 1:实名； 2：提现支付单； 3： 手续费|
|file_name|是|文件名称|
|start_time|是|开始时间|
|unique_code|是|唯一码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

