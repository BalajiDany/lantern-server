package com.project.eniac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.project.eniac.service.CommonLanguageServiceImpl;
import com.project.eniac.service.CommonLocationServiceImpl;
import com.project.eniac.service.HttpClientProviderServiceImpl;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.HttpClientProviderService;

@Configuration
@PropertySource("classpath:eniac.properties")
public class BaseConfig {

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
