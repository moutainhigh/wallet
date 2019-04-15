package com.rfchina.wallet.server.web;

import com.alibaba.fastjson.JSON;
import com.rfchina.platform.common.misc.ResponseCode.EnumResponseCode;
import com.rfchina.platform.common.misc.ResponseValue;
import com.rfchina.wallet.server.bank.pudong.domain.request.PayReq;
import com.rfchina.wallet.server.bank.pudong.domain.response.PayRespBody;
import com.rfchina.wallet.server.bank.pudong.domain.util.UrlConstant;
import com.rfchina.wallet.server.msic.EnumWallet.RemitLocation;
import com.rfchina.wallet.server.msic.EnumWallet.SysFlag;
import com.rfchina.wallet.server.service.DirectBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class JuniorWalletController {

	@Autowired
	private DirectBankService directBankService;

	@ApiOperation("初级钱包-思力出钱")
	@PostMapping(UrlConstant.JUNIOR_WALLET_SL_PAY)
	public ResponseValue<PayRespBody> slPay(
		@ApiParam(value = "电子凭证号(业务方定义唯一)", required = true, example = "123") @RequestParam("elec_cheque_no") String elecChequeNo,
		@ApiParam(value = "钱包ID", required = true, example = "1") @RequestParam("wallet_id") String payeeAcctNo,
		@ApiParam(value = "支付金额(单位分)", required = true, example = "1") @RequestParam("amount") String amount,
		@ApiParam(value = "附言", required = false) @RequestParam(value = "note", required = false) String note,
		@ApiParam(value = "支付用途 收款人为个人客户时必须输入 1-工资、奖金收入 2-稿费、演出费等劳务费用 3-债券、期货、信托等投资的本金和收益 4-个人债权或产权转让收益 5-个人贷款转存 6-证券交易结算资金和期货交易保证金 7-集成、赠予款项 8-保险理赔、保费退换等款项 9-纳税退还 A-农、副、矿产品销售收入", required = false)
		@RequestParam(value = "pay_purpose", required = false) String payPurpose
	) {
//		BigDecimal bigAmount = new BigDecimal(amount).divide(BigDecimal.TEN)
//			.setScale(2, BigDecimal.ROUND_DOWN);
//		PayReq payReq = PayReq.builder()
//			.elecChequeNo(elecChequeNo)
//			.acctNo("95200078801300000003")
//			.acctName("浦发2000040752")
//			.payeeAcctNo(payeeAcctNo)
//			.payeeName(payeeName)
//			.amount(bigAmount.toString())
//			.sysFlag(SysFlag.SELF.getValue())
//			.remitLocation(RemitLocation.SELF.getValue())
//			.note(note)
//			.build();
//		PayRespBody respBody = directBankService.toPay(Arrays.asList(payReq));
		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS,
			PayRespBody.builder().acceptNo("PT19YQ0000018904").failCount("0").successCount("1")
				.seqNo("020189583321").build());
	}

	@ApiOperation("初级钱包-思力批量出钱（最多20笔）")
	@PostMapping(UrlConstant.JUNIOR_WALLET_SL_BATCH_PAY)
	public ResponseValue<PayRespBody> slBatchPay(
		@ApiParam(value = "json数组，参考思力出钱单笔接口，拼装成数组即可", required = true) String jsonArry) {
//		List<PayReq> payReqs = JSON.parseArray(jsonArry, PayReq.class);
//		payReqs = payReqs.stream().map(payReq -> {
//			payReq.setAcctNo("95200078801300000003");
//			payReq.setAcctName("浦发2000040752");
//			payReq.setSysFlag(SysFlag.SELF.getValue());
//			payReq.setRemitLocation(RemitLocation.SELF.getValue());
//
//			// 必须注意，分转换为0.00元
//			BigDecimal bigAmount = new BigDecimal(payReq.getAmount()).divide(BigDecimal.TEN)
//				.setScale(2, BigDecimal.ROUND_DOWN);
//			payReq.setAmount(bigAmount.toString());
//
//			return payReq;
//		}).collect(Collectors.toList());
//
//		PayRespBody respBody = directBankService.toPay(payReqs);

		return new ResponseValue<>(EnumResponseCode.COMMON_SUCCESS, null);
	}

}
