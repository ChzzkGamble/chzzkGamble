package com.chzzkGamble.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager recentlyCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.SECONDS)
                        .maximumSize(20)
                        .removalListener(((key, value, cause) -> log.info("캐시가 제거되었습니다. key : {}", key)))
        );

        return cacheManager;
    }

    @Bean
    public CacheManager rankingCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .removalListener(((key, value, cause) -> log.info("캐시가 제거되었습니다. key : {}", key)))
        );

        return cacheManager;
    }
}
