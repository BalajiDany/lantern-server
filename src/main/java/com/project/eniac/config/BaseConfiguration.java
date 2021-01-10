package com.project.eniac.config;

import com.project.eniac.service.impl.*;
import com.project.eniac.service.spec.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@RequiredArgsConstructor
public class BaseConfiguration {

    private final TaskScheduler taskScheduler;

    @Bean
    CommonLanguageService commonLanguageService() {
        return new CommonLanguageServiceImpl();
    }

    @Bean
    CommonLocationService commonLocationService() {
        return new CommonLocationServiceImpl();
    }

    @Bean
    HttpClientProviderService httpClientService() {
        return new HttpClientProviderServiceImpl();
    }

    @Bean
    EngineConfigurationService engineConfigurationService() {
        return new EngineConfigurationServiceImpl();
    }

    @Bean
    EngineDiagnosisService engineDiagnosisService() {
        return new EngineDiagnosisServiceImpl(taskScheduler);
    }

}
