package com.project.eniac.service.spec;

import org.apache.http.client.methods.HttpUriRequest;

public interface HttpClientProviderService {

	String makeRequest(HttpUriRequest getRequest, String clientId);

	String makeRequest(HttpUriRequest getRequest, String clientId, boolean resetClient);

	void resetClient(String clientId);

}
