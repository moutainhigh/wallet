###  高级钱包-代付结果查询

请求地址: /wallet_server/v1/m/senior/agent_pay_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|pay_order_no|是|代付单号|

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


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|agent_pay_req|是|代付列表（与代收的分账规则对应），参考AgentPayReq结构体|
|collect_order_no|是|代收单号|

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


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|collect_req|是|代收内容，参考CollectReq结构体|

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
|access_token|是|应用令牌|
|collect_order_no|是|代收单号|

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


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|collect_req|是|代收内容，参考CollectReq结构体|

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
    pay_info:""  , //扫码支付信息/ JS 支付串信息/微信原生 H5 支付串信息
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    payer_wallet_id:""  , //付款人钱包id
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    refund_limit:""  , //可退总额
    start_time:""  , //开始时间
    status:""  , //代收状态。1：待支付 2：已支付 3：交易失败 4：交易关闭（超时或其他）
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    we_chat_a_p_p_info:""   //微信 APP 支付信息
}  
}
```

###  高级钱包-充值

请求地址: /wallet_server/v1/m/senior/recharge

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|recharge_req|是|充值内容，参考RechargeReq结构体|

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
|access_token|是|应用令牌|
|refund_order_no|是|退款单号|

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


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|collect_order_no|是|代收单号|
|refund_list|是|退款清单，参考List&lt;RefundInfo&gt;结构体|

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

