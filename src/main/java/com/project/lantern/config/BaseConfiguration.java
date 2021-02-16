package com.project.lantern.config;

import com.project.lantern.config.engine.CodeSearchEngineConfiguration;
import com.project.lantern.config.engine.GeneralSearchEngineConfiguration;
import com.project.lantern.config.engine.TorrentSearchEngineConfiguration;
import com.project.lantern.config.engine.VideoSearchEngineConfiguration;
import com.project.lantern.service.impl.CommonLanguageServiceImpl;
import com.project.lantern.service.impl.CommonLocationServiceImpl;
import com.project.lantern.service.impl.EngineDiagnosisServiceImpl;
import com.project.lantern.service.impl.HttpClientProviderServiceImpl;
import com.project.lantern.service.spec.CommonLanguageService;
import com.project.lantern.service.spec.CommonLocationService;
import com.project.lantern.service.spec.EngineDiagnosisService;
import com.project.lantern.service.spec.HttpClientProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.TaskScheduler;

@Configuration
@RequiredArgsConstructor
@Import({
        AsyncConfiguration.class,
        PropertySourceConfiguration.class,

        GeneralSearchEngineConfiguration.class,
        CodeSearchEngineConfiguration.class,
        TorrentSearchEngineConfiguration.class,
        VideoSearchEngineConfiguration.class,

        ServiceConfiguration.class,
})
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
    EngineDiagnosisService engineDiagnosisService() {
        return new EngineDiagnosisServiceImpl(taskScheduler);
    }

}
