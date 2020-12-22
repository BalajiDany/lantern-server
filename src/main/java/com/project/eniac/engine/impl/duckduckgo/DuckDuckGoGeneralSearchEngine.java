package com.project.eniac.engine.impl.duckduckgo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.duckduckgo.utils.DuckDuckGoRequestUtil;
import com.project.eniac.engine.spec.GeneralSearchEngine;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DuckDuckGoGeneralSearchEngine extends GeneralSearchEngine {

	private final HttpClientProviderService httpClientProviderService;

	@Override
	public String getEngineName() {
		return EngineConstant.ENGINE_DUCK_DUCK_GO;
	}

	@Override
	public HttpClientProviderService getHttpClientService() {
		return httpClientProviderService;
	}

	@Override
	public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
		String url = "https://html.duckduckgo.com/html";
		String location = DuckDuckGoRequestUtil.getRegion(searchEntity.getLocation());

		try {

			URI uri = new URIBuilder(url)
					.addParameter("q", searchEntity.getQuery())
					.addParameter("kl", location)
//					.addParameter("safe", "high")
					.build();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
			request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

			return request;
		} catch (URISyntaxException exception) {
			log.error("Exception on Creating URL : {}", url);
			return null;
		}
	}

	@Override
	public SearchResultEntity<GeneralSearchResultEntity> getResponse(String response) {

		List<GeneralSearchResultEntity> searchResultEntity = new ArrayList<GeneralSearchResultEntity>();

		Document document = Jsoup.parse(response);
		Elements elements = document.select("div.result__body"); // Select all results

		for (Element element : elements) {

			Element anchorElement = element.selectFirst("h2.result__title > a");
			Element bodyElement = element.selectFirst("a.result__snippet");

			if (ObjectUtils.anyNull(anchorElement, bodyElement)) continue;

			String url = anchorElement.attr("href");
			String title = anchorElement.text();
			String content = bodyElement.text();

			boolean isInvalidContent = StringUtils.isEmpty(url)
					|| StringUtils.isEmpty(title)
					|| StringUtils.isEmpty(content);

			if (isInvalidContent) continue;

			GeneralSearchResultEntity resultEntity = GeneralSearchResultEntity.builder()
					.url(url).title(title).content(content).build();
			searchResultEntity.add(resultEntity);
		}

		SearchResultEntityBuilder<GeneralSearchResultEntity> resultEntityBuilder = SearchResultEntity
				.<GeneralSearchResultEntity>builder()
				.engineName(this.getEngineName())
				.engineType(this.getEngineType());

		// Result Delivery
		if (searchResultEntity.size() != 0) {
			return resultEntityBuilder
					.searchResult(searchResultEntity)
					.engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
					.build();
		} else if (document.select("div.result--no-result").isEmpty() == false) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT).build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
		}
	}

}
