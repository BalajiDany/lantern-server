package com.project.eniac.config.engine;

import com.project.eniac.engine.impl.bing.BingVideoSearchEngine;
import com.project.eniac.engine.impl.google.GoogleVideoSearchEngine;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "project.eniac.engine.video.enable", havingValue = "true", matchIfMissing = true)
public class VideoSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.video.google.enable", havingValue = "true", matchIfMissing = true)
    VideoSearchEngine googleVideoSearchEngine(HttpClientProviderService httpClientService) {
        return new GoogleVideoSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.video.bing.enable", havingValue = "true", matchIfMissing = true)
    VideoSearchEngine bingVideGeneralSearchEngine(HttpClientProviderService httpClientProviderService) {
        return new BingVideoSearchEngine(httpClientProviderService);
    }

}
