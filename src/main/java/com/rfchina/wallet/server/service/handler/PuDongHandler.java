package com.rfchina.wallet.server.service.handler;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.server.model.ext.PayInReq;
import com.rfchina.wallet.server.model.ext.PayInResp;
import com.rfchina.wallet.server.msic.EnumWallet.GatewayMethod;
import java.util.Date;
import java.util.List;

public interface PuDongHandler {

	boolean isSupportWalletType(Byte walletType);

	boolean isSupportMethod(Byte method);

	GatewayMethod getGatewayMethod();

	Tuple<GatewayMethod, PayInResp> pay(List<PayInReq> payInReqs) throws Exception;

	int updatePayStatus(String acceptNo, Date createTime);

	PuDongHandler getNext();

}
