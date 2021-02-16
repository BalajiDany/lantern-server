package com.project.lantern.config.engine;

import com.project.lantern.engine.impl.bing.BingGeneralSearchEngine;
import com.project.lantern.engine.impl.bing.BingParachuteGeneralSearchEngine;
import com.project.lantern.engine.impl.duckduckgo.DuckDuckGoGeneralSearchEngine;
import com.project.lantern.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.lantern.engine.impl.yahoo.YahooGeneralSearchEngine;
import com.project.lantern.engine.spec.GeneralSearchEngine;
import com.project.lantern.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "project.lantern.engine.general.enable", havingValue = "true", matchIfMissing = true)
public class GeneralSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.general.google.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine googleGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new GoogleGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.general.bing.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine bingGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new BingGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.general.bingparachute.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine bingParachuteGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new BingParachuteGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.general.yahoo.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine yahooGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new YahooGeneralSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.general.duckduckgo.enable", havingValue = "true", matchIfMissing = true)
    GeneralSearchEngine duckDuckGoGeneralSearchEngine(HttpClientProviderService httpClientService) {
        return new DuckDuckGoGeneralSearchEngine(httpClientService);
    }

}
