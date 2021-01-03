package com.project.eniac.config.engine;

import com.project.eniac.engine.impl.kickass.KickassTorrentSearchEngine;
import com.project.eniac.engine.impl.limetorrent.LimeTorrentSearchEngine;
import com.project.eniac.engine.impl.piratebay.PirateBay10TorrentSearchEngine;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "project.eniac.engine.torrent.enable", havingValue = "true", matchIfMissing = true)
public class TorrentSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.torrent.piratebay.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine piratebayTorrentSearchEngine(HttpClientProviderService httpClientService) {
        return new PirateBay10TorrentSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.torrent.kickass.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine kickassTorrentSearchEngine(HttpClientProviderService httpClientService) {
        return new KickassTorrentSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.eniac.engine.torrent.limetorrent.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine LimeTorrentSearchEngine(HttpClientProviderService httpClientProviderService) {
        return new LimeTorrentSearchEngine(httpClientProviderService);
    }

}
