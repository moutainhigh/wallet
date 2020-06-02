###  充值审批-商家确认充值

请求地址: /mch/v1/m/recharge/mch_confirm_recharge

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌|
|apply_id|是|申请单id|
|mch_id|是|商家ID|
|user_id|是|用户ID|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  充值审批-支付中心出纳审批

请求地址: /mch/v1/m/recharge/rfpay_finance_audit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌|
|apply_id|是|充值单ID|
|audit_result|是|审批状态,1:审核成功, 2:审核失败|
|user_id|是|操作人ID|
|user_name|是|操作人名称|
|user_role|是|操作人角色(支付中心自带)|
|reason|否|失败原因|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    apply_time:""  , //申请时间
    apply_user:""  , //申请用户
    apply_user_name:""  , //申请人姓名
    audit_pic_urls:""  , //资料图片
    balance:""  , //余额
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
}  
}
```

###  充值审批-支付中心负责人审批（内部商家）

请求地址: /mch/v1/m/recharge/rfpay_manager_audit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌|
|apply_id|是|充值单ID|
|audit_result|是|审批状态,1:审核成功, 2:审核失败|
|user_id|是|操作人ID|
|user_name|是|操作人名称|
|user_role|是|操作人角色|
|reason|否|失败原因|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    apply_time:""  , //申请时间
    apply_user:""  , //申请用户
    apply_user_name:""  , //申请人姓名
    audit_pic_urls:""  , //资料图片
    balance:""  , //余额
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
}  
}
```

###  充值审批-思力运营提交充值申请

请求地址: /mch/v1/m/recharge/recharge_apply

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌(支付中心自带)|
|mch_id|是|商户ID|
|outlay_account_id|是|营销账号|
|recharge_amount|是|充值金额|
|recharge_type|是|充值类型, 1:直充；2:充值转积分;3:充值转优惠券|
|apply_user|否|申请用户ID(支付中心自带)|
|apply_user_name|否|申请用户名称(支付中心自带)|
|outer_bill_id|否|应用订单号或标识|
|pic_urls|否|单据url|
|remark|否|备注|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
  
}
```

###  充值审批-支付中心运营审批（外部商家）

请求地址: /mch/v1/m/recharge/rfpay_operator_audit

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|管理令牌(支付中心自带)|
|apply_id|是|充值单ID|
|audit_result|是|审批状态,1:审核成功, 2:审核失败|
|user_id|是|操作人ID(支付中心自带)|
|user_name|是|操作人名称(支付中心自带)|
|user_role|是|操作人角色(支付中心自带)|
|audit_pic_url|否|查账截图|
|reason|否|失败原因|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    apply_time:""  , //申请时间
    apply_user:""  , //申请用户
    apply_user_name:""  , //申请人姓名
    audit_pic_urls:""  , //资料图片
    balance:""  , //余额
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
}  
}
```

