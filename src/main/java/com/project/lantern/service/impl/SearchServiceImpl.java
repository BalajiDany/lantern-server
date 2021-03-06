package com.project.lantern.service.impl;

import com.project.lantern.engine.spec.*;
import com.project.lantern.entity.EngineResultEntity.*;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.entity.SearchResponseEntity;
import com.project.lantern.service.spec.CommonLanguageService;
import com.project.lantern.service.spec.CommonLocationService;
import com.project.lantern.service.spec.EngineDiagnosisService;
import com.project.lantern.service.spec.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final CommonLanguageService commonLanguageService;

    private final CommonLocationService commonLocationService;

    private final EngineDiagnosisService engineDiagnosisService;

    @Autowired(required = false)
    private final List<GeneralSearchEngine> generalSearchEngines = new ArrayList<>();

    @Autowired(required = false)
    private final List<VideoSearchEngine> videoSearchEngines = new ArrayList<>();

    @Autowired(required = false)
    private final List<TorrentSearchEngine> torrentSearchEngines = new ArrayList<>();

    @Autowired(required = false)
    private final List<CodeSearchEngine> codeSearchEngines = new ArrayList<>();

    @Override
    public SearchResponseEntity<GeneralSearchResultEntity> generalSearch(SearchRequestEntity searchEntity) {
        return coreSearch(generalSearchEngines, searchEntity);
    }

    @Override
    public SearchResponseEntity<VideoSearchResultEntity> videoSearch(SearchRequestEntity searchEntity) {
        return coreSearch(videoSearchEngines, searchEntity);
    }

    @Override
    public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(SearchRequestEntity searchEntity) {
        return coreSearch(torrentSearchEngines, searchEntity);
    }

    @Override
    public SearchResponseEntity<CodeSearchResultEntity> codeSearch(SearchRequestEntity searchEntity) {
        return coreSearch(codeSearchEngines, searchEntity);
    }

    private <T, E extends BaseSearchEngine<T>> SearchResponseEntity<T> coreSearch(
        List<E> searchEngine, SearchRequestEntity requestEntity) {
        long startTime = System.currentTimeMillis();
        this.cleanRequestEntity(requestEntity);

        List<SearchResultEntity<T>> resultEntity = searchEngine.stream().parallel()
            .filter(engine -> engine.getEngineState().isEnabled())
            .map(engine -> this.searchAndDiagnosis(engine, requestEntity))
            .filter(this::isValidResponse)
            .collect(Collectors.toList());

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        return SearchResponseEntity.<T>builder()
            .searchResults(resultEntity)
            .duration(runTime)
            .build();
    }

    private <T> SearchResultEntity<T> searchAndDiagnosis(
        BaseSearchEngine<T> searchEngine, SearchRequestEntity searchEntity) {
        SearchResultEntity<T> response = searchEngine.performSearch(searchEntity);
        afterSearch(searchEngine, response);
        return response;
    }

    private void afterSearch(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response) {
        engineDiagnosisService.diagnosisEngine(searchEngine, response);
    }

    private boolean isValidResponse(SearchResultEntity<?> searchResultEntity) {
        return true;
    }

    private void cleanRequestEntity(SearchRequestEntity entity) {
        String language = entity.getLanguage();
        String location = entity.getLocation();

        entity.setLanguage(commonLanguageService.getSupportedLanguage(language));
        entity.setLocation(commonLocationService.getSupportedLocation(location));
    }

}
