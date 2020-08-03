###  终端管理-地区绑定终端

请求地址: /wallet_server/v1/m/senior/wallet/bind_terminal

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|area_code|是|地区码|
|creator_id|是|创建人id|
|creator_name|是|创建人名称|
|vsp_termid|是|终端号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  终端管理-地区绑定子商户

请求地址: /wallet_server/v1/m/senior/wallet/bind_vsp_cusid

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|app_id|是|通联AppId|
|area_code|是|地区码|
|creator_id|是|创建人id|
|creator_name|是|创建人名称|
|proxy_wallet_id|是|主收款人钱包id|
|vsp_cusid|是|子商户号|
|vsp_merchantid|是|集团商户号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  终端管理-地区商户号列表

请求地址: /wallet_server/v1/m/senior/wallet/query_area

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|limit|是|limit|
|offset|是|offset|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    app_id:""  , //通联appid
    area_code:""  , //地区码
    area_name:""  , //地区名
    create_time:""  , //创建时间
    creator_id:""  , //创建者id
    creator_name:""  , //创建者名称
    id:""  , //id
    proxy_biz_user_id:""  , //主收款会员
    proxy_wallet_id:""  , //主收款钱包id
    update_time:""  , //更新时间
    vsp_cusid:""  , //子商户号
    vsp_merchantid:""   //集团商户号
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  终端管理-终端列表

请求地址: /wallet_server/v1/m/senior/wallet/query_terminal

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|limit|是|limit|
|offset|是|offset|
|area_code|否|地区码|
|proxy_wallet_id|否|主收款钱包id|
|status|否|状态： 0：未绑定，1：已绑定，2：已解绑|
|vsp_cusid|否|子商户号|
|vsp_termid|否|终端号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    app_id:""  , //通联appid
    area_code:""  , //地区码
    area_name:""  , //地区名
    create_time:""  , //创建时间
    creator_id:""  , //创建者id
    creator_name:""  , //创建者名称
    proxy_wallet_id:""  , //主收款钱包id
    status:""  , //状态： 0：未绑定，1：已绑定，2：已解绑
    update_time:""  , //更新时间
    vsp_cusid:""  , //子商户号
    vsp_merchantid:""  , //集团商户号
    vsp_termid:""   //终端号
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

