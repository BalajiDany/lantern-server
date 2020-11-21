package com.project.eniac.engine.piratebay;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.types.EngineResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(SEARCH_QUERY);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		SearchResultEntity<TorrentSearchResultEntity> entity = pirateBayTorrentSearchEngine.performSearch(searchEntity);
		logResponse(entity);
	}

	private <T> void logResponse(SearchResultEntity<T> entity) {
		log.info("Engine Name   : " + entity.getEngineName());
		log.info("Engine Type   : " + entity.getEngineType());
		log.info("Engine Result : " + entity.getEngineResultType());

		if (entity.getEngineResultType() == EngineResultType.FOUND_SEARCH_RESULT) {
			List<T> searchResultList = entity.getSearchResult();
			log.info("Result Count  : " + searchResultList.size());

			for (T searchResult : searchResultList) {
				log.info(searchResult.toString());
			}
		}
	}

}
