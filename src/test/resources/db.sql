drop table if exists rf_wallet_user;
CREATE TABLE `rf_wallet_user` (
  `user_id`  bigint NOT NULL COMMENT '用户ID',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '关联的钱包ID',
  `mobile` varchar(15) NOT NULL COMMENT '登录手机号',
  `register_progress` tinyint(2) DEFAULT 0 COMMENT '注册进度, 1:已通过身份验证, 2:已绑定银行卡',
  `status` tinyint(2) DEFAULT NULL COMMENT '帐号状态: 1:正常，2：禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '钱包信息最后更新日期',
  PRIMARY KEY (`user_id`)
);


drop table if exists rf_wallet;
CREATE TABLE `rf_wallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包ID',
  `type` tinyint(4) DEFAULT NULL COMMENT '钱包类型， 1：企业钱包，2：个人钱包',
  `title` varchar(100) DEFAULT NULL COMMENT '钱包标题，通常是姓名或公司名',
  `wallet_balance` bigint(20) DEFAULT 0 COMMENT '钱包余额',
  `recharge_amount` bigint(20) DEFAULT 0 COMMENT '累计充值金额',
  `recharge_count` int(11) DEFAULT 0 COMMENT '累计充值次数',
  `pay_amount` bigint(20) DEFAULT 0 COMMENT '累计支付金额',
  `pay_count` int(11) DEFAULT 0 COMMENT '累计支付次数',
  `status` tinyint(2) DEFAULT NULL COMMENT '钱包状态: 1:待审核，2：激活,3：禁用',
  `audit_type` tinyint(4) DEFAULT NULL COMMENT '审核方式，1：运营，2：通联',
  `level` TINYINT(2) NULL DEFAULT 1 COMMENT '钱包等级，1： 初级钱包，2： 高级钱包',
  `source` TINYINT(2) NULL DEFAULT 3 COMMENT '钱包来源，1： 富慧通-企业商家，2： 富慧通-个人商家，3： 用户',
  `balance_upd_time` datetime DEFAULT NULL COMMENT '钱包余额最后更新日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '钱包信息最后更新日期',
  PRIMARY KEY (`id`)
);


drop table if exists rf_wallet_company;
CREATE TABLE `rf_wallet_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '关联的钱包ID',
  `company_name` varchar(64) DEFAULT NULL COMMENT '公司名称',
  `tel` varchar(16) DEFAULT NULL COMMENT '公司电话',
  `email` varchar(32) DEFAULT NULL COMMENT '公司邮箱',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '钱包信息最后更新日期',
  PRIMARY KEY (`id`)
);


drop table if exists rf_wallet_person;
CREATE TABLE `rf_wallet_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '关联的钱包ID',
  `name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `id_type` tinyint(2) DEFAULT NULL COMMENT '证件类型，1:身份证',
  `id_no` varchar(64) DEFAULT NULL COMMENT '证件号',
  `tel` varchar(15) DEFAULT NULL COMMENT '电话',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '钱包信息最后更新日期',
  PRIMARY KEY (`id`)
);


drop table if exists rf_wallet_card;
CREATE TABLE `rf_wallet_card` (
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '钱包ID',
  `bank_code` varchar(32) NOT NULL COMMENT '银行代码',
  `bank_name` varchar(256) DEFAULT NULL COMMENT '银行名称',
  `bank_account` varchar(32) NOT NULL COMMENT '银行账号',
  `deposit_bank` varchar(256) DEFAULT NULL COMMENT '开户行',
  `deposit_name` varchar(128) DEFAULT NULL COMMENT '开户名',
  `status` tinyint(2) DEFAULT NULL COMMENT '绑定状态: 1:已绑定，2：已解绑',
  `is_def` tinyint(2) DEFAULT NULL COMMENT '是否默认银行卡: 1:是，2：否',
  `is_public` tinyint(2) DEFAULT NULL COMMENT '是否对公账户: 1:是，2：否',
  `telephone` varchar(15) DEFAULT NULL COMMENT '预留手机号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '钱包信息最后更新日期',
  PRIMARY KEY (`wallet_id`, `bank_code`, `bank_account`)
);

drop table if exists rf_wallet_log;
CREATE TABLE `rf_wallet_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `wallet_id` bigint(20) DEFAULT NULL COMMENT '钱包ID',
  `type` tinyint(2) DEFAULT NULL COMMENT '流水类型，1：直接转帐，2：收入，3：支出',
  elec_cheque_no varchar(64) DEFAULT NULL COMMENT '电子凭证号',
  accept_no varchar(64) DEFAULT NULL COMMENT '受理编号',
  payer_account varchar(64) DEFAULT NULL COMMENT '付款方帐号',
  `payee_account` varchar(64) DEFAULT NULL COMMENT '收款方帐号',
  `payee_type` tinyint(2) DEFAULT NULL COMMENT '收款账户类型，1：对公账户，2：个人账户',
  `amount` bigint(20) DEFAULT NULL COMMENT '流水金额',
  status  tinyint comment '交易状态。 1: 待发送银行网关，2：银行受理中，3：交易成功，4：交易失败，5：撤销',
  err_msg varchar(250) comment '错误信息',
  remark varchar(250) comment '备注',
  `ref_method` tinyint(2) DEFAULT NULL COMMENT '关联接口方法，1：银企直连AQ52，2：银企直连8800',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`)
);

drop table if exists rf_bank_code;
create table rf_bank_code(
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   bank_code varchar(12) comment '银行行号',
   bank_name varchar(64) comment '银行名称',
   class_code varchar(8) comment '所属分类代码',
   class_name varchar(64) comment '所属分类行名',
   area_code  varchar(8) comment '所属地区',
   city_name  varchar(64) comment '所属省市',
   province_name varchar(64) comment '所属地区',
   PRIMARY key(`id`)
) ;

drop table if exists rf_app_domain;
CREATE TABLE `rf_app_domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `app_id` varchar(32) NOT NULL COMMENT '应用id',
  `legal_domain` varchar(256) NOT NULL COMMENT '合法域名',
  `status` tinyint(2) NOT NULL COMMENT '状态, 1:正常, 2:禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `last_upd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ;