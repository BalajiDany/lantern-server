package com.project.eniac.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.project.eniac.engine.BaseSearchEngine;
import com.project.eniac.service.spec.HttpClientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientServiceImpl implements HttpClientService {

	private final Map<String, CloseableHttpClient> httpClientMap = new HashMap<String, CloseableHttpClient>();

	@Override
	public <T> String makeRequest(HttpGet getRequest, BaseSearchEngine<T> searchEngine) {
		return this.makeRequest(getRequest, searchEngine, false);
	}

	@Override
	public <T> String makeRequest(HttpGet getRequest, BaseSearchEngine<T> searchEngine, boolean resetClient) {
		String clientId = searchEngine.getEngineName();
		if (resetClient) this.resetClient(searchEngine);

		// Perform Request
		CloseableHttpClient httpclient = this.getHttpClient(clientId);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(getRequest);
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);
		} catch (IOException exception) {
			return "";
		}
	}

	@Override
	public <T> void resetClient(BaseSearchEngine<T> searchEngine) {
		String clientId = searchEngine.getEngineName();

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
		return HttpClients.createDefault();
	}

}
