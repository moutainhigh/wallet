package com.rfchina.wallet.server.service.handler.common;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletOrder;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.domain.model.WalletRefundDetail;
import com.rfchina.wallet.domain.model.WalletWithdraw;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.model.ext.RechargeResp;
import com.rfchina.wallet.server.model.ext.WalletCollectResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.List;


/**
 * 处理器接口
 *
 * @author nzm
 */
public abstract class EBankHandler {

	public abstract boolean isSupportWalletLevel(Byte walletLevel);

	public abstract boolean isSupportTunnelType(Byte tunnelType);

	public abstract GatewayMethod getGatewayMethod();

	public abstract PayStatusResp onAskErr(WalletApply walletLog, IGatewayError err);

	private EBankHandler next;

	public EBankHandler getNext() {
		return next;
	}

	public void setNext(EBankHandler handler) {
		this.next = handler;
	}

	/**
	 * 查询订单结果
	 */
	public abstract Tuple<WalletApply, GatewayTrans> updatePayStatus(
		Tuple<WalletApply, GatewayTrans> applyTuple);

	/**
	 * 转账
	 */
	public abstract Tuple<GatewayMethod, PayTuple> transfer(Long applyId);

	/**
	 * 代收
	 */
	public abstract WalletCollectResp collect(WalletOrder order, WalletCollect collect);

	/**
	 * 代付
	 */
	public abstract void agentPay(WalletOrder order, List<WalletClearing> clearings);

	/**
	 * 退款
	 */
	public abstract void refund(WalletOrder order, WalletRefund refund, List<WalletRefundDetail> details);

	/**
	 * 充值
	 */
	public abstract RechargeResp recharge(WalletOrder order, WalletRecharge recharge);

	/**
	 * 提现
	 */
	public abstract WalletWithdraw withdraw(WalletOrder order, WalletWithdraw withdraw);


}
