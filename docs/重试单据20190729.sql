## 后台重发单据 ST1201907240825000000068 ST1201907240825000000194
begin;
update rf_wallet_apply set status = 1 ,curr_trans_id = null,create_time = now() where biz_no in ('ST1201907240825000000068','ST1201907240825000000194');
commit;