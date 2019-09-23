package com.rfchina.wallet.server.service.handler.yunst;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.platform.common.utils.DateUtil;
import com.rfchina.platform.common.utils.EnumUtil;
import com.rfchina.wallet.domain.mapper.ext.WalletApplyDao;
import com.rfchina.wallet.domain.misc.EnumDef.EnumWalletLevel;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.yunst.request.GetOrderDetailReq;
import com.rfchina.wallet.server.bank.yunst.request.YunstQueryBalanceReq;
import com.rfchina.wallet.server.bank.yunst.response.GetOrderDetailResp;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstQueryBalanceResult;
import com.rfchina.wallet.server.bank.yunst.util.CommonGatewayException;
import com.rfchina.wallet.server.bank.yunst.util.YunstTpl;
import com.rfchina.wallet.server.mapper.ext.WalletApplyExtDao;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import com.rfchina.wallet.server.msic.EnumWallet.WalletApplyStatus;
import com.rfchina.wallet.server.msic.EnumWallet.YunstOrderStatus;
import com.rfchina.wallet.server.service.GatewayTransService;
import com.rfchina.wallet.server.service.handler.common.EBankHandler;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YunstBizHandler  extends YunstBaseHandler implements EBankHandler  {

	@Autowired
	private YunstTpl yunstTpl;

	@Autowired
	private WalletApplyExtDao walletApplyDao;

	@Autowired
	private GatewayTransService gatewayTransService;

	@Override
	public boolean isSupportWalletLevel(Byte walletType) {
		return EnumWalletLevel.SENIOR.getValue().byteValue() == walletType;
	}


	@Override
	public boolean isSupportMethod(Byte method) {
		return false;
	}

	@Override
	public GatewayMethod getGatewayMethod() {
		return null;
	}

	@Override
	public Tuple<GatewayMethod, PayTuple> pay(List<WalletApply> payInReqs) throws Exception {
		return null;
	}

	@Override
	public List<Tuple<WalletApply, GatewayTrans>> updatePayStatus(
		List<Tuple<WalletApply, GatewayTrans>> applyTuples) {

		applyTuples.forEach(tuple -> {
			WalletApply walletApply = tuple.left;
			GatewayTrans trans = tuple.right;
			// 查询订单状态
			GetOrderDetailReq req = GetOrderDetailReq.builder().bizOrderNo(trans.getAcceptNo())
				.build();
			trans.setStage(req.getServcieName() + "." + req.getMethodName());
			try {
				GetOrderDetailResp resp = yunstTpl.execute(req, GetOrderDetailResp.class);
				YunstOrderStatus yunstStatus = EnumUtil
					.parse(YunstOrderStatus.class, resp.getOrderStatus());
				WalletApplyStatus status = yunstStatus.toApplyStatus();
				walletApply.setStatus(status.getValue());
				Date bizTime = DateUtil
					.parse(resp.getPayDatetime(), DateUtil.STANDARD_DTAETIME_PATTERN);
				walletApply.setBizTime(bizTime);
				walletApplyDao.updateByPrimaryKey(walletApply);

				trans.setBizTime(bizTime);
				trans.setEndTime(new Date());
				gatewayTransService.updateTrans(trans);
			} catch (CommonGatewayException e) {
				walletApply.setStatus(WalletApplyStatus.WAIT_DEAL.getValue());
				walletApplyDao.updateByPrimaryKey(walletApply);

				trans.setErrCode(e.getBankErrCode());
				trans.setSysErrMsg(e.getBankErrMsg());
				gatewayTransService.updateTrans(trans);

				log.info("", e);
			} catch (Exception e) {
				log.error("", e);
			}

		});
		return null;
	}

	@Override
	public PayStatusResp onAskErr(WalletApply walletLog, IGatewayError err) {
		return null;
	}

	/**
	 * 查询余额
	 */
	public YunstQueryBalanceResult queryBalance(Long walletId, Byte source, String accountSetNo) throws Exception {
		String bizUserId = transferToYunstBizUserFormat(walletId, source);
		YunstQueryBalanceReq req = YunstQueryBalanceReq.builder$()
			.bizUserId(bizUserId)
			.accountSetNo(accountSetNo)
			.build();

		return yunstTpl.execute(req, YunstQueryBalanceResult.class);
	}

}
