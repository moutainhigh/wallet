package com.rfchina.wallet.server.bank.pudong.domain.util;

import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.UnknownError;
import com.rfchina.wallet.server.bank.pudong.domain.exception.UserInfoError;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class ExceptionUtil {

	private static PredicatePatterns errCodePatterns = new PredicatePatterns();

	public static IGatewayError explain(Exception e, Predicate<IGatewayError> isUser) {
		if (e instanceof GatewayError) {

			IGatewayError err = (IGatewayError) e;
			if (isUser != null && isUser.test(err)) {
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


	public static String extractErrCode(String note) {

		if (StringUtils.isNotBlank(note)) {
			for (Pattern pattern : errCodePatterns) {
				Matcher matcher = pattern.matcher(note);
				if (matcher.matches()) {
					return matcher.group(1);
				}
			}
		}
		return null;
	}

	public static class PredicatePatterns extends ArrayList<Pattern> {

		public PredicatePatterns() {
			super(2);
		add(Pattern.compile(".+错误原因:(\\w+).+"));
		}
	}
}
