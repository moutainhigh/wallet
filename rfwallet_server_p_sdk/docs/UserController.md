###  获取商家帐号列表

请求地址: /mch/v1/m/user/child/list

请求类型: POST

请求参数:


| 参数名 | 是否必须 | 描述 |
|:-- |:-- |:--   |
|access_token|是|应用令牌|
|limit|是|需要查询的数量（数量最大50）|
|mch_id|是|商户ID|
|offset|是|查询列表的起始偏移量，从0开始，即offset: 5是指从列表里的第六个开始读取。|
|keyword|否|关键字|
|role_id|否|角色ID|
|stat|否|是否返回数据总量,false:否, true:是,  默认false|
|status|否|账号状态, 1:正常, 2:冻结|

返回数据
```
{
  "code": 1001,//状态码
  "msg": ""//消息
   , "data":  {
    list: [ {
    create_time:""  , //创建时间
    digest_type:""  , //摘要类型,1:商家摘要,2:社区摘要
    email:""  , //联系邮箱
    id:""  , //主键id
    last_active_ip:""  , //
    last_active_time:""  , //
    last_upd_time:""  , //最后更新时间
    mch_draft_id:""  , //所属商户草稿
    mch_id:""  , //所属商户ID
    mobile:""  , //联系手机
    nickname:""  , //用户昵称, 姓名
    outer_id:""  , //提供给外部应用的USERID
    parent_id:""  , //父账号, 0: 主账号, 其它: 某主账号下的子账号, 默认为0
    reg_progress:""  , //注册完成进度, MCH_INFO :待完成商家资料, BINDING_MOBILE_OR_MAIL :待绑定手机或邮箱, HOME: 已完成, 待渠道审批: WAIT_AUDIT,  待签订协议: WAIT_SIGN_CONTACT
    remark:""  , //备注
    role_id:""  , //角色ID, 主账号默认为超级权限, 子账号一定要关联角色, 默认为0
    role_name:""  , //
    status:""  , //账号状态, 1:正常, 2:冻结
    tel:""  , //联系电话
    uname:""   //用户账号
}  ]  , 
    page_limit:""  , //
    page_num:""  , //
    total:""  , //
    total_page:""   //
}  
}
```

