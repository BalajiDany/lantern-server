package com.project.eniac.engine.spec;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.types.EngineType;
import com.project.eniac.utils.UserAgent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;

@Data
@Slf4j
public abstract class BaseSearchEngine<T> {

	private boolean enabled = true;

	private int breakdownCountInRow = 0;

	private int timeoutCountInRow = 0;

	abstract public String getEngineName();

	abstract public EngineType getEngineType();

	abstract public HttpClientProviderService getHttpClientService();

	abstract public HttpUriRequest getRequest(SearchRequestEntity searchEntity);

	abstract public SearchResultEntity<T> getResponse(String response);

	public SearchResultEntity<T> performSearch(SearchRequestEntity searchEntity) {
		long startTime = System.currentTimeMillis();

		HttpUriRequest getRequest = this.getRequest(searchEntity);

		String userAgent = UserAgent.getRandomUserAgent();
		getRequest.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);

		HttpClientProviderService httpClientService = this.getHttpClientService();
		log.info("URL : {}", getRequest.getURI().toString());
		String response = httpClientService.makeRequest(getRequest, this.getEngineName());

		SearchResultEntity<T> responseEntity;

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;

		// Time Out
		if (StringUtils.isBlank(response)) {
			responseEntity = SearchResultEntity
					.<T>builder()
					.engineName(this.getEngineName())
					.engineType(this.getEngineType())
					.engineResultType(EngineResultType.ENGINE_TIME_OUT)
					.build();
		} else {
			responseEntity = this.getResponse(response);
		}
		responseEntity.setDuration(runTime);
		return responseEntity;
	}

}
