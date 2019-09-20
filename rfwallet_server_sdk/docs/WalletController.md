###  富慧通审核企业商家钱包

请求地址: /wallet_server/v1/m/wallet/active_wallet_company

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式，1：运营，3：银企直连，5：通联|
|company_name|是|公司名称|
|status|是|钱包状态: 1:待审核，2：激活,3：禁用|
|wallet_id|是|钱包ID|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
  }
}
```

###  富慧通审核个人商家钱包

请求地址: /wallet_server/v1/m/wallet/active_wallet_person

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|audit_type|是|审核方式，1：运营，3：银企直连，5：通联|
|id_no|是|证件号|
|id_type|是|证件类型，1:身份证|
|name|是|姓名|
|status|是|钱包状态: 1:待审核，2：激活,3：禁用|
|wallet_id|是|钱包ID|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
  }
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
  "msg": "", //消息
"data": [{
              area_code: "" , //地区编码
        city_name: "" , //市
        province_name: ""  //省

      
  }]
}
```

###  银行类别列表

请求地址: /wallet_server/v1/wallet/bank/class_list

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": [{
              class_code: "" , //银行编码
        class_name: ""  //银行名称

      
  }]
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
  "msg": "", //消息
"data": [{
              bank_code: "" , //支行编码
        bank_name: ""  //支行名称

      
  }]
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
  "msg": "", //消息
"data": {
              area_code: "" , //所属地区
        bank_code: "" , //银行行号
        bank_name: "" , //银行名称
        city_name: "" , //所属省市
        class_code: "" , //所属分类代码
        class_name: "" , //所属分类行名
        id: "" , //ID
        province_name: "" , //所属地区
        weight: ""  //

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
  "msg": "", //消息
"data": {
              bank_account: "" , //银行账号
        bank_code: "" , //银行代码
        bank_name: "" , //银行名称
        create_time: "" , //创建日期
        deposit_bank: "" , //开户支行
        deposit_name: "" , //开户名
        id: "" , //ID
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
        verify_channel: "" , //验证渠道。1:安全验证服务，2：通联快捷支付
        verify_time: "" , //验证时间
        wallet_id: ""  //钱包ID

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
  "msg": "", //消息
"data": [{
              bank_account: "" , //银行账号
        bank_code: "" , //银行代码
        bank_name: "" , //银行名称
        create_time: "" , //创建日期
        deposit_bank: "" , //开户支行
        deposit_name: "" , //开户名
        id: "" , //ID
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
        verify_channel: "" , //验证渠道。1:安全验证服务，2：通联快捷支付
        verify_time: "" , //验证时间
        wallet_id: ""  //钱包ID

      
  }]
}
```

###  开通未审核的高级钱包

请求地址: /wallet_server/v1/m/wallet/create_wallet

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|title|是|钱包标题，通常是姓名或公司名|
|type|是|钱包类型， 1：企业钱包，2：个人钱包|
|wallet_level|是|钱包等级 1:初级钱包,2:高级钱包|
|biz_user_id|否|业务用户id|
|channel_type|否|渠道类型 1:浦发银企直连,2:通联云商通|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              audit_type: "" , //审核方式，1：运营，2：银企直连，4：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        freeze_amount: "" , //冻结金额
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        source: "" , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
        status: "" , //钱包状态: 1:待激活，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

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
  "msg": "", //消息
"data": {
              business_remark: "" , //业务定制内容
        create_time: "" , //创建日期
        last_upd_time: "" , //钱包信息最后更新日期
        mobile: "" , //登录手机号
        register_progress: "" , //注册进度,二进制形式 0:初始化帐号 1:已通过身份验证 2:已创建钱包 4:已绑定银行 8:已签订协议
        status: "" , //帐号状态: 1:正常，2：禁用
        user_id: "" , //用户ID
        wallet_id: ""  //关联的钱包ID

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
  "msg": "", //消息
"data": [{
              amount: "" , //支付金额
        batch_no: "" , //钱包批次号
        biz_no: "" , //业务凭证号
        biz_time: "" , //银行交易终态日期
        create_time: "" , //创建日期
        elec_cheque_no: "" , //电子凭证号
        end_time: "" , //交易结束时间（浦发只有时分秒，查询成功定为交易结束时间）
        err_code: "" , //错误码
        lanch_time: "" , //银行发起时间
        note: "" , //附言(不超过100)
        payee_account: "" , //收款方帐号
        payee_bank_code: "" , //收款银行行号
        payee_bank_info: {
                        area_code: "" , //所属地区
        bank_code: "" , //银行行号
        bank_name: "" , //银行名称
        city_name: "" , //所属省市
        class_code: "" , //所属分类代码
        class_name: "" , //所属分类行名
        id: "" , //ID
        province_name: "" , //所属地区
        weight: ""  //

            },
        payee_name: "" , //收款方户名
        payee_type: "" , //收款账户类型，1：对公账户，2：个人账户
        remark: "" , //备注
        status: "" , //交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销，6：待处理
        sys_err_msg: "" , //系统错误信息
        trans_date: "" , //钱包收单日期
        user_err_msg: ""  //用户错误信息

      
  }]
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
  "msg": "", //消息
"data": {
              bank_card_count: "" , //银行卡数量
        company_info: {
                        company_name: "" , //公司名称
        create_time: "" , //创建日期
        email: "" , //公司邮箱
        id: "" , //ID
        last_upd_time: "" , //钱包信息最后更新日期
        tel: "" , //公司电话
        wallet_id: ""  //关联的钱包ID

            },
        def_wallet_card: {
                        bank_account: "" , //银行账号
        bank_code: "" , //银行代码
        bank_name: "" , //银行名称
        create_time: "" , //创建日期
        deposit_bank: "" , //开户支行
        deposit_name: "" , //开户名
        id: "" , //ID
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
        verify_channel: "" , //验证渠道。1:安全验证服务，2：通联快捷支付
        verify_time: "" , //验证时间
        wallet_id: ""  //钱包ID

            },
        person_info: {
                        create_time: "" , //创建日期
        email: "" , //邮箱
        id: "" , //ID
        id_no: "" , //证件号
        id_type: "" , //证件类型，1:身份证
        last_upd_time: "" , //钱包信息最后更新日期
        name: "" , //姓名
        real_level: "" , //实名认证类型，1:身份证实名,2:手机号实名
        tel: "" , //电话
        wallet_id: ""  //关联的钱包ID

            },
        wallet: {
                        audit_type: "" , //审核方式，1：运营，2：银企直连，4：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        freeze_amount: "" , //冻结金额
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        source: "" , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
        status: "" , //钱包状态: 1:待激活，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

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
  "msg": "", //消息
"data": {
              bank_card_count: "" , //银行卡数量
        company_info: {
                        company_name: "" , //公司名称
        create_time: "" , //创建日期
        email: "" , //公司邮箱
        id: "" , //ID
        last_upd_time: "" , //钱包信息最后更新日期
        tel: "" , //公司电话
        wallet_id: ""  //关联的钱包ID

            },
        def_wallet_card: {
                        bank_account: "" , //银行账号
        bank_code: "" , //银行代码
        bank_name: "" , //银行名称
        create_time: "" , //创建日期
        deposit_bank: "" , //开户支行
        deposit_name: "" , //开户名
        id: "" , //ID
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
        verify_channel: "" , //验证渠道。1:安全验证服务，2：通联快捷支付
        verify_time: "" , //验证时间
        wallet_id: ""  //钱包ID

            },
        person_info: {
                        create_time: "" , //创建日期
        email: "" , //邮箱
        id: "" , //ID
        id_no: "" , //证件号
        id_type: "" , //证件类型，1:身份证
        last_upd_time: "" , //钱包信息最后更新日期
        name: "" , //姓名
        real_level: "" , //实名认证类型，1:身份证实名,2:手机号实名
        tel: "" , //电话
        wallet_id: ""  //关联的钱包ID

            },
        wallet: {
                        audit_type: "" , //审核方式，1：运营，2：银企直连，4：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        freeze_amount: "" , //冻结金额
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        source: "" , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
        status: "" , //钱包状态: 1:待激活，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

            }

  }
}
```

###  重做问题单

请求地址: /wallet_server/v1/m/wallet/redo_pay

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|wallet_log_id|是|流水id|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
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
|type|是|验证码类型, 1:登录, 2:验证已开通钱包帐号, 3:验证高级钱包|
|verify_token|是|反作弊结果查询token|
|biz_user_id|否|业务用户id|
|channel_type|否|渠道类型 1:浦发银企直连,2:通联云商通|
|redirect_url|否|触发图形验证码并验证成功后重定向地址|
|source|否|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
  }
}
```

###  高级钱包认证

请求地址: /wallet_server/v1/m/wallet/senior_authentication

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|biz_user_id|是|业务用户id|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              balance: "" , //银行余额
        biz_user_id: "" , //业务用户标识
        channel_type: "" , //渠道类型。1: 浦发银企直连，2：通联云商通
        channel_user_id: "" , //银行用户标识
        check_time: "" , //审核时间
        create_time: "" , //创建日期
        fail_reason: "" , //失败原因
        id: "" , //id
        is_sign_contact: "" , //是否已签订协议
        member_type: "" , //银行用户类型。2：企业会员 3：个人会员
        pic_url: "" , //审核图片地址
        remark: "" , //备注
        security_tel: "" , //安全手机
        status: "" , //资料审核状态。1：待审核 ，2：审核成功，3：审核失败
        wallet_id: ""  //钱包id

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
|biz_user_id|是|业务用户id|
|channel_type|是|渠道类型 1:浦发银企直连,2:通联云商通|
|source|是|钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户|
|wallet_id|是|钱包id|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              balance: "" , //银行余额
        biz_user_id: "" , //业务用户标识
        channel_type: "" , //渠道类型。1: 浦发银企直连，2：通联云商通
        channel_user_id: "" , //银行用户标识
        check_time: "" , //审核时间
        create_time: "" , //创建日期
        fail_reason: "" , //失败原因
        id: "" , //id
        is_sign_contact: "" , //是否已签订协议
        member_type: "" , //银行用户类型。2：企业会员 3：个人会员
        pic_url: "" , //审核图片地址
        remark: "" , //备注
        security_tel: "" , //安全手机
        status: "" , //资料审核状态。1：待审核 ，2：审核成功，3：审核失败
        wallet_id: ""  //钱包id

  }
}
```

###  钱包流水

请求地址: /wallet_server/v1/m/wallet/log/list

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|limit|是|需要查询的数量（数量最大50）|
|offset|是|查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取|
|wallet_id|是|钱包id|
|end_time|否|结束时间|
|start_time|否|开始时间|
|stat|否|非必填, false:否, true:是, 是否返回数据总量, 默认false|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              list: [{
                        amount: "" , //流水金额
        batch_no: "" , //钱包批次号
        biz_no: "" , //业务凭证号
        biz_time: "" , //银行交易终态时间
        channel_type: "" , //渠道类型。 1：浦发银企直连，2：通联云商通
        create_time: "" , //创建日期
        curr_trans_id: "" , //当前交易ID
        curr_try_times: "" , //当前尝试次数
        id: "" , //ID
        lanch_time: "" , //银行发起时间
        lancher: "" , //发起方，当订单失败重新发起时可用。 1：钱包系统发起  2：用户发起
        max_try_times: "" , //最大尝试次数
        note: "" , //附言(不超过100)
        notified: "" , //1:已通知技术 2:已通知业务
        pay_purpose: "" , //支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入
        payee_account: "" , //收款方帐号
        payee_bank_code: "" , //收款银行行号
        payee_name: "" , //收款方户名
        payee_type: "" , //收款账户类型，1：对公账户，2：个人账户
        payer_account: "" , //付款方帐号
        query_time: "" , //计划查询时间
        remark: "" , //备注
        status: "" , //交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败(确切失败)，5：撤销，6：待处理，7：等待重试(用户或系统)
        type: "" , //类型，1：财务结算，2：收入，3：支出，4：充值，5：提现，6：退款，7：扣款
        wallet_id: "" , //钱包ID
        wallet_level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        wallet_type: ""  //钱包类型， 1：企业钱包，2：个人钱包

            },]
        page_limit: "" , //
        page_num: "" , //
        total: "" , //
        total_page: ""  //

  }
}
```

