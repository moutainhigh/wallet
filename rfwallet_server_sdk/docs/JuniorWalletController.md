###  初级钱包-思力批量出钱（最多20笔）

请求地址: /wallet-server/v1/junior/sl_batch_pay_in

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|jsonArry|是|json数组，参考思力出钱单笔接口，拼装成数组即可( 钱包类型必须统一为企业或个人 )|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              accept_no: "" , //受理编号
        fail_count: "" , //失败笔数
        seq_no: "" , //柜员流水号
        success_count: ""  //成功笔数

  }
}
```

###  初级钱包-思力出钱

请求地址: /wallet-server/v1/junior/sl_pay_in

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|amount|是|支付金额(单位分)|
|elec_cheque_no|是|电子凭证号(业务方定义唯一)|
|wallet_id|是|钱包ID|
|note|否|附言|
|pay_purpose|否|支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": {
              accept_no: "" , //受理编号
        fail_count: "" , //失败笔数
        seq_no: "" , //柜员流水号
        success_count: ""  //成功笔数

  }
}
```

###  初级钱包-查询支付状态

请求地址: /wallet-server/v1/junior/sl_query

请求类型: POST

请求参数:

| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|access_token|
|accept_no|否|受理编号|
|elec_cheque_no|否|电子凭证号(业务方定义唯一)|


返回数据
```
{
  "code": 1001,//状态码
  "msg": "", //消息
"data": [{
              accept_no: "" , //受理编号
        amount: "" , //支付金额
        elec_cheque_no: "" , //电子凭证号
        err_msg: "" , //失败原因
        status: "" , //支付状态。1：受理中，2：交易成功。3：交易失败
        trans_date: ""  //交易日期

      
  }]
}
```

