package com.project.lantern.engine.impl.stackoverflow;

import com.project.lantern.engine.EngineConstant;
import com.project.lantern.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.lantern.engine.spec.CodeSearchEngine;
import com.project.lantern.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class StackOverflowCodeSearchEngine extends CodeSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final GoogleGeneralSearchEngine googleGeneralSearchEngine;

    private static final String STACK_OVERFLOW_SITE = "site:stackoverflow.com";

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("fb385a4e-ca0a-4e87-ba42-d7472dbf99f5"))
            .engineName(EngineConstant.ENGINE_STACK_OVERFLOW)
            .engineType(EngineType.CODE)
            .hasLocationSupport(true)
            .hasLanguageSupport(true)
            .hasPaginationSupport(true)
            .maxAllowdedContinousTimeoutCount(5)
            .maxAllowdedContinousBreakdownCount(5)
            .build();

    private final EngineStateEntity engineState = EngineStateEntity.builder()
            .isEnabled(true)
            .continuousTimeoutCount(0)
            .continuousBreakdownCount(0)
            .build();

    @PostConstruct
    public void init() {
        if (ObjectUtils.isEmpty(googleGeneralSearchEngine)) engineState.setEnabled(false);
    }

    @Override
    public EngineSpecEntity getEngineSpec() {
        return this.engineSpec;
    }

    @Override
    public EngineStateEntity getEngineState() {
        return this.engineState;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String searchQuery = searchEntity.getQuery() +
                StringUtils.SPACE + STACK_OVERFLOW_SITE;
        searchEntity.setQuery(searchQuery);
        return googleGeneralSearchEngine.getSearchRequest(searchEntity);
    }

    @Override
    public SearchResultEntity<CodeSearchResultEntity> getResponseEntity(String response) {
        SearchResultEntity<GeneralSearchResultEntity> googleSearchResult = googleGeneralSearchEngine
                .getResponseEntity(response);

        List<GeneralSearchResultEntity> searchResults = googleSearchResult.getSearchResults();
        List<CodeSearchResultEntity> codeSearchResults = searchResults.stream()
                .map(this::formResultEntity)
                .collect(Collectors.toList());

        SearchResultEntityBuilder<CodeSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<CodeSearchResultEntity>builder()
                .searchResults(codeSearchResults)
                .engineResultType(googleSearchResult.getEngineResultType());

        return resultEntityBuilder.build();
    }

    private CodeSearchResultEntity formResultEntity(GeneralSearchResultEntity resultEntity) {
        return CodeSearchResultEntity.builder()
                .url(resultEntity.getUrl())
                .title(resultEntity.getTitle())
                .content(resultEntity.getContent()).build();
    }

}

/*
 * LOG DETAILS
 * URL: https://www.google.com/search?q=Angular+Routing+site%3Astackoverflow.com&hl=en&gl=US&start=0
 *      1. Powered By google.
 *
 * * Google Engine must be enabled
 *
 */
