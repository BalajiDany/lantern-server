package com.project.eniac.service.impl;

import com.project.eniac.engine.spec.*;
import com.project.eniac.entity.EngineResultEntity.*;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.service.spec.EngineDiagnosisService;
import com.project.eniac.service.spec.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final CommonLanguageService commonLanguageService;

    private final CommonLocationService commonLocationService;

    private final EngineDiagnosisService engineDiagnosisService;

    @Autowired(required = false)
    private List<GeneralSearchEngine> generalSearchEngines;

    @Autowired(required = false)
    private List<VideoSearchEngine> videoSearchEngines = new ArrayList<>();

    @Autowired(required = false)
    private List<TorrentSearchEngine> torrentSearchEngines = new ArrayList<>();

    @Autowired(required = false)
    private List<CodeSearchEngine> codeSearchEngines = new ArrayList<>();

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
                .searchResults(resultEntity)
                .duration(runTime)
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
                .searchResults(resultEntity)
                .duration(runTime)
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
                .searchResults(resultEntity)
                .duration(runTime)
                .build();
    }

    @Override
    public SearchResponseEntity<CodeSearchResultEntity> codeSearch(SearchRequestEntity searchEntity) {
        long startTime = System.currentTimeMillis();
        this.renovateMainSearchEntity(searchEntity);

        List<SearchResultEntity<CodeSearchResultEntity>> resultEntity = codeSearchEngines.stream().parallel()
                .filter(BaseSearchEngine::isEnabled)
                .map(engine -> {
                    SearchResultEntity<CodeSearchResultEntity> response = engine.performSearch(searchEntity);
                    afterSearch(engine, response);
                    return response;
                })
                .filter(this::isValidResponse)
                .collect(Collectors.toList());

        long stopTime = System.currentTimeMillis();
        long runTime = stopTime - startTime;
        return SearchResponseEntity.<CodeSearchResultEntity>builder()
                .searchResults(resultEntity)
                .duration(runTime)
                .build();
    }

    private void afterSearch(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response) {
        engineDiagnosisService.diagnosisEngine(searchEngine, response);
    }

    private boolean isValidResponse(SearchResultEntity<?> searchResultEntity) {
        return true;
    }

    private void renovateMainSearchEntity(SearchRequestEntity entity) {
        String language = entity.getLanguage();
        String location = entity.getLocation();

        entity.setLanguage(commonLanguageService.getSupportedLanguage(language));
        entity.setLocation(commonLocationService.getSupportedLocation(location));
    }

}
