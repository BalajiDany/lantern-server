package com.project.eniac.service.spec;

import org.apache.http.client.methods.HttpUriRequest;

public interface HttpClientProviderService {

	<T> String makeRequest(HttpUriRequest getRequest, String clientId);

	<T> String makeRequest(HttpUriRequest getRequest, String clientId, boolean resetClient);

	<T> void resetClient(String clientId);

}
