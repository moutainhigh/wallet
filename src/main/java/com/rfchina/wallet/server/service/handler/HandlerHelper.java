package com.rfchina.wallet.server.service.handler;

import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandlerHelper {
	@Autowired
	private HandlerAQ52 handlerAQ52;

	@Autowired
	private Handler8800 handler8800;

	private PuDongHandler rootHandler;

	@PostConstruct
	public void init() {

		rootHandler = handlerAQ52;
		handlerAQ52.setNext(handler8800);
	}

	public PuDongHandler selectByWalletType(Byte walletType){
		PuDongHandler currHandler = rootHandler;

		while (currHandler != null) {
			if (currHandler.isSupportWalletType(walletType)) {
				return currHandler;
			}
			currHandler = currHandler.getNext();
		}

		throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_HANDLER_NOT_FOUND,"type = "+ walletType);
	}

	public PuDongHandler selectByMethod(Byte refMethod) {
		PuDongHandler currHandler = rootHandler;

		while (currHandler != null) {
			if (currHandler.isSupportMethod(refMethod)) {
				return currHandler;
			}
			currHandler = currHandler.getNext();
		}

		throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_METHOD_NOT_FOUND);
	}
}
