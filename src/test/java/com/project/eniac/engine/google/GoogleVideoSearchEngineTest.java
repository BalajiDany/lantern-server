package com.project.eniac.engine.google;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.utils.Printer;

@SpringBootTest
public class GoogleVideoSearchEngineTest {

	@Autowired
	private CommonLanguageService commonLanguageService;

	@Autowired
	private CommonLocationService commonLocationService;

	@Autowired
	private GoogleVideoSearchEngine googleVideoSearchEngine;

	private final static String SEARCH_QUERY = "The Big Bang Theory";

	@Test
	void verifySearch() {

		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(SEARCH_QUERY);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		SearchResultEntity<VideoSearchResultEntity> entity = googleVideoSearchEngine.performSearch(searchEntity);
		Printer.printVideoSearchResultEntity(entity);
	}

}
