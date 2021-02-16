package com.project.lantern.engine.stackoverflow;

import com.project.lantern.engine.impl.stackoverflow.StackOverflowCodeSearchEngine;
import com.project.lantern.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.CommonLanguageService;
import com.project.lantern.service.spec.CommonLocationService;
import com.project.lantern.utils.Printer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class StackOverflowCodeSearchEngineTest {

    @Autowired
    private CommonLanguageService commonLanguageService;

    @Autowired
    private CommonLocationService commonLocationService;

    @Autowired
    private StackOverflowCodeSearchEngine stackOverflowCodeSearchEngine;

    private final static String SEARCH_QUERY = "Angular Routing";

    @Test
    void verifySearch() {

        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(SEARCH_QUERY);
        searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
        searchEntity.setLocation(commonLocationService.getDefaultLocation());

        SearchResultEntity<CodeSearchResultEntity> entity = stackOverflowCodeSearchEngine.performSearch(searchEntity);
//        assertNotEquals(entity.getSearchResults(), null);
//        assertNotEquals(entity.getSearchResults().size(), 0);

        Printer.printCodeSearchResultEntity(entity);
    }

}
