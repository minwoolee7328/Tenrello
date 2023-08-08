package com.example.tenrello.security.redis;

import com.example.tenrello.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue()
                .set(username, refreshToken, JwtUtil.REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

}
