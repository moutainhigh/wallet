###  查询钱包信息（企业or个人）

请求地址: /wallet_server/v1/u/wallet/query_wallet_base_info

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
    wallet_owner_list: [ {
    deleted:""  , //是否删除 0：正常 1：已删除
    id:""  , //id
    owner_id:""  , //所有者id
    owner_type:""  , //所有者类型,1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户
    wallet_id:""   //钱包id
}  ]   
}  
}
```

###  业务查询银行卡信息

请求地址: /wallet_server/v1/u/wallet/query_wallet_card

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
   , "data": [ {
} ] 
}
```

