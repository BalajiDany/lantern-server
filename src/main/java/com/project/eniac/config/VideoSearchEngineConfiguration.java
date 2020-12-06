package com.project.eniac.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.engine.impl.google.GoogleVideoSearchEngine;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.video.enable", havingValue = "true", matchIfMissing = true)
public class VideoSearchEngineConfiguration {

	@Bean
	@ConditionalOnProperty(value = "project.eniac.engine.video.google.enable", havingValue = "true", matchIfMissing = true)
	VideoSearchEngine googleVideoSearchEngine(HttpClientProviderService httpClientService) {
		return new GoogleVideoSearchEngine(httpClientService);
	}

}
