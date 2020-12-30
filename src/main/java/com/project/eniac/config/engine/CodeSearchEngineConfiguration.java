package com.project.eniac.config.engine;

import com.project.eniac.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.eniac.engine.impl.stackoverflow.StackOverflowCodeSearchEngine;
import com.project.eniac.engine.spec.CodeSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.code.enable", havingValue = "true", matchIfMissing = true)
@Import({GeneralSearchEngineConfiguration.class})
public class CodeSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.code.stackoverflow", havingValue = "true", matchIfMissing = true)
    CodeSearchEngine stackOverflowCodeSearchEngine(
            HttpClientProviderService httpClientService,
            GoogleGeneralSearchEngine googleGeneralSearchEngine) {
        return new StackOverflowCodeSearchEngine(httpClientService, googleGeneralSearchEngine);
    }

}
