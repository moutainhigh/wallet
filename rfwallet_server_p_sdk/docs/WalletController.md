###  富慧通审核企业商家钱包

请求地址: /wallet_server/v1/m/wallet/audit_wallet_company

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
    bank_class:""  , //银行分类代码
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

请求地址: /wallet_server/v1/m/wallet/create_mch_wallet

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|company_name|是|公司名称|
|mch_id|是|商家ID|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|title|是|钱包标题，通常是姓名或公司名|
|type|是|钱包类型， 1：企业钱包，2：个人钱包|
|email|否|公司邮箱|
|tel|否|公司电话|

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
    bank_class:""  , //银行分类代码
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
    progress:""  , //钱包进度，组合字段 1:通道已注册会员 2:通道已绑定手机 4:通道已实名 8:通道已签约 16:已设置支付密码 32:已绑卡
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
    bank_class:""  , //银行分类代码
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
    progress:""  , //钱包进度，组合字段 1:通道已注册会员 2:通道已绑定手机 4:通道已实名 8:通道已签约 16:已设置支付密码 32:已绑卡
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

###  设置出款申请单状态为失败

请求地址: /wallet_server/v1/m/wallet/apply_bill/set_status_fail

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_comment|是|备注|
|audit_user|是|设置人|
|audit_user_id|是|设置人ID|
|batch_no|是|批次号|
|biz_no|是|业务单号|

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

###  钱包列表

请求地址: /wallet_server/v1/m/wallet/wallet_list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|访问令牌|
|limit|是|必填，需要查询的数量（数量最大50）|
|offset|是|必填，查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|stat|是|非必填, false:否, true:是, 是否返回数据总量, 默认false|
|status|否|钱包状态: 1:待审核，2：激活,3：禁用|
|title|否|钱包名字|
|type|否|钱包类型， 1：企业钱包，2：个人钱包|
|wallet_level|否|钱包等级 1： 初级钱包，2： 高级钱包|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
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
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

