package com.project.eniac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.service.impl.EngineStatusServiceImpl;
import com.project.eniac.service.impl.NetworkStatusServiceImpl;
import com.project.eniac.service.impl.SearchServiceImpl;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.EngineDiagnosisService;
import com.project.eniac.service.spec.EngineStatusService;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.service.spec.NetworkStatusService;
import com.project.eniac.service.spec.SearchService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CommonServiceConfiguration {

	private final CommonLanguageService commonLanguageService;

	private final CommonLocationService commonLocationService;

	private final EngineDiagnosisService engineDiagnosisService;

	private final HttpClientProviderService httpClientService;

	@Bean
	NetworkStatusService networkStatusService() {
		return new NetworkStatusServiceImpl(httpClientService);
	}

	@Bean
	SearchService searchService() {
		return new SearchServiceImpl(commonLanguageService, commonLocationService, engineDiagnosisService);
	}

	@Bean
	EngineStatusService engineStatusService() {
		return new EngineStatusServiceImpl();
	}

}
