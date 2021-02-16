package com.project.lantern.engine.spec;

import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineResultType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Collections;
import java.util.UUID;

@Data
@Slf4j
public abstract class BaseSearchEngine<T> {

    abstract public EngineSpecEntity getEngineSpec();

    abstract public EngineStateEntity getEngineState();

    abstract public HttpClientProviderService getHttpClientService();

    abstract public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity);

    abstract public SearchResultEntity<T> getResponseEntity(String response);

    public void resetEngine() {
        UUID engineId = getEngineSpec().getEngineId();
        getHttpClientService().resetClient(engineId);
    }

    public SearchResultEntity<T> performSearch(SearchRequestEntity searchEntity) {
        long startTime = System.currentTimeMillis();
        EngineSpecEntity engineSpecEntity = this.getEngineSpec();

        // Make Request
        HttpUriRequest searchRequest = this.getSearchRequest(searchEntity);
        log.debug("URL : {}", searchRequest.getURI().toString());

        HttpClientProviderService httpClientService = this.getHttpClientService();
        String response = httpClientService.makeRequest(searchRequest, engineSpecEntity.getEngineId());

        // Prepare Response
        SearchResultEntityBuilder<T> searchResultEntityBuilder = SearchResultEntity.<T>builder()
                .engineName(engineSpecEntity.getEngineName())
                .engineType(engineSpecEntity.getEngineType());

        if (StringUtils.isEmpty(response)) {
            searchResultEntityBuilder
                    .searchResults(Collections.emptyList())
                    .engineResultType(EngineResultType.ENGINE_TIME_OUT);
        } else {
            SearchResultEntity<T> responseEntity = this.getResponseEntity(response);
            searchResultEntityBuilder
                    .searchResults(responseEntity.getSearchResults())
                    .engineResultType(responseEntity.getEngineResultType());
        }

        long duration = System.currentTimeMillis() - startTime;
        return searchResultEntityBuilder
                .duration(duration)
                .build();
    }

}
