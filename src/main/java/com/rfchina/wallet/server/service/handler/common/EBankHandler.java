package com.rfchina.wallet.server.service.handler.common;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.GatewayTrans;
import com.rfchina.wallet.domain.model.WalletApply;
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
public interface EBankHandler {

	boolean isSupportWalletLevel(Byte walletType);

	boolean isSupportMethod(Byte method);

	GatewayMethod getGatewayMethod();

	Tuple<GatewayMethod, PayTuple> pay(List<WalletApply> payInReqs) throws Exception;

	List<Tuple<WalletApply, GatewayTrans>> updatePayStatus(
		List<Tuple<WalletApply, GatewayTrans>> applyTuples);

	PayStatusResp onAskErr(WalletApply walletLog, IGatewayError err);

	EBankHandler next = null;

	default EBankHandler getNext() {
		return next;
	}
}
