package com.rfchina.wallet.server.bank.pudong.domain.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GatewayErrPredicate implements Predicate<IGatewayError> {

	private Set<String> exactCodeSet = new HashSet<>(20);

	@Override
	public boolean test(IGatewayError e) {
		return exactCodeSet.contains(e.getErrCode());
	}

	public void add(String code){
		exactCodeSet.add(code);
	}

	public void addAll(List<String> codes){
		exactCodeSet.addAll(codes);
	}
}
