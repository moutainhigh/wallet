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

###  高级钱包企业用户绑定手机

请求地址: /wallet_server/v1/m/wallet/senior_bind_phone

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|mobile|是|手机号码|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包渠道信息

请求地址: /wallet_server/v1/m/wallet/channel_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包商家资料审核（通道）

请求地址: /wallet_server/v1/m/wallet/senior_company_info_audit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|company_basic_info|是|企业信息(json)|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    balance:""  , //银行余额
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包企业信息

请求地址: /wallet_server/v1/m/wallet/senior_company_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

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

###  高级钱包确认绑定银行卡

请求地址: /wallet_server/v1/m/wallet/senior_confirm_bind_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|phone|是|银行预留手机号|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|trans_date|是|验证银行卡申请时间|
|trans_num|是|验证银行卡流水号|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|
|cvv2|否|信用卡cvv2码|
|validate|否|信用卡到期4位日期|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包认证

请求地址: /wallet_server/v1/m/wallet/senior_person_authentication

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|id_no|是|身份证号|
|mobile|是|手机号码|
|real_name|是|姓名|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|verify_code|是|短信验证码|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包个人用户修改手机

请求地址: /wallet_server/v1/m/wallet/senior_person_change_bind_phone

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|id_no|是|身份证号|
|old_phone|是|手机号码|
|real_name|是|姓名|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包个人信息

请求地址: /wallet_server/v1/m/wallet/senior_person_info

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
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

###  高级钱包个人设置支付密码

请求地址: /wallet_server/v1/m/wallet/senior_person_set_paypassword

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|identity_no|是|身份证|
|name|是|姓名|
|phone|是|绑定手机|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包委托代扣协议

请求地址: /wallet_server/v1/m/wallet/senior_balance_protocol

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包会员协议

请求地址: /wallet_server/v1/m/wallet/senior_member_protocol

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包认证验证码

请求地址: /wallet_server/v1/m/wallet/senior_sms_verify_code

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
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包解除绑定银行卡

请求地址: /wallet_server/v1/m/wallet/senior_un_bind_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
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
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    has_pay_password:""  , //是否设置支付密码 1-是 2-否
    id:""  , //id
    is_sign_contact:""  , //签订通联会员协议 0-未签订 1-签订电子会员协议 2-签订扣款协议
    member_type:""  , //银行用户类型。2：企业会员 3：个人会员
    pic_url:""  , //审核图片地址
    remark:""  , //备注
    security_tel:""  , //安全手机
    status:""  , //资料审核状态。1: 未提交审核, 2：待审核 ，3：审核成功，4：审核失败
    wallet_id:""   //钱包id
}  
}
```

###  高级钱包验证银行卡

请求地址: /wallet_server/v1/m/wallet/senior_verify_bank_card

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|card_no|是|银行卡号|
|identity_no|是|身份证|
|phone|是|银行预留手机号|
|real_name|是|姓名|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|
|cvv2|否|信用卡cvv2码|
|validate|否|信用卡到期4位日期|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    bank_code:""  , //银行代码
    bank_name:""  , //银行名称
    biz_user_id:""  , //商户系统用户标识(个人用户:WU+walletId,个人商户:WM+walletId,企业用户:WC+walletId)
    card_type:""  , //银行卡类型 1-储蓄卡 2-信用卡
    trance_num:""  , //流水号
    trans_date:""   //申请时间 YYYYMMDD
}  
}
```

