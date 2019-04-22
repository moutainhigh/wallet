package com.rfchina.wallet.server.web;

import com.alibaba.fastjson.JSON;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.bank.pudong.domain.response.PubPayRespBody;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.UrlConstant;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.service.JuniorWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class JuniorWalletController {

	@Autowired
	private JuniorWalletService juniorWalletService;

	@ApiOperation("初级钱包-思力出钱")
	@PostMapping(UrlConstant.JUNIOR_WALLET_PAY_IN)
	public ResponseValue<PayInResp> payIn(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "钱包ID", required = true, example = "1") @RequestParam("wallet_id") Long walletId,
		@ApiParam(value = "支付金额(单位分)", required = true, example = "1") @RequestParam("amount") Long amount,
		@ApiParam(value = "电子凭证号(业务方定义唯一)", required = true, example = "123") @RequestParam("elec_cheque_no") String elecChequeNo,
		@ApiParam(value = "附言", required = false) @RequestParam(value = "note", required = false) String note,
		@ApiParam(value = "支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入", required = false)
		@RequestParam(value = "pay_purpose", required = false) Byte payPurpose
	) {

		PayInReq payInReq = PayInReq.builder()
			.walletId(walletId)
			.amount(amount)
			.elecChequeNo(elecChequeNo)
			.note(note)
			.payPurpose(payPurpose)
			.build();

		PayInResp respBody = juniorWalletService.payIn(Arrays.asList(payInReq));
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, respBody);
	}

	@ApiOperation("初级钱包-思力批量出钱（最多20笔）")
	@PostMapping(UrlConstant.JUNIOR_WALLET_BATCH_PAY_IN)
	public ResponseValue<PayInResp> batchPayIn(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "json数组，参考思力出钱单笔接口，拼装成数组即可( 钱包类型必须统一为企业或个人 )", required = true) String jsonArry
	) {
		List<PayInReq> payReqs = JSON.parseArray(jsonArry, PayInReq.class);

		PayInResp respBody = juniorWalletService.payIn(payReqs);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, respBody);
	}

	@ApiOperation("初级钱包-查询支付状态")
	@PostMapping(UrlConstant.JUNIOR_WALLET_QUERY)
	public ResponseValue<List<PayStatusResp>> query(
		@RequestParam("access_token") String accessToken,
		@ApiParam(value = "电子凭证号(业务方定义唯一)", required = false, example = "123") @RequestParam("elec_cheque_no") String elecChequeNo,
		@ApiParam(value = "受理编号", required = false) @RequestParam("accept_no") String acceptNo
	) {
		List<PayStatusResp> resp = juniorWalletService.query(elecChequeNo, acceptNo);
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, resp);
	}

}
