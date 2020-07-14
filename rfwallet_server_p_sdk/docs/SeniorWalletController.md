###  高级钱包-商家绑定终端

请求地址: /wallet_server/v1/m/senior/wallet/bind_terminal2

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|app_id|是|APPID|
|biz_user_id|是|渠道用户ID|
|vsp_cusid|是|子商户号|
|vsp_merchantid|是|集团号|
|vsp_termid|是|终端号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    biz_user_id:""  , //商户系统用户标识，商户系统中唯一编号。
    result:""  , //绑定、查询收银宝终端号结果
    vsp_termid_list: [ {
    set_date:""  , //绑定时间
    vsp_cusid:""  , //收银宝商户号  单商户模式：商户收银宝商户号  集团模式：收银宝子商户号
    vsp_merchantid:""  , //收银宝集团商户号 集团模式：集团商户收银宝商户号单商户模式：不返
    vsp_termid:""   //收银宝终端号
}  ]   
}  
}
```

###  高级钱包-商家绑定终端

请求地址: /wallet_server/v1/m/senior/wallet/bind_terminal

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|terminal_id|是|终端id|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-新增终端

请求地址: /wallet_server/v1/m/senior/wallet/create_terminal

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|app_id|是|通联appId|
|mch_id|是|商家id|
|mch_name|是|商家名称|
|province|是|省份|
|shop_address|是|门店地址|
|vsp_cusid|是|子商户号|
|vsp_merchantid|是|集团商户号|
|vsp_termid|是|终端号|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  高级钱包-线下确认更新企业信息

请求地址: /wallet_server/v1/m/senior/wallet/manual_company_audit

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

###  高级钱包-终端列表

请求地址: /wallet_server/v1/m/senior/wallet/query_terminal

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|limit|是|limit|
|offset|是|offset|
|mch_id|否|商家id|
|province|否|省份|
|vsp_cusid|否|子商户号|
|vsp_termid|否|终端号|
|wallet_id|否|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    app_id:""  , //通联appId
    bind_time:""  , //绑定时间
    biz_user_id:""  , //会员ID
    create_time:""  , //创建时间
    id:""  , //id
    mch_id:""  , //商家id
    mch_name:""  , //商家id
    province:""  , //省份
    shop_address:""  , //门店地址
    status:""  , //状态： 0：未绑定，1：已绑定，2：已解绑
    unbind_time:""  , //绑定时间
    vsp_cusid:""  , //子商户号
    vsp_merchantid:""  , //集团商户号
    vsp_termid:""  , //终端号
    wallet_id:""   //钱包id
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  高级钱包-重置支付密码

请求地址: /wallet_server/v1/m/senior/wallet/reset_pay_pwd

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

###  高级钱包-余额明细

请求地址: /wallet_server/v1/m/senior/wallet/order_detail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|from_time|否|交易时间开始|
|limit|否|每页限制|
|offset|否|起始页偏移量|
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
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
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
|id_no|是|身份证号|
|jump_url|是|前端跳转地址|
|mobile|是|手机号码|
|real_name|是|姓名|
|tunnel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|user_id|是|用户id|
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

###  高级钱包-用户绑定钱包身份

请求地址: /wallet_server/v1/m/senior/wallet/person_idbind

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|user_id|是|用户id|
|wallet_id|是|钱包id|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
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
    progress:""  , //钱包进度，组合字段 1:通道已注册会员 2:通道已绑定手机 4:通道已实名 8:通道已签约 16:已设置支付密码 32:已绑卡
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
    balance_protocol_no:""  , //扣款协议编号
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
    balance_protocol_no:""  , //扣款协议编号
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
    balance_protocol_no:""  , //扣款协议编号
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
    balance_protocol_no:""  , //扣款协议编号
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

###  高级钱包-解绑手机

请求地址: /wallet_server/v1/m/senior/wallet/unbind_phone

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

