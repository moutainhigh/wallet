package com.rfchina.wallet.server.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * @author nzm
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Bean(value = "redisTemplate")
	public RedisTemplate<Object, Object> jsonRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		RedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		return redisTemplate;
	}
}
