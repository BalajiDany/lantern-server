package com.project.eniac.engine.google;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.VideoSearchEngine;
import com.project.eniac.engine.google.utils.GoogleRequestUtil;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GoogleVideoSearchEngine extends VideoSearchEngine {

	private final HttpClientProviderService httpClientService;

	@Override
	public String getEngineName() {
		return EngineConstant.ENGINE_GOOGLE;
	}

	@Override
	public HttpClientProviderService getHttpClientService() {
		return httpClientService;
	}

	@Override
	public HttpGet getRequest(MainSearchEntity searchEntity) {
		String language = GoogleRequestUtil.getLanguage(searchEntity.getLanguage());
		String region = GoogleRequestUtil.getRegion(searchEntity.getLocation());

		String url = new StringBuilder()
				.append("https://www.")
				.append(GoogleRequestUtil.getDomain(region))
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
					.addParameter("tbm", "vid")
//					.addParameter("safe", "high")
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
	public SearchResultEntity<VideoSearchResultEntity> getResponse(String response) {
		List<VideoSearchResultEntity> searchResultEntity = new ArrayList<VideoSearchResultEntity>();

		Document document = Jsoup.parse(response);
		Elements elements = document.select("#search > div > div > div.g"); // Select all results

		for (Element element : elements) {
			Set<String> classNames = element.classNames();

			// It is not common search result
			if (classNames.size() > 1) continue;

			Element anchorElement = element.selectFirst("div.yuRUbf > a");
			Element titleElement = element.selectFirst("div.yuRUbf > a > h3 > span");
			Element contentElement = element.selectFirst("div.IsZvec span.aCOpRe");
			Element uploadedDateElement = element.selectFirst("div.IsZvec div.fG8Fp");
			Element thumpnailElement = element.selectFirst("div.IsZvec > div > div > a");
			Element durationElement = element.selectFirst("div.ij69rd.UHe5G");

			boolean isInValidelement = anchorElement == null
					|| titleElement == null
					|| contentElement == null
					|| uploadedDateElement == null
					|| thumpnailElement == null
					|| durationElement == null;

			if (isInValidelement) continue;

			String url = anchorElement.attr("href");
			String title = titleElement.text();
			String content = contentElement.text();
			String uploadedDate = uploadedDateElement.ownText();
			String thumpnailUrl = thumpnailElement.attr("href");
			String duration = durationElement.ownText();

			boolean isInvalidContent = StringUtils.isEmpty(url)
					|| StringUtils.isEmpty(title)
					|| StringUtils.isEmpty(content)
					|| StringUtils.isEmpty(uploadedDate)
					|| StringUtils.isEmpty(thumpnailUrl)
					|| StringUtils.isEmpty(duration);

			if (isInvalidContent) continue;

			VideoSearchResultEntity resultEntity = VideoSearchResultEntity.builder()
					.url(url).title(title).content(content)
					.uploadedDate(uploadedDate).duration(duration).thumpnailUrl(thumpnailUrl).build();
			searchResultEntity.add(resultEntity);
		}

		SearchResultEntityBuilder<VideoSearchResultEntity> resultEntityBuilder = SearchResultEntity
				.<VideoSearchResultEntity>builder()
				.engineName(this.getEngineName())
				.engineType(this.getEngineType());

		// Result Delivery
		if (searchResultEntity.size() != 0) {
			return resultEntityBuilder
					.searchResult(searchResultEntity)
					.engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
					.build();
		} else if (document.select("#search").isEmpty() == false) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT).build();
		} else {
			if (document.select("#captcha-form").isEmpty() == false) log.error("Google Captch Required");
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
		}
	}

}
