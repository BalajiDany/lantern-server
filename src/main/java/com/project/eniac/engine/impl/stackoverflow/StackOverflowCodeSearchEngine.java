package com.project.eniac.engine.impl.stackoverflow;

import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.google.GoogleGeneralSearchEngine;
import com.project.eniac.engine.spec.CodeSearchEngine;
import com.project.eniac.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class StackOverflowCodeSearchEngine extends CodeSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final GoogleGeneralSearchEngine googleGeneralSearchEngine;

    private static final String STACK_OVERFLOW_SITE = "site:stackoverflow.com";

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_STACK_OVERFLOW;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String searchQuery = new StringBuilder(searchEntity.getQuery())
                .append(StringUtils.SPACE)
                .append(STACK_OVERFLOW_SITE).toString();
        searchEntity.setQuery(searchQuery);
        return googleGeneralSearchEngine.getRequest(searchEntity);
    }

    @Override
    public SearchResultEntity<CodeSearchResultEntity> getResponse(String response) {
        SearchResultEntity<GeneralSearchResultEntity> googleSearchResult = googleGeneralSearchEngine.getResponse(response);

        SearchResultEntityBuilder<CodeSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<CodeSearchResultEntity>builder()
                .engineName(this.getEngineName())
                .engineType(this.getEngineType())
                .duration(googleSearchResult.getDuration())
                .engineResultType(googleSearchResult.getEngineResultType());

        List<GeneralSearchResultEntity> searchResults = googleSearchResult.getSearchResults();
        if (ObjectUtils.isNotEmpty(searchResults)) {
            List<CodeSearchResultEntity> codeSearchResults = searchResults.stream()
                    .map(this::formResultEntity)
                    .collect(Collectors.toList());
            resultEntityBuilder.searchResults(codeSearchResults);
        }

        return resultEntityBuilder.build();
    }

    private CodeSearchResultEntity formResultEntity(GeneralSearchResultEntity generalSearchResultEntity) {
        return CodeSearchResultEntity.builder()
                .url(generalSearchResultEntity.getUrl())
                .title(generalSearchResultEntity.getTitle())
                .content(generalSearchResultEntity.getContent()).build();
    }
}
