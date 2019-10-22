###  高级钱包-代付结果查询

请求地址: /wallet_server/v1/m/senior/agent_pay_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|pay_order_no|是|代付单号|
|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //总金额
    apply_id:""  , //工单id
    collect_id:""  , //代收单id
    collect_info_id:""  , //分帐id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    payee_wallet_id:""  , //收款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    start_time:""  , //开始时间
    status:""  , //清算状态。1：未清算 2：已清算  3:交易失败
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

###  高级钱包-代付

请求地址: /wallet_server/v1/m/senior/agent_pay

请求类型: POST

请求参数:

```
    结构体：SettleReq   {
    receivers: [ {
    amount:""  , //金额,单位:分
    fee_amount:""  , //代付接口手续费（代收无效）,单位:分
    wallet_id:""   //收款人钱包ID
}  ]   
}  ,
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|collect_order_no|是|代收单号|

|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    clearings: [ {
    amount:""  , //总金额
    apply_id:""  , //工单id
    collect_id:""  , //代收单id
    collect_info_id:""  , //分帐id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    payee_wallet_id:""  , //收款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    start_time:""  , //开始时间
    status:""  , //清算状态。1：未清算 2：已清算  3:交易失败
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  ]  , 
    collect:{
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //金额
    apply_id:""  , //工单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    refund_limit:""  , //可退总额
    start_time:""  , //开始时间
    status:""  , //代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}   
}  
}
```

###  高级钱包-定时代收

请求地址: /wallet_server/v1/m/senior/collect_async

请求类型: POST

请求参数:

```
    结构体：CollectReq   {
    amount:""  , //支付金额(单位分)
    biz_no:""  , //业务凭证号(业务方定义唯一,最长32字节)
    expire_time:""  , //订单过期时间,订单最长时效为 24 小时
    fee:""  , //手续费，单位:分。如果不存在,则填 0。
    industry_code:""  , //行业代码（由渠道分配）
    industry_name:""  , //行业名称（由渠道分配）
    note:""  , //附言
    payer_wallet_id:""  , //钱包用户登陆态ID
    recievers: [ {
    amount:""  , //金额,单位:分
    fee_amount:""  , //代付接口手续费（代收无效）,单位:分
    wallet_id:""   //收款人钱包ID
}  ]  , 
    validate_type:""  , //交易验证方式 1：短信 2：密码
    wallet_pay_method:{
    alipay:{
    amount:""  , //渠道出资额(单位分)
    pay_type:""  , //31：支付宝扫码支付(正扫) 32：支付宝JS支付(生活号) 33：支付宝原生
    user_id:""   //支付宝JS支付user_id
}  , 
    balance:{
    amount:""   //渠道出资额(单位分)
}  , 
    code_pay:{
    amount:""  , //渠道出资额(单位分)
    authcode:""  , //支付授权码，支付宝被扫刷卡支付时,用户的付款二维码
    pay_type:""   //41：收银宝刷卡支付（被扫）
}  , 
    methods:""  , //
    wechat:{
    amount:""  , //渠道出资额(单位分)
    cusip:""  , //用户下单及调起支付的终端IP
    open_id:""  , //微信JS支付openid
    pay_type:""  , //21:微信小程序支付 22:微信原生APP支付 23:微信原生H5支付 24:微信JS支付(公众号) 25:微信扫码支付(正扫)
    scene_info:""  , //场景信息（H5支付）
    sub_app_id:""   //微信端应用ID:appid（H5支付）
}   
}   
}  ,
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |

|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //金额
    apply_id:""  , //工单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    refund_limit:""  , //可退总额
    start_time:""  , //开始时间
    status:""  , //代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

###  高级钱包-代收结果查询

请求地址: /wallet_server/v1/m/senior/collect_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|collect_order_no|是|代收单号|
|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //金额
    apply_id:""  , //工单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    refund_limit:""  , //可退总额
    start_time:""  , //开始时间
    status:""  , //代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

###  高级钱包-即刻代收

请求地址: /wallet_server/v1/m/senior/collect_sync

请求类型: POST

请求参数:

```
    结构体：CollectReq   {
    amount:""  , //支付金额(单位分)
    biz_no:""  , //业务凭证号(业务方定义唯一,最长32字节)
    expire_time:""  , //订单过期时间,订单最长时效为 24 小时
    fee:""  , //手续费，单位:分。如果不存在,则填 0。
    industry_code:""  , //行业代码（由渠道分配）
    industry_name:""  , //行业名称（由渠道分配）
    note:""  , //附言
    payer_wallet_id:""  , //钱包用户登陆态ID
    recievers: [ {
    amount:""  , //金额,单位:分
    fee_amount:""  , //代付接口手续费（代收无效）,单位:分
    wallet_id:""   //收款人钱包ID
}  ]  , 
    validate_type:""  , //交易验证方式 1：短信 2：密码
    wallet_pay_method:{
    alipay:{
    amount:""  , //渠道出资额(单位分)
    pay_type:""  , //31：支付宝扫码支付(正扫) 32：支付宝JS支付(生活号) 33：支付宝原生
    user_id:""   //支付宝JS支付user_id
}  , 
    balance:{
    amount:""   //渠道出资额(单位分)
}  , 
    code_pay:{
    amount:""  , //渠道出资额(单位分)
    authcode:""  , //支付授权码，支付宝被扫刷卡支付时,用户的付款二维码
    pay_type:""   //41：收银宝刷卡支付（被扫）
}  , 
    methods:""  , //
    wechat:{
    amount:""  , //渠道出资额(单位分)
    cusip:""  , //用户下单及调起支付的终端IP
    open_id:""  , //微信JS支付openid
    pay_type:""  , //21:微信小程序支付 22:微信原生APP支付 23:微信原生H5支付 24:微信JS支付(公众号) 25:微信扫码支付(正扫)
    scene_info:""  , //场景信息（H5支付）
    sub_app_id:""   //微信端应用ID:appid（H5支付）
}   
}   
}  ,
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |

|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //金额
    apply_id:""  , //工单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    refund_limit:""  , //可退总额
    start_time:""  , //开始时间
    status:""  , //代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

###  高级钱包-充值

请求地址: /wallet_server/v1/m/senior/recharge

请求类型: POST

请求参数:

```
    结构体：RechargeReq   {
    amount:""  , //支付金额(单位分)
    biz_no:""  , //业务凭证号(业务方定义唯一,最长32字节)
    expire_time:""  , //订单过期时间,订单最长时效为 24 小时
    fee:""  , //手续费，单位:分。如果不存在,则填 0。
    industry_code:""  , //行业代码（由渠道分配）
    industry_name:""  , //行业名称（由渠道分配）
    payer_wallet_id:""  , //钱包用户登陆态ID
    validate_type:""  , //交易验证方式 1：短信 2：密码
    wallet_pay_method:{
    alipay:{
    amount:""  , //渠道出资额(单位分)
    pay_type:""  , //31：支付宝扫码支付(正扫) 32：支付宝JS支付(生活号) 33：支付宝原生
    user_id:""   //支付宝JS支付user_id
}  , 
    balance:{
    amount:""   //渠道出资额(单位分)
}  , 
    code_pay:{
    amount:""  , //渠道出资额(单位分)
    authcode:""  , //支付授权码，支付宝被扫刷卡支付时,用户的付款二维码
    pay_type:""   //41：收银宝刷卡支付（被扫）
}  , 
    methods:""  , //
    wechat:{
    amount:""  , //渠道出资额(单位分)
    cusip:""  , //用户下单及调起支付的终端IP
    open_id:""  , //微信JS支付openid
    pay_type:""  , //21:微信小程序支付 22:微信原生APP支付 23:微信原生H5支付 24:微信JS支付(公众号) 25:微信扫码支付(正扫)
    scene_info:""  , //场景信息（H5支付）
    sub_app_id:""   //微信端应用ID:appid（H5支付）
}   
}   
}  ,
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |

|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-退款结果查询

请求地址: /wallet_server/v1/m/senior/refund_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|refund_order_no|是|退款单号|
|access_token|否|应用令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //退款金额
    apply_id:""  , //工单id
    collect_amount:""  , //原代收金额
    collect_id:""  , //代收单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    start_time:""  , //开始时间
    status:""  , //退款状态。1：未退款 2：已退款 3:交易失败
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

###  高级钱包-退款

请求地址: /wallet_server/v1/m/senior/refund

请求类型: POST

请求参数:

```
    结构体：List&lt;RefundInfo&gt;  [ {
    amount:""  , //金额,单位:分
    wallet_id:""   //钱包ID
} ] 
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|collect_order_no|是|代收单号|
|access_token|否|应用令牌|


返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //退款金额
    apply_id:""  , //工单id
    collect_amount:""  , //原代收金额
    collect_id:""  , //代收单id
    create_time:""  , //创建日期
    end_time:""  , //结束时间
    err_code:""  , //通道错误码
    err_msg:""  , //系统错误信息
    expire_time:""  , //过期时间
    id:""  , //id
    order_no:""  , //订单号
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    start_time:""  , //开始时间
    status:""  , //退款状态。1：未退款 2：已退款 3:交易失败
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""   //通道类型。1: 浦发银企直连，2：通联云商通
}  
}
```

