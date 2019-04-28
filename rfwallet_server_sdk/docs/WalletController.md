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
|area＿code|是|地区编码|
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

###  绑定银行卡(对公)

请求地址: /wallet_server/v1/wallet/bank_card/bind

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
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
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
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
        wallet_id: ""  //钱包ID

      
  }]
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
  "msg": "", //消息
"data": {
              audit_type: "" , //审核方式，1：运营，3：银企直连，5：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        source: "" , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
        status: "" , //钱包状态: 1:待审核，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

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
        is_def: "" , //是否默认银行卡: 1:是，2：否
        is_public: "" , //是否对公账户: 1:是，2：否
        last_upd_time: "" , //钱包信息最后更新日期
        status: "" , //绑定状态: 1:已绑定，2：已解绑
        telephone: "" , //预留手机号
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
        tel: "" , //电话
        wallet_id: ""  //关联的钱包ID

            },
        wallet: {
                        audit_type: "" , //审核方式，1：运营，3：银企直连，5：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        level: "" , //钱包等级，1： 初级钱包，2： 高级钱包
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        source: "" , //钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
        status: "" , //钱包状态: 1:待审核，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

            }

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
                        accept_no: "" , //受理编号
        amount: "" , //流水金额
        create_time: "" , //创建日期
        elec_cheque_no: "" , //电子凭证号
        err_msg: "" , //错误信息
        id: "" , //ID
        payee_account: "" , //收款方帐号
        payee_type: "" , //收款账户类型，1：对公账户，2：个人账户
        payer_account: "" , //付款方帐号
        ref_method: "" , //关联接口方法，1：银企直连AQ52，2：银企直连8800
        remark: "" , //备注
        status: "" , //交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销
        type: "" , //流水类型，1：直接转帐，2：收入，3：支出
        wallet_id: ""  //钱包ID

            },]
        page_limit: "" , //
        page_num: "" , //
        total: "" , //
        total_page: ""  //

  }
}
```

