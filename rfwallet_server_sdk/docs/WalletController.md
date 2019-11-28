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

###  银行地区列表

请求地址: /wallet_server/v1/wallet/bank/area_list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|class_code|是|银行类型编码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  银行类别列表

请求地址: /wallet_server/v1/wallet/bank/class_list

请求类型: POST

请求参数:



返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  银行支行列表

请求地址: /wallet_server/v1/wallet/bank/bank_list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|area_code|是|地区编码|
|class_code|是|银行类型编码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

###  银行支行信息

请求地址: /wallet_server/v1/wallet/bank/bank

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|bank_code|是|银行编码|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    area_code:""  , //所属地区
    bank_code:""  , //银行行号
    bank_name:""  , //银行名称
    city_name:""  , //所属省市
    class_code:""  , //所属分类代码
    class_name:""  , //所属分类行名
    id:""  , //ID
    province_name:""  , //所属地区
    weight:""   //
}  
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

