package com.project.lantern.config.engine;

import com.project.lantern.engine.impl.bing.BingVideoSearchEngine;
import com.project.lantern.engine.impl.google.GoogleVideoSearchEngine;
import com.project.lantern.engine.spec.VideoSearchEngine;
import com.project.lantern.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "project.lantern.engine.video.enable", havingValue = "true", matchIfMissing = true)
public class VideoSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.video.google.enable", havingValue = "true", matchIfMissing = true)
    VideoSearchEngine googleVideoSearchEngine(HttpClientProviderService httpClientService) {
        return new GoogleVideoSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.video.bing.enable", havingValue = "true", matchIfMissing = true)
    VideoSearchEngine bingVideGeneralSearchEngine(HttpClientProviderService httpClientProviderService) {
        return new BingVideoSearchEngine(httpClientProviderService);
    }

}
