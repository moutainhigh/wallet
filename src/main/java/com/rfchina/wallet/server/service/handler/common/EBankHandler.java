package com.rfchina.wallet.server.service.handler.common;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
import com.rfchina.wallet.domain.model.WalletClearing;
import com.rfchina.wallet.domain.model.WalletCollect;
import com.rfchina.wallet.domain.model.WalletRecharge;
import com.rfchina.wallet.domain.model.WalletRefund;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.model.ext.PayStatusResp;
import com.rfchina.wallet.server.model.ext.PayTuple;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.List;


/**
 * 处理器接口
 *
 * @author nzm
 */
public abstract class EBankHandler {

	public abstract boolean isSupportWalletLevel(Byte walletLevel);

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
	public abstract List<WalletCollect> collect(Long applyId);

	/**
	 * 代付
	 */
	public abstract List<WalletClearing> agentPay(Long applyId);

	/**
	 * 退款
	 * @return
	 */
	public abstract List<WalletRefund> refund(Long collectId);

	/**
	 * 充值
	 * @return
	 */
	public abstract List<WalletRecharge> recharge(Long applyId);


}
