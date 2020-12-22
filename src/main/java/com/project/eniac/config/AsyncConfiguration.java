package com.project.eniac.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration  {

	@Bean(name = "networkAgentTaskExecutor")
	public Executor networkAgentTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

	@Bean(name = "engineDiagnosisTaskExecutor")
	public Executor engineDiagnosisTaskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
