package com.project.lantern.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

    @Bean(name = "networkAgentTaskExecutor")
    public Executor networkAgentTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean(name = "engineDiagnosisTaskExecutor")
    public Executor engineDiagnosisTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
