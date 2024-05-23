package com.ssafy.trip.domain.auth.repository;

import com.ssafy.trip.core.cache.CacheKey;
import com.ssafy.trip.core.properties.AuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AuthCacheRepository {
    public static final int EXPIRE_MINUTES = 5;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthProperties authProperties;

    public void saveFindPassword(String email, String value) {
        String key = CacheKey.findPasswordCodeKey(email);
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(EXPIRE_MINUTES));
    }

    public void saveConfirm(String email, String value) {
        String key = CacheKey.emailConfirmCodeKey(email);
        redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(EXPIRE_MINUTES));
    }

    public void saveTokenValidate(String username, Map<String, String> value) {
        String key = CacheKey.authenticationKey(username);
        redisTemplate.opsForHash().putAll(key, value);
        redisTemplate.expire(key, Duration.ofMillis(authProperties.getRefreshTokenExpiry()));
    }

    public boolean existsConfirmKey(String email) {
        String key = CacheKey.emailConfirmCodeKey(email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean existsFindPasswordKey(String email) {
        String key = CacheKey.findPasswordCodeKey(email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public String findPassword(String email) {
        String key = CacheKey.findPasswordCodeKey(email);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteFindPassword(String email) {
        String key = CacheKey.findPasswordCodeKey(email);
        redisTemplate.delete(key);
    }

    public String confirm(String email) {
        String key = CacheKey.emailConfirmCodeKey(email);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteConfirm(String email) {
        String key = CacheKey.emailConfirmCodeKey(email);
        redisTemplate.delete(key);
    }

    public Object findRefreshKeyInTokenValidate(String username) {
        String key = CacheKey.authenticationKey(username);
        return redisTemplate.opsForHash().get(key, "refreshToken");
    }
}
