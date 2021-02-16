package com.project.lantern.config;

import com.project.lantern.service.impl.EngineConfigurationServiceImpl;
import com.project.lantern.service.impl.EngineStatusServiceImpl;
import com.project.lantern.service.impl.NetworkStatusServiceImpl;
import com.project.lantern.service.impl.SearchServiceImpl;
import com.project.lantern.service.spec.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class ServiceConfiguration {

    private final CommonLanguageService commonLanguageService;

    private final CommonLocationService commonLocationService;

    private final EngineDiagnosisService engineDiagnosisService;

    private final HttpClientProviderService httpClientService;

    @Bean
    NetworkStatusService networkStatusService() {
        return new NetworkStatusServiceImpl(httpClientService);
    }

    @Bean
    SearchService searchService() {
        return new SearchServiceImpl(commonLanguageService, commonLocationService, engineDiagnosisService);
    }

    @Bean
    EngineStatusService engineStatusService() {
        return new EngineStatusServiceImpl();
    }

    @Bean
    EngineConfigurationService engineConfigurationService() {
        return new EngineConfigurationServiceImpl();
    }
}
