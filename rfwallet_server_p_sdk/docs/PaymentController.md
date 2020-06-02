###  查询营销帐号列表

请求地址: /mch/v1/m/payment/outlay_account_list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|mch_id|是|商户ID|
|account_name|否|账号名称|
|limit|否|需要查询的数量（数量最大50）|
|offset|否|查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取|
|stat|否|非必填, false:否, true:是, 是否返回数据总量, 默认false|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    account_amount:""  , //账号余额
    account_name:""  , //账户名称
    create_time:""  , //创建时间
    mch_id:""  , //商户ID,三位随机码+秒级时间戳取后五位
    outlay_account_id:""  , //营销账号
    pay_amount:""  , //累计支出金额
    pay_count:""  , //累计支出次数
    recharge_amount:""  , //累计充值金额
    recharge_count:""  , //累计充值次数
    status:""   //账户状态, 1:正常,2:冻结
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  充值审批-查询充值申请

请求地址: /mch/v1/m/payment/query_recharge_apply

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|limit|是| 需要查询的数量（数量最大50）|
|offset|是| 查询起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|status|是|审批状态,1:资料待完善, 2:商户提交审核,3:支付中心运营审批通过,4:支付中心负责人审批通过,5:财务确认, 6:审核成功, 7:审核失败|
|sys_role|是|角色(*支付中心自动设置,前端无需显式设定)|
|end_date|否|结束时间|
|id|否|日志id|
|start_date|否|起始时间|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    apply_time:""  , //申请时间
    apply_user:""  , //申请用户
    apply_user_name:""  , //申请人姓名
    audit_pic_urls:""  , //资料图片
    audit_role_type:""  , //具有审核权限的角色类型
    balance:""  , //余额
    can_audit:""  , //当前用户是否具有审批权限
    confirm_time:""  , //确认时间
    confirm_user:""  , //确认用户
    confirm_user_name:""  , //确认人姓名
    create_time:""  , //创建时间
    id:""  , //
    last_audit_time:""  , //审核时间
    last_outlay_amount:""  , //充值前营销账户余额
    mch_biz_type:""  , //商家业务类型，1：外部商家，2：内部商家，3：外部直连
    mch_id:""  , //商户ID,三位随机码+秒级时间戳取后五位
    mch_name:""  , //商户名称
    next_outlay_amount:""  , //充值后营销账户余额
    outer_bill_id:""  , //应用订单号或标识
    outlay_account_id:""  , //营销账户
    outlay_account_name:""  , //营销账户名称
    pic_urls:""  , //资料图片
    reason:""  , //失败原因
    recharge_amount:""  , //充值金额
    recharge_type:""  , //充值类型, 1:直充；2:充值转积分;3:充值转优惠券
    remark:""  , //备注
    status:""   //申请状态, 1:资料待完善, 2:运营提交审核,3:支付中心运营审批通过,4:支付中心负责人审批通过,5:财务确认, 6:充值成功, 7:审核失败
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

###  充值审批-审批记录

请求地址: /mch/v1/m/payment/recharge_audit_log

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|apply_id|是|申请单ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data": [ {
} ] 
}
```

