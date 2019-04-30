package com.rfchina.wallet.server.bank.pudong.builder;

import okhttp3.OkHttpClient;

/**
 * 浦发网关执行接口
 *
 * @author nzm
 */
public interface GatewayLancher<T> {

	T lanch(OkHttpClient client) throws Exception;
}
