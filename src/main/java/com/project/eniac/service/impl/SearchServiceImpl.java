package com.project.eniac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.eniac.engine.spec.BaseSearchEngine;
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
import com.project.eniac.service.spec.EngineDiagnosisService;
import com.project.eniac.service.spec.SearchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final CommonLanguageService commonLanguageService;

	private final CommonLocationService commonLocationService;

	private final EngineDiagnosisService engineDiagnosisService;

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

		List<SearchResultEntity<GeneralSearchResultEntity>> resultEntity = generalSearchEngines.stream().parallel()
			.filter(BaseSearchEngine::isEnabled)
			.map(engine -> {
				SearchResultEntity<GeneralSearchResultEntity> response = engine.performSearch(searchEntity);
				afterSearch(engine, response);
				return response;
			})
			.filter(this::isValidResponse)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<GeneralSearchResultEntity>builder()
				.searchResult(resultEntity)
				.searchDuration(runTime)
				.build();
	}

	@Override
	public SearchResponseEntity<VideoSearchResultEntity> videoSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();
		this.renovateMainSearchEntity(searchEntity);

		List<SearchResultEntity<VideoSearchResultEntity>> resultEntity = videoSearchEngines.stream().parallel()
			.filter(BaseSearchEngine::isEnabled)
			.map(engine -> {
				SearchResultEntity<VideoSearchResultEntity> response = engine.performSearch(searchEntity);
				afterSearch(engine, response);
				return response;
			})
			.filter(this::isValidResponse)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<VideoSearchResultEntity>builder()
				.searchResult(resultEntity)
				.searchDuration(runTime)
				.build();
	}

	@Override
	public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();
		this.renovateMainSearchEntity(searchEntity);

		List<SearchResultEntity<TorrentSearchResultEntity>> resultEntity = torrentSearchEngines.stream().parallel()
			.filter(BaseSearchEngine::isEnabled)
			.map(engine -> {
				SearchResultEntity<TorrentSearchResultEntity> response = engine.performSearch(searchEntity);
				afterSearch(engine, response);
				return response;
			})
			.filter(this::isValidResponse)
			.collect(Collectors.toList());

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		return SearchResponseEntity.<TorrentSearchResultEntity>builder()
				.searchResult(resultEntity)
				.searchDuration(runTime)
				.build();
	}

	private void afterSearch(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response) {
		engineDiagnosisService.diagnosisEngine(searchEngine, response);
	}

	private boolean isValidResponse(SearchResultEntity<?> searchResultEntity) {
		return  true;
	}

	private void renovateMainSearchEntity(SearchRequestEntity entity) {
		String language = entity.getLanguage();
		String location = entity.getLocation();

		entity.setLanguage(commonLanguageService.getSupportedLanguage(language));
		entity.setLocation(commonLocationService.getSupportedLocation(location));
	}

}
