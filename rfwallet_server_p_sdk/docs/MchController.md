###  显示图片

请求地址: /mch/v1/m/mch/browse_picture

请求类型: GET

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌(支付中心自带)|
|url|是|url|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  营销账号详情-查询积分有效期

请求地址: /mch/v1/ip/account/get_coin_expire_time

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|outlay_account_id|是|营销账号ID 或 专项营销账户ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  获取商家所有状态的收款账号

请求地址: /mch/v1/account/account_list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|mch_id|是|mch_id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  获取收款账号对应结算配置

请求地址: /mch/v1/ip/account/settle/cfg

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|account_id|是|收款帐号id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    account_id:""  , //账号ID
    create_time:""  , //创建日期
    id:""  , //ID
    last_upd_time:""  , //最后更新时间
    mch_id:""  , //商户ID
    settle_account:""  , //商户结算账号ID或钱包账号
    settle_type:""  , //商户结算账号类型, 1: 银行账号, 2: 微众钱包账号，3：富力钱包
    status:""   //状态, 1:启用, 2:禁用
}  
}
```

###  商家管理-查询商家信息

请求地址: /mch/v1/ip/account/list_coin_expire_time

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|limit|是|需要查询的数量（数量最大50）|
|offset|是|查询起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|account_name|否|账户名称|
|mch_id|否|商家ID|
|mch_name|否|商家名称|
|outlay_account_id|否|营销账号ID 或 专项营销账户ID|
|stat|否|非必填, false:否, true:是, 是否返回数据总量, 默认false|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  查询商户银行卡结算账号

请求地址: /mch/v1/ip/settle/account/query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|limit|是|需要查询的数量（数量最大50）|
|offset|是|查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|mch_ids|否|商家id，非必填, 多个以,号分隔|
|stat|否|是否返回数据总量,false:否, true:是,  默认false|
|status|否|状态，非必填, 1:启用, 2:禁用，支持多个，用,号隔开|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    bank_account:""  , //银行账号
    bank_name:""  , //银行名
    create_time:""  , //创建日期
    deposit_bank:""  , //开户行
    deposit_name:""  , //开户名
    id:""  , //ID
    mch_id:""  , //商家ID
    status:""   //状态, 1:激活, 2:失效, 3:删除
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  营销账号详情-设置积分有效期

请求地址: /mch/v1/ip/account/set_coin_expire_time

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|expire_time|是|过期时间,天|
|outlay_account_id|是|营销账号ID 或 专项营销账户ID|
|effective_time|否|生效时间, 格式: yyyy-MM-dd HH:mm:ss|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  商家-设置钱包数量限制

请求地址: /mch/v1/m/mch/set_wallet_limit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|mch_id|是|mch_id|
|wallet_limit|是|wallet_limit|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  上传商家图片

请求地址: /mch/v1/m/mch/upload_picture

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌(支付中心自带)|
|ext|是|图片扩展名|
|pic_data|是|pic_data|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

