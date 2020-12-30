package com.project.eniac.config.engine;

import com.project.eniac.engine.impl.bing.BingGeneralSearchEngine;
import com.project.eniac.engine.impl.bing.BingParachuteGeneralSearchEngine;
import com.project.eniac.engine.impl.duckduckgo.DuckDuckGoGeneralSearchEngine;
import com.project.eniac.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.eniac.engine.impl.yahoo.YahooGeneralSearchEngine;
import com.project.eniac.engine.spec.GeneralSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.general.enable", havingValue = "true", matchIfMissing = true)
public class GeneralSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.general.google.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine googleGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new GoogleGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.general.bing.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine bingGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new BingGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.general.bingparachute.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine bingParachuteGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new BingParachuteGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.general.yahoo.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine yahooGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new YahooGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.general.duckduckgo.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine duckDuckGoGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new DuckDuckGoGeneralSearchEngine(httpClientService);
    }

}
