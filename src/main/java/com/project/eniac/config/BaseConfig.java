package com.project.eniac.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.TorrentSearchEngine;
import com.project.eniac.engine.VideoSearchEngine;
import com.project.eniac.service.CommonLanguageServiceImpl;
import com.project.eniac.service.CommonLocationServiceImpl;
import com.project.eniac.service.CommonNetworkStatusServiceImpl;
import com.project.eniac.service.CommonSearchServiceImpl;
import com.project.eniac.service.HttpClientProviderServiceImpl;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.CommonNetworkStatusService;
import com.project.eniac.service.spec.CommonSearchService;
import com.project.eniac.service.spec.HttpClientProviderService;

@Configuration
@EnableAsync
@EnableScheduling
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

	@Bean
	CommonNetworkStatusService commonNetworkStatusService(HttpClientProviderService httpClientService) {
		return new CommonNetworkStatusServiceImpl(httpClientService);
	}

	@Bean
	CommonSearchService commonSearchService(List<GeneralSearchEngine> generalSearchEngines,
			List<VideoSearchEngine> videoSearchEngines, List<TorrentSearchEngine> torrentSearchEngines,
			CommonLanguageService commonLanguageService, CommonLocationService commonLocationService) {
		return new CommonSearchServiceImpl(generalSearchEngines, videoSearchEngines, torrentSearchEngines,
				commonLanguageService, commonLocationService);
	}

}
