package com.project.eniac.engine.piratebay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.engine.impl.piratebay.PirateBayTorrentSearchEngine;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.utils.Printer;

@SpringBootTest
public class PirateBayTorrentSearchEngineTest {

	@Autowired
	private CommonLanguageService commonLanguageService;

	@Autowired
	private CommonLocationService commonLocationService;

	@Autowired
	private PirateBayTorrentSearchEngine pirateBayTorrentSearchEngine;

	private final static String SEARCH_QUERY = "The Big Bang Theory";

	@Test
	void verifySearch() {
		SearchRequestEntity searchEntity = new SearchRequestEntity();
		searchEntity.setQuery(SEARCH_QUERY);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		SearchResultEntity<TorrentSearchResultEntity> entity = pirateBayTorrentSearchEngine.performSearch(searchEntity);
		Printer.printTorrentSearchResultEntity(entity);
	}

}
