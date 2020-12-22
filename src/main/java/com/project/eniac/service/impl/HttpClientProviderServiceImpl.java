package com.project.eniac.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import com.project.eniac.service.spec.HttpClientProviderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientProviderServiceImpl implements HttpClientProviderService {

	private final Map<String, CloseableHttpClient> httpClientMap = new HashMap<String, CloseableHttpClient>();

	@Value("${project.eniac.configuration.network.connention.timeout}")
	private int networkTimeOut;

	@Override
	public <T> String makeRequest(HttpUriRequest request, String clientId) {
		return this.makeRequest(request, clientId, false);
	}

	@Override
	public <T> String makeRequest(HttpUriRequest request, String clientId, boolean resetClient) {
		if (resetClient) this.resetClient(clientId);

		// Perform Request
		CloseableHttpClient httpclient = this.getHttpClient(clientId);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);
		} catch (IOException exception) {
			return "";
		} catch (IllegalStateException exception) {
			return "";
		}
	}

	@Override
	public <T> void resetClient(String clientId) {

		try {
			if (httpClientMap.containsKey(clientId)) httpClientMap.get(clientId).close();
		} catch (IOException exception) {
			log.error("Unable to close the httpClient : {}", clientId);
		}

		CloseableHttpClient httpClient = this.createHttpClient();
		httpClientMap.put(clientId, httpClient);
	}

	private CloseableHttpClient getHttpClient(String clientId) {
		if (httpClientMap.containsKey(clientId)) return httpClientMap.get(clientId);

		CloseableHttpClient httpClient = this.createHttpClient();
		httpClientMap.put(clientId, httpClient);

		return httpClient;
	}

	private CloseableHttpClient createHttpClient() {
		RequestConfig configuration = RequestConfig.custom()
				.setConnectTimeout(networkTimeOut)
				.build();

		return HttpClientBuilder.create()
				.setDefaultRequestConfig(configuration)
				.build();
	}

}
