package com.project.eniac.config;

import com.project.eniac.config.engine.CodeSearchEngineConfiguration;
import com.project.eniac.config.engine.GeneralSearchEngineConfiguration;
import com.project.eniac.config.engine.TorrentSearchEngineConfiguration;
import com.project.eniac.config.engine.VideoSearchEngineConfiguration;
import com.project.eniac.service.impl.CommonLanguageServiceImpl;
import com.project.eniac.service.impl.CommonLocationServiceImpl;
import com.project.eniac.service.impl.EngineDiagnosisServiceImpl;
import com.project.eniac.service.impl.HttpClientProviderServiceImpl;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.EngineDiagnosisService;
import com.project.eniac.service.spec.HttpClientProviderService;
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
