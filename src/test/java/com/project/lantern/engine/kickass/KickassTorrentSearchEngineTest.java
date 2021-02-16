package com.project.lantern.engine.kickass;

import com.project.lantern.engine.impl.kickass.KickassTorrentSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.CommonLanguageService;
import com.project.lantern.service.spec.CommonLocationService;
import com.project.lantern.utils.Printer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class KickassTorrentSearchEngineTest {

    @Autowired
    private CommonLanguageService commonLanguageService;

    @Autowired
    private CommonLocationService commonLocationService;

    @Autowired
    private KickassTorrentSearchEngine kickAssTorrentSearchEngine;

    private final static String SEARCH_QUERY = "The Big Bang Theory";

    @Test
    void verifySearch() {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(SEARCH_QUERY);
        searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
        searchEntity.setLocation(commonLocationService.getDefaultLocation());

        SearchResultEntity<TorrentSearchResultEntity> entity = kickAssTorrentSearchEngine.performSearch(searchEntity);
//        assertNotEquals(entity.getSearchResults(), null);
//        assertNotEquals(entity.getSearchResults().size(), 0);

        Printer.printTorrentSearchResultEntity(entity);
    }

}
