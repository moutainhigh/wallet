###  查询钱包信息（企业or个人）

请求地址: /v1/wallet/query_wallet_info

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
        user_info: {
                        create_time: "" , //创建日期
        id: "" , //帐号ID
        last_upd_time: "" , //钱包信息最后更新日期
        mobile: "" , //登录手机号
        register_progress: "" , //注册进度, 1:已通过身份验证, 2:已开通钱包
        status: "" , //帐号状态: 1:正常，2：禁用
        uid: "" , //用户ID
        wallet_id: ""  //关联的钱包ID

            },
        wallet: {
                        audit_type: "" , //审核方式，1：运营，3：银企直连，5：通联
        balance_upd_time: "" , //钱包余额最后更新日期
        create_time: "" , //创建日期
        id: "" , //钱包ID
        last_upd_time: "" , //钱包信息最后更新日期
        pay_amount: "" , //累计支付金额
        pay_count: "" , //累计支付次数
        recharge_amount: "" , //累计充值金额
        recharge_count: "" , //累计充值次数
        status: "" , //钱包状态: 1:待审核，2：激活,3：禁用
        title: "" , //钱包标题，通常是姓名或公司名
        type: "" , //钱包类型， 1：企业钱包，2：个人钱包
        wallet_balance: ""  //钱包余额

            }

  }
}
```

