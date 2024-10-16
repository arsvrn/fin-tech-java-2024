package com.tbank.edu.hw9.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ExecutorConfig {

    @Value("${executor.fixed.thread-pool-size}")
    private int fixedThreadPoolSize;

    @Bean(name = "dataInitializerExecutor")
    public ExecutorService dataInitializerExecutor() {
        return Executors.newFixedThreadPool(fixedThreadPoolSize, new CustomThreadFactory("DataInitializerThread"));
    }

    @Bean(name = "scheduledTaskExecutor")
    public ScheduledExecutorService scheduledTaskExecutor() {
        return Executors.newScheduledThreadPool(1, new CustomThreadFactory("ScheduledTaskThread"));
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String threadNamePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public CustomThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, threadNamePrefix + "-" + threadNumber.getAndIncrement());
        }
    }
}
