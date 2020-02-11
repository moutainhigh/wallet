###  高级钱包-代付

请求地址: /wallet_server/v1/m/senior/pay/agent_pay

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|agent_pay_req|是|代付列表（与代收的分账规则对应），参考AgentPayReq.Reciever结构体|
|biz_no|是|业务方单号|
|collect_order_no|是|原代收单号|
|note|否|备注|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    clearing:{
    agent_wallet_id:""  , //中间账户钱包id
    amount:""  , //金额
    collect_info_id:""  , //分帐id
    collect_order_no:""  , //代收单号
    create_time:""  , //创建日期
    id:""  , //id
    order_id:""   //订单id
}  , 
    order:{
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""   //钱包id
}   
}  
}
```

###  高级钱包-对账文件

请求地址: /wallet_server/v1/m/senior/pay/balance_file

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|balance_date|是|对账日期 yyyy-MM-dd|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance_date:""  , //钱包对账日期
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    status:""  , //状态 1：进行中 2：对账完成 3:对账失败
    wallet_file_url:""   //对账文件
}  
}
```

###  高级钱包-即刻代收

请求地址: /wallet_server/v1/m/senior/pay/collect_sync

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|collect_req|是|代收内容，参考CollectReq结构体|
|customer_ip|否|客户Ip|
|jump_url|否|跳转地址|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_info:""  , //扫码支付信息/ JS 支付串信息/微信原生 H5 支付串信息
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    signed_params:""  , //密码确认时输入参数
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    ticket:""  , //业务票据
    trade_no:""  , //交易编号
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""  , //钱包id
    we_chat_a_p_p_info:""   //微信 APP 支付信息
}  
}
```

###  高级钱包-代扣

请求地址: /wallet_server/v1/m/senior/pay/deduction

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|deduction_req|是|消费内容，参考DeductionReq结构体|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_info:""  , //扫码支付信息/ JS 支付串信息/微信原生 H5 支付串信息
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    signed_params:""  , //密码确认时输入参数
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    ticket:""  , //业务票据
    trade_no:""  , //交易编号
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""  , //钱包id
    we_chat_a_p_p_info:""   //微信 APP 支付信息
}  
}
```

###  高级钱包-订单结果查询

请求地址: /wallet_server/v1/m/senior/pay/order_query

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|order_no|是|统一订单号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包-充值

请求地址: /wallet_server/v1/m/senior/pay/recharge

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|amount|是|金额|
|card_id|是|银行卡id|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    extend_info:""  , //扩展参数
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    password_confirm:""  , //密码验证
    pay_info:""  , //扫码支付信息/ JS支付串信息/ 微信原生H5支付串信息
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    sms_confirm:""  , //短信验证
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    ticket:""  , //业务票据
    trade_no:""  , //交易编号
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""  , //钱包id
    we_chat_a_p_p_info:""   //微信 APP 支付信息
}  
}
```

###  高级钱包-退款

请求地址: /wallet_server/v1/m/senior/pay/refund

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|biz_no|是|业务方单号|
|collect_order_no|是|代收单号|
|refund_list|是|退款清单，参考List&lt;RefundInfo&gt;结构体|
|note|否|备注|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包-短信确认

请求地址: /wallet_server/v1/m/senior/pay/sms_confirm

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|customer_ip|是|客户ip|
|ticket|是|业务令牌|
|verify_code|是|短信验证码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-重发短信

请求地址: /wallet_server/v1/m/senior/pay/sms_retry

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|ticket|是|业务令牌|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-提现

请求地址: /wallet_server/v1/m/senior/pay/withdraw

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|amount|是|金额|
|card_id|是|银行卡id|
|customer_ip|是|客户Ip|
|wallet_id|是|钱包id|
|jump_url|否|跳转地址|
|validate_type|否|交易验证方式 0：无验证 1：短信 2：密码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    amount:""  , //金额
    batch_no:""  , //钱包批次号
    biz_no:""  , //业务凭证号
    biz_tag:""  , //业务标识。1: 有退款 2: 已记流水
    biz_user_id:""  , //商户系统用户标识
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    progress:""  , //进度。1：待发送 2：已发送 3：已接收结果
    remark:""  , //备注
    signed_params:""  , //密码确认时输入参数
    source_app_id:""  , //来源APPID
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败，5：交易关闭（超时或其他）
    sub_status:""  , //0：默认 1:待人工处理 2:等待重新发起
    ticket:""  , //业务票据
    tunnel_err_code:""  , //通道错误码
    tunnel_err_msg:""  , //系统错误信息
    tunnel_fee:""  , //通道手续费
    tunnel_order_no:""  , //渠道订单号
    tunnel_status:""  , //通道状态
    tunnel_succ_time:""  , //通道成功时间
    tunnel_type:""  , //通道类型。1: 浦发银企直连，2：通联云商通
    type:""  , //类型，1：财务结算，2：充值，3：提现，4：代收，5：代付，6：退款，7：消费, 8：代扣,
    user_err_msg:""  , //用户端错误提示
    wallet_id:""   //钱包id
}  
}
```

