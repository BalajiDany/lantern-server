package com.project.eniac.engine.bing;

import com.project.eniac.engine.impl.bing.BingVideoSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.utils.Printer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BingVideoSearchEngineTest {

    @Autowired
    private CommonLanguageService commonLanguageService;

    @Autowired
    private CommonLocationService commonLocationService;

    @Autowired
    private BingVideoSearchEngine bingVideoSearchEngine;

    private final static String SEARCH_QUERY = "The Big Bang Theory";

    @Test
    void verifySearch() {

        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(SEARCH_QUERY);
        searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
        searchEntity.setLocation(commonLocationService.getDefaultLocation());

        SearchResultEntity<VideoSearchResultEntity> entity = bingVideoSearchEngine.performSearch(searchEntity);
        Printer.printVideoSearchResultEntity(entity);
    }
}
