package com.project.eniac.engine.kickass;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.utils.Printer;

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
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(SEARCH_QUERY);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		SearchResultEntity<TorrentSearchResultEntity> entity = kickAssTorrentSearchEngine.performSearch(searchEntity);
		Printer.printTorrentSearchResultEntity(entity);
	}

}
