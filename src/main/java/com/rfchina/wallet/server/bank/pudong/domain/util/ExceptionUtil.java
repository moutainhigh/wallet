package com.rfchina.wallet.server.bank.pudong.domain.util;

import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.UnknownError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.UserInfoError;
import java.util.function.Predicate;

public class ExceptionUtil {

	public static IGatewayError explain(Exception e, Predicate isUser) {
		if (e instanceof GatewayError) {

			IGatewayError err = (IGatewayError) e;
			if (isUser != null && isUser.test(err.getErrCode())) {
				UserInfoError result = new UserInfoError(e);
				result.setErrCode(err.getErrCode());
				result.setErrMsg(err.getErrMsg());
				return result;
			}
			return err;
		} else {
			return new UnknownError(e);
		}
	}
}
