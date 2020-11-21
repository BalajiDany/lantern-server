package com.project.eniac.engine.bing;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.service.spec.HttpClientProviderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BingGeneralSearchEngine extends GeneralSearchEngine {

	private final HttpClientProviderService httpClientService;

	@Override
	public String getEngineName() {
		return "Bing";
	}

	@Override
	public HttpClientProviderService getHttpClientService() {
		return this.httpClientService;
	}

	@Override
	public HttpGet getRequest(MainSearchEntity mainSearchEntity) {
		return null;
	}

	@Override
	public SearchResultEntity<GeneralSearchResultEntity> getResponse(String response) {
		return null;
	}

}
