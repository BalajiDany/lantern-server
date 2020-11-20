package com.project.eniac.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.eniac.engine.TorrentSearchEngine;
import com.project.eniac.engine.piratebay.PirateBayTorrentSearchEngine;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.torrent.enable", havingValue = "true", matchIfMissing = true)
public class TorrentSearchEngineConfiguration {

	@Bean
	@ConditionalOnProperty(value = "project.eniac.engine.torrent.piratebay.enable", havingValue = "true", matchIfMissing = true)
	TorrentSearchEngine piratebayTorrentSearchEngine() {
		return new PirateBayTorrentSearchEngine();
	}
}
