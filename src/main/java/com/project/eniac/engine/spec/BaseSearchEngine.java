package com.project.eniac.engine.spec;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineType;
import com.project.eniac.utils.UserAgent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseSearchEngine<T> {

	abstract public String getEngineName();

	abstract public EngineType getEngineType();

	abstract public HttpClientProviderService getHttpClientService();

	abstract public HttpGet getRequest(SearchRequestEntity searchEntity);

	abstract public SearchResultEntity<T> getResponse(String response);

	public SearchResultEntity<T> performSearch(SearchRequestEntity searchEntity) {
		HttpGet getRequest = this.getRequest(searchEntity);

		String userAgent = UserAgent.getRandomUserAgent();
		getRequest.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);

		HttpClientProviderService httpClientService = this.getHttpClientService();
		log.info("URL : {}", getRequest.getURI().toString());
		String response = httpClientService.makeRequest(getRequest, this.getEngineName());
		return this.getResponse(response);
	}

}
