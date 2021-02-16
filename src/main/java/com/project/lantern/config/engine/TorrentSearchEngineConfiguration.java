package com.project.lantern.config.engine;

import com.project.lantern.engine.impl.kickass.KickassTorrentSearchEngine;
import com.project.lantern.engine.impl.limetorrent.LimeTorrentSearchEngine;
import com.project.lantern.engine.impl.piratebay.PirateBay10TorrentSearchEngine;
import com.project.lantern.engine.impl.piratebay.PirateBayTorrentSearchEngine;
import com.project.lantern.engine.spec.TorrentSearchEngine;
import com.project.lantern.service.spec.HttpClientProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "project.lantern.engine.torrent.enable", havingValue = "true", matchIfMissing = true)
public class TorrentSearchEngineConfiguration {

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.torrent.piratebay.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine piratebayTorrentSearchEngine(HttpClientProviderService httpClientService) {
        return new PirateBayTorrentSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.torrent.piratebay10.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine piratebay10TorrentSearchEngine(HttpClientProviderService httpClientService) {
        return new PirateBay10TorrentSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.torrent.kickass.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine kickassTorrentSearchEngine(HttpClientProviderService httpClientService) {
        return new KickassTorrentSearchEngine(httpClientService);
    }

    @Bean
    @ConditionalOnProperty(value = "project.lantern.engine.torrent.limetorrent.enable", havingValue = "true", matchIfMissing = true)
    TorrentSearchEngine LimeTorrentSearchEngine(HttpClientProviderService httpClientProviderService) {
        return new LimeTorrentSearchEngine(httpClientProviderService);
    }

}
