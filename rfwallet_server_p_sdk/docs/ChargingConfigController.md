###  计费接口

请求地址: /mch/v1/m/charging/calculate

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|amount|是|金额|
|charging_key|是|收费key|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  设置配置

请求地址: /mch/v1/m/charging/config_setting

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|charging_key|是|收费主键|
|charging_value|是|收费值|
|id|是|ID|
|title|是|描述|
|type|是|收费类型,1按次收费，2按比率收费|
|charging_region_end|否|计费区间结束|
|charging_region_start|否|计费区间开始|
|max_fee|否|最大收费|
|min_fee|否|最小收费|
|remark|否|备注|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  查看计费配置

请求地址: /mch/v1/m/charging/config_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|limit|是|需要查询的数量（数量最大50）|
|offset|是|查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|stat|否|是否返回数据总量,false:否, true:是,  默认false|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    charging_key:""  , //收费业务主键
    charging_region_end:""  , //收费区间结束
    charging_region_start:""  , //收费区间开始
    charging_value:""  , //收费值，跟type相关
    create_time:""  , //创建时间
    id:""  , //ID
    last_upd_time:""  , //最后更新时间
    max_fee:""  , //最大收费款项
    min_fee:""  , //最小收费款项
    remark:""  , //备注
    title:""  , //标题说明
    type:""   //收费方式，1按次收费，2按比率收费
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  获取计费配置

请求地址: /mch/v1/m/charging/get_config

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|charging_key|是|收费key|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

