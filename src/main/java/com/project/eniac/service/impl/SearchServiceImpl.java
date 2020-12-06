package com.project.eniac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.eniac.engine.spec.GeneralSearchEngine;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.SearchService;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final CommonLanguageService commonLanguageService;

	private final CommonLocationService commonLocationService;

	@Autowired
	private List<GeneralSearchEngine> generalSearchEngines;

	@Autowired
	private List<VideoSearchEngine> videoSearchEngines;

	@Autowired
	private List<TorrentSearchEngine> torrentSearchEngines;

	@Override
	public SearchResponseEntity<GeneralSearchResultEntity> generalSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();
		this.renovateMainSearchEntity(searchEntity);

		List<SearchResultEntity<GeneralSearchResultEntity>> resutlEntity = generalSearchEngines.stream().parallel()
			.map(engine -> engine.performSearch(searchEntity))
			.filter(this::afterSearch)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<GeneralSearchResultEntity>builder()
				.searchResult(resutlEntity)
				.searchDuration(runTime)
				.build();
	}

	@Override
	public SearchResponseEntity<VideoSearchResultEntity> videoSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();
		this.renovateMainSearchEntity(searchEntity);

		List<SearchResultEntity<VideoSearchResultEntity>> resutlEntity = videoSearchEngines.stream().parallel()

			.map(engine -> engine.performSearch(searchEntity))
			.filter(this::afterSearch)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<VideoSearchResultEntity>builder()
				.searchResult(resutlEntity)
				.searchDuration(runTime)
				.build();
	}


	@Override
	public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();
		this.renovateMainSearchEntity(searchEntity);

		List<SearchResultEntity<TorrentSearchResultEntity>> resutlEntity = torrentSearchEngines.stream().parallel()
			.map(engine -> engine.performSearch(searchEntity))
			.filter(this::afterSearch)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<TorrentSearchResultEntity>builder()
				.searchResult(resutlEntity)
				.searchDuration(runTime)
				.build();
	}

	private <T> boolean afterSearch(SearchResultEntity<T> searchResultEntity) {
		EngineResultType resultType = searchResultEntity.getEngineResultType();
		if (resultType == EngineResultType.ENGINE_BREAK_DOWN) {
			// Trigger Diagnosis
			log.error("{} Engine Break Down", searchResultEntity.getEngineName());
			return false;
		}
		return  true;
	}

	private void renovateMainSearchEntity(SearchRequestEntity entity) {
		String language = entity.getLanguage();
		String location = entity.getLocation();

		entity.setLanguage(commonLanguageService.getSupportedLanguage(language));
		entity.setLocation(commonLocationService.getSupportedLocation(location));
	}

}
