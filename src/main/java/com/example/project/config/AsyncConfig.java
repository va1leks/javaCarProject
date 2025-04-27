package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // минимальное количество потоков
        executor.setMaxPoolSize(10);  // максимальное количество потоков
        executor.setQueueCapacity(25);  // максимальная длина очереди задач
        executor.setThreadNamePrefix("async-");  // префикс имени потока
        executor.initialize();
        return executor;
    }
}