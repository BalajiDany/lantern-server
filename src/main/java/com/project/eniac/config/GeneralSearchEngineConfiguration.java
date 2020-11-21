package com.project.eniac.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.google.GoogleGeneralSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.general.enable", havingValue = "true", matchIfMissing = true)
public class GeneralSearchEngineConfiguration {

	@Bean
	@ConditionalOnProperty(value = "project.eniac.engine.general.google.enable", havingValue = "true", matchIfMissing = true)
	GeneralSearchEngine googleGeneralSearchEngine(HttpClientProviderService httpClientService) {
		return new GoogleGeneralSearchEngine(httpClientService);
	}

}
