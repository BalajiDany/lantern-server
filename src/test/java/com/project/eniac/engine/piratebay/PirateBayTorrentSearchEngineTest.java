package com.project.eniac.engine.piratebay;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.spec.CommonLanguageService;
import com.project.eniac.service.spec.CommonLocationService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.UserAgent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class PirateBayTorrentSearchEngineTest {

	@Autowired
	private CommonLanguageService commonLanguageService;

	@Autowired
	private CommonLocationService commonLocationService;

	@Autowired
	private PirateBayTorrentSearchEngine pirateBayTorrentSearchEngine;

	private final static String SEARCH_QUERY = "The Big Bang Theory";

	@Test
	void verifySearch() {
		SearchResultEntity<TorrentSearchResultEntity> entity = performSearch(SEARCH_QUERY);
		logResponse(entity);
	}

	private SearchResultEntity<TorrentSearchResultEntity> performSearch(String searchQuery) {
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(searchQuery);
		searchEntity.setLanguage(commonLanguageService.getDefaultLanguage());
		searchEntity.setLocation(commonLocationService.getDefaultLocation());

		HttpGet request = pirateBayTorrentSearchEngine.getRequest(searchEntity);
		String userAgent = UserAgent.getRandomUserAgent();
		request.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);

		String response = makeRequest(request);

		return pirateBayTorrentSearchEngine.getResponse(response);
	}

	private <T> void logResponse(SearchResultEntity<T> entity) {
		log.info("Engine Name   : " + entity.getEngineName());
		log.info("Engine Type   : " + entity.getEngineType());
		log.info("Engine Result : " + entity.getEngineResultType());

		if (entity.getEngineResultType() == EngineResultType.FOUND_SEARCH_RESULT) {
			List<T> searchResultList = entity.getSearchResult();
			log.info("Result Count  : " + searchResultList.size());

			for (T searchResult : searchResultList) {
				log.info(searchResult.toString());
			}
		}
	}

	private String makeRequest(HttpGet httpGet) {

		System.out.println(" ======================================================= ");
		System.out.println(httpGet.toString());
		for (Header header : httpGet.getAllHeaders()) {
			System.out.println(header.getName() + " : " + header.getValue());
		}
		System.out.println(" ======================================================= ");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);
		} catch (IOException e) {
			return "";
		}
	}

}
