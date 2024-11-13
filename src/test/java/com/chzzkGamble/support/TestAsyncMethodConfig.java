package com.chzzkGamble.support;

import java.util.concurrent.Executor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/*
비동기 메소드를 검증하기 위해 필요한 설정
 */
@TestConfiguration
@EnableAsync
public class TestAsyncMethodConfig implements AsyncConfigurer {

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        return new SyncTaskExecutor(); // 비동기 메소드를 동기화하여 실행
    }
}
