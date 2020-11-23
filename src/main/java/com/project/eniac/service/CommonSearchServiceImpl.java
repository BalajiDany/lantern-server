package com.project.eniac.service;

import java.util.List;
import java.util.stream.Collectors;

import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.TorrentSearchEngine;
import com.project.eniac.engine.VideoSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResponseEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.CommonSearchService;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommonSearchServiceImpl implements CommonSearchService {

	private final List<GeneralSearchEngine> generalSearchEngines;

	private final List<VideoSearchEngine> videoSearchEngines;

	private final List<TorrentSearchEngine> torrentSearchEngines;

	private final CommonLanguageService commonLanguageService;

	private final CommonLocationService commonLocationService;

	@Override
	public SearchResponseEntity<GeneralSearchResultEntity> generalSearch(MainSearchEntity searchEntity) {
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
	public SearchResponseEntity<VideoSearchResultEntity> videoSearch(MainSearchEntity searchEntity) {
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
	public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(MainSearchEntity searchEntity) {
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

	private void renovateMainSearchEntity(MainSearchEntity entity) {
		String language = entity.getLanguage();
		String location = entity.getLocation();

		entity.setLanguage(commonLanguageService.getSupportedLanguage(language));
		entity.setLocation(commonLocationService.getSupportedLocation(location));
	}

}
