package com.project.eniac.config.engine;

import com.project.eniac.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.eniac.engine.impl.stackoverflow.StackOverflowCodeSearchEngine;
import com.project.eniac.engine.spec.CodeSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;

@Configuration
@Import(GeneralSearchEngineConfiguration.class)
@ConditionalOnProperty(value = "project.eniac.engine.code.enable", havingValue = "true", matchIfMissing = true)
public class CodeSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.code.stackoverflow.enable", havingValue = "true", matchIfMissing = true)
    CodeSearchEngine stackOverflowCodeSearchEngine(
            HttpClientProviderService httpClientService,
            @Nullable GoogleGeneralSearchEngine googleGeneralSearchEngine) {
        return new StackOverflowCodeSearchEngine(httpClientService, googleGeneralSearchEngine);
    }

}
