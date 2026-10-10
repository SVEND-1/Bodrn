package org.example.bodrn.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginRateLimiter {

    private static final int MAX_ATTEMPTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(15);

    private final Cache<String, AtomicInteger> byIp = Caffeine.newBuilder()
            .expireAfterWrite(WINDOW)
            .maximumSize(100_000)
            .build();

    private final Cache<String, AtomicInteger> byUser = Caffeine.newBuilder()
            .expireAfterWrite(WINDOW)
            .maximumSize(100_000)
            .build();

    public boolean tryConsume(String ip, String username) {
        return inc(byIp, "ip:" + ip) <= MAX_ATTEMPTS
                && inc(byUser, "user:" + username.toLowerCase()) <= MAX_ATTEMPTS;
    }

    public void reset(String ip, String username) {
        byIp.invalidate("ip:" + ip);
        byUser.invalidate("user:" + username.toLowerCase());
    }

    private int inc(Cache<String, AtomicInteger> cache, String key) {
        return cache.get(key, k -> new AtomicInteger(0)).incrementAndGet();
    }
}