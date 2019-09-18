package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.wallet.server.bank.yunst.request.YunstQueryBalanceReq;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Import(YunstTpl.class)
public class YunstBizHandler extends YunstBaseHandler {
	@Autowired
	private YunstTpl yunstTpl;

	/**
	 * 查询余额
	 */
	public YunstQueryBalanceResult queryBalance(String bizUserId, Integer type, String accountSetNo) throws Exception {
		bizUserId = transferToYunstBizUserFormat(bizUserId, type);
		YunstQueryBalanceReq req = YunstQueryBalanceReq.builder$()
				.bizUserId(bizUserId)
				.accountSetNo(accountSetNo)
				.build();

		return yunstTpl.execute(req, YunstQueryBalanceResult.class);
	}
}
