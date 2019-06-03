package com.rfchina.wallet.server.bank.pudong.domain.predicate;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.domain.exception.IGatewayError;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ExactErrPredicate implements Predicate<IGatewayError> {

	private Map<String, String> exactCodeSet = new HashMap<>(20);

	@Override
	public boolean test(IGatewayError err) {

		if (err == null || StringUtils.isBlank(err.getErrCode())) {
			return false;
		}

		boolean isOk = exactCodeSet.containsKey(err.getErrCode());
		String value = exactCodeSet.get(err.getErrCode());
		if (isOk && StringUtils.isNotBlank(value)) {
			isOk = err.getErrMsg().contains(value);
		}
		return isOk;
	}

	public void add(String code, String word) {
		exactCodeSet.put(code, word);
	}

	public void parseText(String conf) {
		Map map = JSON.parseObject(conf, Map.class);
		this.exactCodeSet.clear();
		map.forEach((key, value) -> {
			add((String) key, value != null ? (String) value : null);
		});
	}

}
