package com.rfchina.wallet.server.service.handler;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.domain.model.WalletLog;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.Date;
import java.util.List;


/**
 * 处理器接口
 *
 * @author nzm
 */
public interface EBankHandler {

	boolean isSupportWalletType(Byte walletType);

	boolean isSupportMethod(Byte method);

	GatewayMethod getGatewayMethod();

	Tuple<GatewayMethod, PayInResp> pay(List<WalletLog> payInReqs) throws Exception;

	List<WalletLog> updatePayStatus(String acceptNo, Date createTime);

	EBankHandler getNext();

	void onAskErr(WalletLog walletLog, IGatewayError err);
}
