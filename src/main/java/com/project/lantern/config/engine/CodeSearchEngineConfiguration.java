package com.project.lantern.config.engine;

import com.project.lantern.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.lantern.engine.impl.stackoverflow.StackOverflowCodeSearchEngine;
import com.project.lantern.engine.spec.CodeSearchEngine;
import com.project.lantern.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

@ConditionalOnProperty(value = "project.lantern.engine.code.enable", havingValue = "true", matchIfMissing = true)
public class CodeSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.code.stackoverflow.enable", havingValue = "true", matchIfMissing = true)
    CodeSearchEngine stackOverflowCodeSearchEngine(
            HttpClientProviderService httpClientService,
            @Nullable GoogleGeneralSearchEngine googleGeneralSearchEngine) {
        return new StackOverflowCodeSearchEngine(httpClientService, googleGeneralSearchEngine);
    }

}
