package com.example.tenrello.security.redis;

import com.example.tenrello.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    // Username - RefreshToken 형태로 저장
    private final StringRedisTemplate redisTemplate;
    // AccessToken - logout 형태로 저장
    private final StringRedisTemplate redisBlackListTemplate;

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void saveRefreshToken(String username, String refreshToken) {
        // ExpirationTime 설정을 통해 자동 삭제 처리
        redisTemplate.opsForValue()
                .set(username, refreshToken, JwtUtil.REFRESH_TOKEN_TIME, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(username);
    }

    public void addBlackList(String accessToken, Long remainingTime) {
        redisBlackListTemplate.opsForValue()
                .set(accessToken, "logout", remainingTime, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackList(String accessToken) {
        return redisBlackListTemplate.opsForValue().get(accessToken) != null;
    }

}
