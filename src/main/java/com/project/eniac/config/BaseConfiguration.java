package com.project.eniac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.service.impl.CommonLanguageServiceImpl;
import com.project.eniac.service.impl.CommonLocationServiceImpl;
import com.project.eniac.service.impl.HttpClientProviderServiceImpl;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.HttpClientProviderService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BaseConfiguration {

	@Bean
	CommonLanguageService commonLanguageService() {
		return new CommonLanguageServiceImpl();
	}

	@Bean
	CommonLocationService commonLocationService() {
		return new CommonLocationServiceImpl();
	}

	@Bean
	HttpClientProviderService httpClientService() {
		return new HttpClientProviderServiceImpl();
	}
}
