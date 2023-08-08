package com.example.tenrello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    // Redis 저장소와 연결
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /***
     * Redis 서버와 통신
     * StringRedisTemplate를 사용하여 Key, value를 모두 문자열로 저장
     */
    @Bean
    public StringRedisTemplate redisTemplate() {
        final StringRedisTemplate redisTemplate = new StringRedisTemplate();

        // RedisTemplate을 사용할 때 Spring-Redis 간 데이터 직렬화/역직렬화 시 사용하는 방식이 jdk 지결ㄹ화 방식
        // 동작에는 문제가 없지만 redis-cli를 통해 데이터를 확인할 때 알아볼 수 없는 형태로 출력되기 때문
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // 필요 없으면 지울 부분
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }
}

