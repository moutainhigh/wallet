###  高级钱包-订单结果查询

请求地址: /wallet_server/v1/u/senior/pay/order_query

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
    charging_type:""  , //计费方式，1按次收费，2按比率收费
    charging_value:""  , //计费单价，计费比例或金额
    create_time:""  , //创建日期
    curr_try_times:""  , //当前尝试次数
    end_time:""  , //结束时间
    expire_time:""  , //过期时间
    good_desc:""  , //商品描述
    good_name:""  , //商品名称
    id:""  , //id
    industry_code:""  , //行业代码（由渠道分配
    industry_name:""  , //行业名称（由渠道分配）
    locked:""  , //1:未锁 2：锁定
    note:""  , //附言,长度由通道确定
    notified:""  , //1:已通知技术 2:已通知业务
    order_no:""  , //订单号
    pay_method:""  , //支付方式 1：余额 2：微信 4：支付宝 8:刷卡支付 16：银行卡
    pay_type:""  , //pos机实际支付类型：61-建单; 62-微信支付; 63-手机QQ支付; 64-支付宝; 65-银联
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