package com.project.eniac.engine.google;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.google.service.GoogleDomainService;
import com.project.eniac.engine.google.service.GoogleDomainServiceImpl;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GoogleGeneralSearchEngine extends GeneralSearchEngine {

	private final GoogleDomainService googleDomainService;

	private final Map<String, String> locationOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CH", "US");
		}
	};

	private final Map<String, String> languageOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("de", "en");
		}
	};

	// TODO
	// on Setting up the bean make googleDomainProvider as a bean and inject it.
	public GoogleGeneralSearchEngine() {
		this.googleDomainService = new GoogleDomainServiceImpl();
	}

	@Override
	public String getEngineName() {
		return "Google";
	}

	@Override
	public HttpGet getRequest(MainSearchEntity searchEntity) {
		String language = searchEntity.getLanguage();
		String region = searchEntity.getLocation();
		
		if (locationOverideMap.containsKey(region)) region = locationOverideMap.get(region);
		if (languageOverideMap.containsKey(region)) region = languageOverideMap.get(region);

		String url = new StringBuilder()
				.append("https://www.")
				.append(googleDomainService.getDomainByLocation(region))
				.append("/search")
				.toString();
		try {

			URI uri = new URIBuilder(url)
					.addParameter("q", searchEntity.getQuery())
					.addParameter("hl", language)
					.addParameter("lang", language)
					.addParameter("lr", "lang_" + language)
					.addParameter("gl", region)
					.addParameter("ie", "utf8")
					.addParameter("oe", "utf8")
					.addParameter("safe", "high")
					.build();

			String acceptLanguage = new StringBuilder()
					.append(language).append("-").append(region).append(",")
					.append(language).append(";q=0.8,")
					.append(language).append(";q=0.5")
					.toString();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, acceptLanguage);
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
		Elements elements = document.select("#search > div > div > div.g"); // Select all results

		for (Element element : elements) {
			Set<String> classNames = element.classNames();

			// It is not common search result
			if (classNames.size() > 1) continue;

			Element anchorElement = element.selectFirst("div.yuRUbf > a");
			Element titleElement = element.selectFirst("div.yuRUbf > a > h3 > span");
			Element contentElement = element.selectFirst("div.IsZvec span.aCOpRe");

			boolean isInValidelement = anchorElement == null
					|| titleElement == null
					|| contentElement == null;

			if (isInValidelement) continue;

			String url = anchorElement.attr("href");
			String title = titleElement.text();
			String content = contentElement.text();

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
		} else if (document.select("#search").isEmpty()) {
			// if contains id captcha-form then google blocked this ip

			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT).build();
		}

	}

}
