package com.project.lantern.engine.bing;

import com.project.lantern.engine.impl.bing.BingGeneralSearchEngine;
import com.project.lantern.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.CommonLanguageService;
import com.project.lantern.service.spec.CommonLocationService;
import com.project.lantern.utils.Printer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class BingGeneralSearchEngineTest {

    @Autowired
    private CommonLanguageService commonLanguageService;

    @Autowired
    private CommonLocationService commonLocationService;

    @Autowired
    private BingGeneralSearchEngine bingGeneralSearchEngine;

    private final static String SEARCH_QUERY = "The Big Bang Theory";

    @Test
    void verifySearch() {

        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(SEARCH_QUERY);
        searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
        searchEntity.setLocation(commonLocationService.getDefaultLocation());

        SearchResultEntity<GeneralSearchResultEntity> entity = bingGeneralSearchEngine.performSearch(searchEntity);
//        assertNotEquals(entity.getSearchResults(), null);
//        assertNotEquals(entity.getSearchResults().size(), 0);

        Printer.printGeneralSearchResultEntity(entity);
    }

}
