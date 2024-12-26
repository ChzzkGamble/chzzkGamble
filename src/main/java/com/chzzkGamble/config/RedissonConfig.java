package com.chzzkGamble.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final String REDIS_HOST_PREFIX = "redis://";

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    public RedissonClient RedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDIS_HOST_PREFIX + host + ":" + port);
        return Redisson.create(config);
    }
}
