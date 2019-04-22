SELECT * from rf_bank_code
WHERE id = 73121
WHERE bank_name LIKE '%浦东发展银行%'

-- 受理中的流水
select accept_no,ref_method, create_time
from rf_wallet_log 
where STATUS = 1

-- 更新流水状态
update rf_wallet_log set err_msg = '' , STATUS = 3
where accept_no = 'PT19YQ0000022441' and elec_cheque_no = 'Eno2019042253666' and STATUS = 2