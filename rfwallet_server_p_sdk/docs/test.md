###  初级钱包-思力批量出钱（最多20笔）

请求地址: /wallet_server/v1/m/junior/sl_batch_pay_in

请求类型: POST

请求参数:

```
    结构体：String 
```

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|


返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    batch_no:""   //钱包批次号
}  
}
```

###  初级钱包-思力出钱

请求地址: /wallet_server/v1/m/junior/sl_pay_in

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|amount|是|支付金额(单位分)|
|biz_no|是|业务凭证号(业务方定义唯一，最长32字)|
|wallet_id|是|钱包ID|
|note|否|附言|
|pay_purpose|否|支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    batch_no:""   //钱包批次号
}  
}
```

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
    create_time:""  , //创建日期
    deleted:""  , //是否删除 0：正常 1：已删除
    event:""  , //事件
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

###  高级钱包-银行卡列表

请求地址: /wallet_server/v1/m/senior/card/list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  高级钱包-确认绑定银行卡

请求地址: /wallet_server/v1/m/senior/card/confirm_bind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|pre_bind_ticket|是|预绑卡票据|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-预绑定银行卡

请求地址: /wallet_server/v1/m/senior/card/pre_bind_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|identity_no|是|身份证|
|phone|是|银行预留手机号|
|real_name|是|姓名|
|wallet_id|是|钱包id|
|bank_code|否|支行编码|
|cvv2|否|信用卡cvv2码|
|validate|否|信用卡到期4位日期|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-解绑银行卡

请求地址: /wallet_server/v1/m/senior/card/unbind_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_id|是|银行卡id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
|jump_url|是|跳转地址|
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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

###  高级钱包-余额明细

请求地址: /wallet_server/v1/m/senior/wallet/order_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|biz_no|否|关联订单号|
|from_time|否|交易时间开始|
|limit|否|每页限制|
|offset|否|起始页偏移量|
|order_no|否|钱包订单号|
|stat|否|是否统计|
|status|否|状态|
|to_time|否|交易时间结束|
|trade_type|否|交易类型|
|wallet_id|否|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
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
    start_time:""  , //开始时间
    status:""  , //交易状态。 2：进行中，3：交易成功，4：交易失败
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
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  高级钱包-企业用户绑定手机

请求地址: /wallet_server/v1/m/senior/wallet/bind_phone

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|mobile|是|手机号码|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    audit_type:""  , //审核方式，1：运营，2：银企直连，4：通联
    balance_upd_time:""  , //钱包余额最后更新日期
    create_time:""  , //创建日期
    freeze_amount:""  , //冻结金额
    id:""  , //钱包ID
    last_upd_time:""  , //钱包信息最后更新日期
    level:""  , //钱包等级，1： 初级钱包，2： 高级钱包
    pay_amount:""  , //累计支付金额
    pay_count:""  , //累计支付次数
    recharge_amount:""  , //累计充值金额
    recharge_count:""  , //累计充值次数
    source:""  , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
    status:""  , //钱包状态: 1:待激活，2：激活,3：禁用
    title:""  , //钱包标题，通常是姓名或公司名
    type:""  , //钱包类型， 1：企业钱包，2：个人钱包
    wallet_balance:""   //钱包余额
}  
}
```

###  高级钱包-商家资料审核（通道）

请求地址: /wallet_server/v1/m/senior/wallet/company_info_audit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|company_basic_info|是|企业信息(json)|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包-企业信息

请求地址: /wallet_server/v1/m/senior/wallet/company_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|is_manual_refresh|是|手动更新开关|
|wallet_id|是|钱包id|
|public_account_no|否|新对公账号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    account_no:""  , //企业对公账户账号
    auth_type:""  , //认证类型(三证或一证)
    bank_city_no:""  , //开户行地区代码
    bank_name:""  , //开户行支行名称
    business_license:""  , //营业执照号(三证)
    check_time:""  , //审核时间
    city:""  , //开户行所在市
    company_address:""  , //企业地址
    company_name:""  , //企业名称
    exp_license:""  , //统一社会信用/营业执照号到期时间 格式:yyyy-MM-dd
    fail_reason:""  , //审核失败原因
    identity_type:""  , //法人证件类型
    is_sign_contract:""  , //是否已签电子协议
    legal_ids:""  , //法人证件号码
    legal_name:""  , //法人姓名
    legal_phone:""  , //法人手机号码
    organization_code:""  , //组织机构代码(三证)
    parent_bank_name:""  , //开户银行名称
    phone:""  , //手机号码
    province:""  , //开户行所在省
    remark:""  , //备注
    status:""  , //审核状态
    tax_register:""  , //税务登记证(三证)
    telephone:""  , //联系电话
    uni_credit:""  , //统一社会信用(一证)
    union_bank:""   //支付行号,12位数字
}  
}
```

###  高级钱包-个人认证

请求地址: /wallet_server/v1/m/senior/wallet/person_authentication

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|id_no|是|身份证号|
|jump_url|是|前端跳转地址|
|mobile|是|手机号码|
|real_name|是|姓名|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  高级钱包-个人信息

请求地址: /wallet_server/v1/m/senior/wallet/person_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    address:""  , //地址
    area:""  , //县市
    country:""  , //国家
    identity_card_no:""  , //身份证号码
    is_identity_checked:""  , //是否进行实名认证
    is_phone_checked:""  , //是否绑定手机
    is_set_pay_pwd:""  , //是否已设置支付密码
    is_sign_contract:""  , //是否已签电子协议
    name:""  , //姓名
    pay_fail_amount:""  , //支付失败次数
    phone:""  , //手机号码
    province:""  , //省份
    real_name_time:""  , //实名认证时间
    register_ip:""  , //创建ip
    register_time:""  , //创建时间
    remark:""  , //备注
    source:""  , //访问终端类型
    user_id:""  , //云商通用户id
    user_state:""   //用户状态
}  
}
```

###  高级钱包-个人设置支付密码

请求地址: /wallet_server/v1/m/senior/wallet/person_set_paypassword

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|jump_url|是|前端跳转地址|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  高级钱包-委托代扣协议

请求地址: /wallet_server/v1/m/senior/wallet/balance_protocol

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|jump_url|是|前端跳转地址|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  高级钱包-会员协议

请求地址: /wallet_server/v1/m/senior/wallet/member_protocol

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|jump_url|是|前端跳转地址|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  高级钱包认证验证码

请求地址: /wallet_server/v1/m/senior/wallet/sms_verify_code

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|mobile|是|手机号码|
|sms_type|是|短信类型|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包渠道信息

请求地址: /wallet_server/v1/m/senior/wallet/tunnel_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|tunnel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}  
}
```

###  升级高级钱包

请求地址: /wallet_server/v1/m/wallet/upgrade_wallet

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包-修改支付密码

请求地址: /wallet_server/v1/m/senior/wallet/update_pay_pwd

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|jump_url|是|前端跳转地址|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  高级钱包-修改手机

请求地址: /wallet_server/v1/m/senior/wallet/update_security_tel

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_id|是|钱包id|
|jump_url|否|前端回跳地址|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    url:""   //
}  
}
```

###  富慧通审核企业商家钱包

请求地址: /wallet_server/v1/m/wallet/active_wallet_company

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式，1：运营，2：银企直连，4：通联|
|company_name|是|公司名称|
|status|是|钱包状态: 1:待审核，2：激活,3：禁用|
|wallet_id|是|钱包ID|
|email|否|公司邮箱|
|phone|否|公司电话|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  富慧通审核个人商家钱包

请求地址: /wallet_server/v1/m/wallet/active_wallet_person

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式，1：运营，2：银企直连，4：通联|
|id_no|是|证件号|
|id_type|是|证件类型，1:身份证|
|name|是|姓名|
|status|是|钱包状态: 1:待审核，2：激活,3：禁用|
|wallet_id|是|钱包ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  绑定银行卡(对公)

请求地址: /wallet_server/v1/m/wallet/bank_card/bind

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|bank_account|是|银行帐号|
|bank_code|是|开户支行|
|deposit_name|是|开户名|
|wallet_id|是|钱包id|
|is_def|否|是否默认银行卡: 1:是，2：否|
|telephone|否|预留手机号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    bank_account:""  , //银行账号
    bank_code:""  , //银行代码
    bank_name:""  , //银行名称
    card_type:""  , //银行卡类型 1-储蓄卡 2-信用卡
    create_time:""  , //创建日期
    deposit_bank:""  , //开户支行
    deposit_name:""  , //开户名
    id:""  , //ID
    is_def:""  , //是否默认银行卡: 1:是，2：否
    is_public:""  , //是否对公账户: 1:是，2：否
    last_upd_time:""  , //钱包信息最后更新日期
    status:""  , //绑定状态: 1:已绑定，2：已解绑
    telephone:""  , //预留手机号
    verify_channel:""  , //验证渠道。1:安全验证服务，2：通联快捷支付
    verify_time:""  , //验证时间
    wallet_id:""   //钱包ID
}  
}
```

###  钱包绑定的银行卡列表

请求地址: /wallet_server/v1/m/wallet/bank_card/list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  开通未审核的钱包

请求地址: /wallet_server/v1/m/wallet/create_wallet

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|title|是|钱包标题，通常是姓名或公司名|
|type|是|钱包类型， 1：企业钱包，2：个人钱包|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    audit_type:""  , //审核方式，1：运营，2：银企直连，4：通联
    balance_upd_time:""  , //钱包余额最后更新日期
    create_time:""  , //创建日期
    freeze_amount:""  , //冻结金额
    id:""  , //钱包ID
    last_upd_time:""  , //钱包信息最后更新日期
    level:""  , //钱包等级，1： 初级钱包，2： 高级钱包
    pay_amount:""  , //累计支付金额
    pay_count:""  , //累计支付次数
    recharge_amount:""  , //累计充值金额
    recharge_count:""  , //累计充值次数
    source:""  , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
    status:""  , //钱包状态: 1:待激活，2：激活,3：禁用
    title:""  , //钱包标题，通常是姓名或公司名
    type:""  , //钱包类型， 1：企业钱包，2：个人钱包
    wallet_balance:""   //钱包余额
}  
}
```

###  通过短信验证码登录

请求地址: /wallet_server/v1/m/wallet/login_with_verify_code

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|ip|是|来源IP|
|mobile|是|手机号码|
|type|是|短信类型, 1:登录当前钱包, 2:登录已开通钱包|
|verify_code|是|验证码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    business_remark:""  , //业务定制内容
    create_time:""  , //创建日期
    last_upd_time:""  , //钱包信息最后更新日期
    mobile:""  , //登录手机号
    register_progress:""  , //注册进度,二进制形式 0:初始化帐号 1:已通过身份验证 2:已创建钱包 4:已绑定银行 8:已签订协议
    status:""  , //帐号状态: 1:正常，2：禁用
    user_id:""  , //用户ID
    wallet_id:""   //关联的钱包ID
}  
}
```

###  查询支付状态

请求地址: /wallet_server/v1/m/wallet/query_wallet_apply

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|batch_no|否|钱包批次号|
|biz_no|否|业务凭证号(业务方定义唯一)|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  通过UID查询钱包信息（企业or个人）

请求地址: /wallet_server/v1/m/wallet/query_wallet_info_by_uid

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|user_id|是|用户ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    bank_card_count:""  , //银行卡数量
    company_info:{
    company_name:""  , //公司名称
    create_time:""  , //创建日期
    email:""  , //公司邮箱
    id:""  , //ID
    last_upd_time:""  , //钱包信息最后更新日期
    tel:""  , //公司电话
    wallet_id:""   //关联的钱包ID
}  , 
    def_wallet_card:{
    bank_account:""  , //银行账号
    bank_code:""  , //银行代码
    bank_name:""  , //银行名称
    card_type:""  , //银行卡类型 1-储蓄卡 2-信用卡
    create_time:""  , //创建日期
    deposit_bank:""  , //开户支行
    deposit_name:""  , //开户名
    id:""  , //ID
    is_def:""  , //是否默认银行卡: 1:是，2：否
    is_public:""  , //是否对公账户: 1:是，2：否
    last_upd_time:""  , //钱包信息最后更新日期
    status:""  , //绑定状态: 1:已绑定，2：已解绑
    telephone:""  , //预留手机号
    verify_channel:""  , //验证渠道。1:安全验证服务，2：通联快捷支付
    verify_time:""  , //验证时间
    wallet_id:""   //钱包ID
}  , 
    person_info:{
    create_time:""  , //创建日期
    email:""  , //邮箱
    id:""  , //ID
    id_no:""  , //证件号
    id_type:""  , //证件类型，1:身份证
    last_upd_time:""  , //钱包信息最后更新日期
    name:""  , //姓名
    real_level:""  , //实名认证类型，1:身份证实名,2:手机号实名
    tel:""  , //电话
    wallet_id:""   //关联的钱包ID
}  , 
    wallet:{
    audit_type:""  , //审核方式，1：运营，2：银企直连，4：通联
    balance_upd_time:""  , //钱包余额最后更新日期
    create_time:""  , //创建日期
    freeze_amount:""  , //冻结金额
    id:""  , //钱包ID
    last_upd_time:""  , //钱包信息最后更新日期
    level:""  , //钱包等级，1： 初级钱包，2： 高级钱包
    pay_amount:""  , //累计支付金额
    pay_count:""  , //累计支付次数
    recharge_amount:""  , //累计充值金额
    recharge_count:""  , //累计充值次数
    source:""  , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
    status:""  , //钱包状态: 1:待激活，2：激活,3：禁用
    title:""  , //钱包标题，通常是姓名或公司名
    type:""  , //钱包类型， 1：企业钱包，2：个人钱包
    wallet_balance:""   //钱包余额
}  , 
    wallet_tunnel:{
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}   
}  
}
```

###  查询钱包信息（企业or个人）

请求地址: /wallet_server/v1/m/wallet/query_wallet_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_id|是|钱包ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    bank_card_count:""  , //银行卡数量
    company_info:{
    company_name:""  , //公司名称
    create_time:""  , //创建日期
    email:""  , //公司邮箱
    id:""  , //ID
    last_upd_time:""  , //钱包信息最后更新日期
    tel:""  , //公司电话
    wallet_id:""   //关联的钱包ID
}  , 
    def_wallet_card:{
    bank_account:""  , //银行账号
    bank_code:""  , //银行代码
    bank_name:""  , //银行名称
    card_type:""  , //银行卡类型 1-储蓄卡 2-信用卡
    create_time:""  , //创建日期
    deposit_bank:""  , //开户支行
    deposit_name:""  , //开户名
    id:""  , //ID
    is_def:""  , //是否默认银行卡: 1:是，2：否
    is_public:""  , //是否对公账户: 1:是，2：否
    last_upd_time:""  , //钱包信息最后更新日期
    status:""  , //绑定状态: 1:已绑定，2：已解绑
    telephone:""  , //预留手机号
    verify_channel:""  , //验证渠道。1:安全验证服务，2：通联快捷支付
    verify_time:""  , //验证时间
    wallet_id:""   //钱包ID
}  , 
    person_info:{
    create_time:""  , //创建日期
    email:""  , //邮箱
    id:""  , //ID
    id_no:""  , //证件号
    id_type:""  , //证件类型，1:身份证
    last_upd_time:""  , //钱包信息最后更新日期
    name:""  , //姓名
    real_level:""  , //实名认证类型，1:身份证实名,2:手机号实名
    tel:""  , //电话
    wallet_id:""   //关联的钱包ID
}  , 
    wallet:{
    audit_type:""  , //审核方式，1：运营，2：银企直连，4：通联
    balance_upd_time:""  , //钱包余额最后更新日期
    create_time:""  , //创建日期
    freeze_amount:""  , //冻结金额
    id:""  , //钱包ID
    last_upd_time:""  , //钱包信息最后更新日期
    level:""  , //钱包等级，1： 初级钱包，2： 高级钱包
    pay_amount:""  , //累计支付金额
    pay_count:""  , //累计支付次数
    recharge_amount:""  , //累计充值金额
    recharge_count:""  , //累计充值次数
    source:""  , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
    status:""  , //钱包状态: 1:待激活，2：激活,3：禁用
    title:""  , //钱包标题，通常是姓名或公司名
    type:""  , //钱包类型， 1：企业钱包，2：个人钱包
    wallet_balance:""   //钱包余额
}  , 
    wallet_tunnel:{
    balance:""  , //银行余额
    balance_protocol_no:""  , //扣款协议号
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_dirty:""  , //脏数据标识 1：正常 2：脏数据
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    tunnel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    tunnel_user_id:""  , //银行用户标识
    wallet_id:""   //钱包id
}   
}  
}
```

###  发送短信验证码

请求地址: /wallet_server/v1/m/wallet/send_verify_code

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|ip|是|来源IP|
|mobile|是|手机号码|
|type|是|验证码类型, 1:登录, 2:验证已开通钱包帐号|
|verify_token|是|反作弊结果查询token|
|biz_user_id|否|业务用户id|
|channel_type|否|渠道类型 1:浦发银企直连,2:通联云商通|
|redirect_url|否|触发图形验证码并验证成功后重定向地址|
|source|否|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

