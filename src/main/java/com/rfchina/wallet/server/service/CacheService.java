package com.rfchina.wallet.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 缓存名称
	 */
	private final static String CACHE_NAME_WALLET = "platform:wallet:super";

	/**
	 * 验证码令牌
	 */
	private final static String CACHE_KEY_WALLET_VERIFY_TOKEN = CACHE_NAME_WALLET + ":verify_token";

	/**
	 * 回跳的应用信息
	 */
	private final static String CACHE_KEY_WALLET_APP_INFO = CACHE_NAME_WALLET + ":register_key";

	/**
	 * 回调地址
	 */
	private final static String CACHE_KEY_WALLET_CALLBACK = CACHE_NAME_WALLET + ":callback";

	private final static String CACHE_KEY_WALLET_VERIFY_COUNT =  CACHE_NAME_WALLET + ":verify";

	/**
	 * set调用发送短信验证码返回的token
	 * @param type		验证码类型
	 * @param mobile	需要发送验证码的手机
	 * @param token		对应当次验证码唯一令牌
	 */
	@SuppressWarnings("unchecked")
	public void setVerifyCodeToken(Integer type, String mobile, String token) {
		String key = CACHE_KEY_WALLET_VERIFY_TOKEN + ":type=" + type + ":mobile=" + mobile;
		redisTemplate.boundValueOps(key).set(token, 2, TimeUnit.MINUTES);
	}

	/**
	 * get调用发送短信验证码返回的token
	 * @param type
	 * @param mobile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getVerifyCodeToken(Integer type, String mobile) {
		String key = CACHE_KEY_WALLET_VERIFY_TOKEN + ":type=" + type + ":mobile=" + mobile;
		if (redisTemplate.hasKey(key)) {
			return Objects.requireNonNull(redisTemplate.boundValueOps(key).get()).toString();
		} else {
			return null;
		}
	}
}
