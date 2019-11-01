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
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
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
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
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
   , "data":  {
    url:""   //
}  
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
   , "data":  {
    url:""   //
}  
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
   , "data":  {
    url:""   //
}  
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
   , "data":  {
    url:""   //
}  
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
   , "data":  {
    url:""   //
}  
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
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
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
    balance_protocol_req_sn:""  , //扣款协议请求流水号
    biz_user_id:""  , //业务用户标识
    channel_type:""  , //渠道类型。1: 浦发银企直连，2：通联云商通
    channel_user_id:""  , //银行用户标识
    check_time:""  , //审核时间
    create_time:""  , //创建日期
    fail_reason:""  , //失败原因
    freezen_amount:""  , //冻结金额
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

