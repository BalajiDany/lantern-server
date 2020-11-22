package com.project.eniac.service.spec;

import org.apache.http.client.methods.HttpGet;

public interface HttpClientProviderService {

	<T> String makeRequest(HttpGet getRequest, String clientId);

	<T> String makeRequest(HttpGet getRequest, String clientId, boolean resetClient);

	<T> void resetClient(String clientId);

}