package com.project.eniac.engine.bing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.utils.Printer;

@SpringBootTest
public class BingParachuteGeneralSearchEngineTest {

	@Autowired
	private CommonLanguageService commonLanguageService;

	@Autowired
	private CommonLocationService commonLocationService;

	@Autowired
	private BingParachuteGeneralSearchEngine bingParachuteGeneralSearchEngine;

	private final static String SEARCH_QUERY = "The Big Bang Theory";

	@Test
	void verifySearch() {

		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(SEARCH_QUERY);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		SearchResultEntity<GeneralSearchResultEntity> entity = bingParachuteGeneralSearchEngine.performSearch(searchEntity);
		Printer.printGeneralSearchResultEntity(entity);
	}

}
