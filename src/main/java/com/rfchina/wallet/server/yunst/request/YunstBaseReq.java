package com.rfchina.wallet.server.yunst.request;

import com.allinpay.yunst.sdk.YunClient;
import com.allinpay.yunst.sdk.bean.YunRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rfchina.platform.common.utils.JsonUtil;
import com.rfchina.wallet.server.yunst.response.YunstBaseResp;

import java.lang.reflect.Field;
import java.util.Objects;

public abstract class YunstBaseReq implements YunstBase{
	public YunstBaseResp execute() throws Exception {
		YunRequest request = new YunRequest(this.getServcieName(), this.getMethodName());
		Class<?> clazz = this.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object value = field.get(this);
			System.out.println(String.format("key:%s,value:%s",fieldName,value));
			if (Objects.nonNull(value)){
				request.put(fieldName, value);
			}
		}

		return JsonUtil.toObject(YunClient.request(request), YunstBaseResp.class,objectMapper -> {objectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);});
	}

}
