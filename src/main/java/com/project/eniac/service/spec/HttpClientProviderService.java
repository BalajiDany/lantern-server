package com.project.eniac.service.spec;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.engine.BaseSearchEngine;

public interface HttpClientProviderService {

	<T> String makeRequest(HttpGet getRequest, BaseSearchEngine<T> searchEngine);

	<T> String makeRequest(HttpGet getRequest, BaseSearchEngine<T> searchEngine, boolean resetClient);

	<T> void resetClient(BaseSearchEngine<T> searchEngine);

}
