package com.project.eniac.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.engine.impl.kickass.KickassTorrentSearchEngine;
import com.project.eniac.engine.impl.piratebay.PirateBayTorrentSearchEngine;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.torrent.enable", havingValue = "true", matchIfMissing = true)
public class TorrentSearchEngineConfiguration {

	@Bean
	@ConditionalOnProperty(value = "project.eniac.engine.torrent.piratebay.enable", havingValue = "true", matchIfMissing = true)
	TorrentSearchEngine piratebayTorrentSearchEngine(HttpClientProviderService httpClientService) {
		return new PirateBayTorrentSearchEngine(httpClientService);
	}

	@Bean
	@ConditionalOnProperty(value = "project.eniac.engine.torrent.kickass.enable", havingValue = "true", matchIfMissing = true)
	TorrentSearchEngine kickassTorrentSearchEngine(HttpClientProviderService httpClientService) {
		return new KickassTorrentSearchEngine(httpClientService);
	}

}
