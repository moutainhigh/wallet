package com.rfchina.wallet.server.service.handler.common;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import com.rfchina.wallet.server.service.handler.pudong.Handler8800;
import com.rfchina.wallet.server.service.handler.yunst.YunstBizHandler;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理器助手
 *
 * @author nzm
 */
@Component
public class HandlerHelper {

	@Autowired
	private Handler8800 handler8800;

	@Autowired
	private YunstBizHandler yunstBizHandler;

	private EBankHandler rootHandler;

	@PostConstruct
	public void init() {
		rootHandler = handler8800;
		handler8800.setNext(yunstBizHandler);
	}

	public EBankHandler selectByWalletLevel(Byte walletLevel) {
		EBankHandler currHandler = rootHandler;

		while (currHandler != null) {
			if (currHandler.isSupportWalletLevel(walletLevel)) {
				return currHandler;
			}
			currHandler = currHandler.getNext();
		}

		throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_HANDLER_NOT_FOUND,
			"type = " + walletLevel);
	}

	public EBankHandler selectByTunnelType(Byte tunnelType) {
		EBankHandler currHandler = rootHandler;

		while (currHandler != null) {
			if (currHandler.isSupportWalletLevel(tunnelType)) {
				return currHandler;
			}
			currHandler = currHandler.getNext();
		}

		throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_HANDLER_NOT_FOUND,
			"type = " + tunnelType);
	}

}
