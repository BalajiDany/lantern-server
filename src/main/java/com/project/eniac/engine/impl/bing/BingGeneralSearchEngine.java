package com.project.eniac.engine.impl.bing;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.bing.utils.BingRequestUtil;
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
public class BingGeneralSearchEngine extends GeneralSearchEngine {

	private final HttpClientProviderService httpClientService;

	@Override
	public String getEngineName() {
		return EngineConstant.ENGINE_BING;
	}

	@Override
	public HttpClientProviderService getHttpClientService() {
		return this.httpClientService;
	}

	@Override
	public HttpGet getRequest(SearchRequestEntity searchEntity) {
		String language = BingRequestUtil.getLanguage(searchEntity.getLanguage());
		String region = BingRequestUtil.getRegion(searchEntity.getLocation());

		String url = "https://www.bing.com/search";

		try {

			URI uri = new URIBuilder(url)
					.addParameter("q", searchEntity.getQuery())
					.addParameter("setlang", language)
					.addParameter("setmkt", region)
					.build();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
			request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

			return request;
		} catch (URISyntaxException exception) {
			log.error("Exception on Creating URL : {}", url);
			log.error("\t Additional Param Region : {} and Language : {}", region, language);
			return null;
		}

	}

	@Override
	public SearchResultEntity<GeneralSearchResultEntity> getResponse(String response) {

		List<GeneralSearchResultEntity> searchResultEntity = new ArrayList<GeneralSearchResultEntity>();

		Document document = Jsoup.parse(response);
		Elements elements = document.select("li.b_algo"); // Select all results

		for (Element element : elements) {

			Element anchorElement = element.selectFirst("h2 > a");
			Element bodyElement = element.selectFirst("div.b_caption > p");

			if (anchorElement == null) continue;
			if (bodyElement == null) {
				bodyElement = element.selectFirst("div.tab-content p.b_paractl");
				if (bodyElement == null) continue;
			}

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
		} else if (document.select("#b_results .b_no").isEmpty() == false) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT).build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
		}
	}

}
