package com.project.eniac.engine;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.service.spec.HttpClientService;
import com.project.eniac.types.EngineType;
import com.project.eniac.utils.UserAgent;

public abstract class BaseSearchEngine<T> {

	abstract public HttpGet getRequest(MainSearchEntity mainSearchEntity);

	abstract public SearchResultEntity<T> getResponse(String response);

	abstract public String getEngineName();

	abstract public EngineType getEngineType();

	abstract public HttpClientService getHttpClientService();

	public SearchResultEntity<T> performSearch(MainSearchEntity mainSearchEntity) {
		HttpGet getRequest = this.getRequest(mainSearchEntity);

		String userAgent = UserAgent.getRandomUserAgent();
		getRequest.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);

		HttpClientService httpClientService = this.getHttpClientService();
		String response = httpClientService.makeRequest(getRequest, this);
		return this.getResponse(response);
	}

}
