package com.project.eniac.engine.impl.stackoverflow;

import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.eniac.engine.spec.CodeSearchEngine;
import com.project.eniac.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineSpecEntity;
import com.project.eniac.entity.EngineStateEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineType;
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
