package com.rfchina.wallet.server.bank.pudong.Builder;

import okhttp3.OkHttpClient;

public interface GatewayLancher<T> {

	T lanch(OkHttpClient client) throws Exception;
}
