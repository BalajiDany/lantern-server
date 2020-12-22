package com.project.eniac.engine.impl.kickass;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity.TorrentSearchResultEntityBuilder;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.ConversionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KickassTorrentSearchEngine extends TorrentSearchEngine {

	private final HttpClientProviderService httpClientProviderService;

	private static final String BASE_URL = "https://kickass.onl/";

	@Override
	public String getEngineName() {
		return EngineConstant.ENGINE_KICKASS;
	}

	@Override
	public HttpClientProviderService getHttpClientService() {
		return this.httpClientProviderService;
	}

	@Override
	public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
		String url =  new StringBuilder(BASE_URL)
				.append("usearch/")
				.append(ConversionUtil.encodeURL(searchEntity.getQuery()))
				.append("/").toString();

		try {

			URI uri = new URIBuilder(url)
					.addParameter("field", "seeders")
					.addParameter("sorder", "desc")
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
	public SearchResultEntity<TorrentSearchResultEntity> getResponse(String response) {
		List<TorrentSearchResultEntity> searchResultEntity = new ArrayList<>();

		Document document = Jsoup.parse(response);
		Elements elements = document.select("tr#torrent_latest_torrents"); // Select all results

		for (Element element: elements) {
			TorrentSearchResultEntity torrentSearchResultEntity = this.extractEntity(element);

			if (ObjectUtils.isNotEmpty(torrentSearchResultEntity)) searchResultEntity.add(torrentSearchResultEntity);
		}

		SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
				.<TorrentSearchResultEntity>builder()
				.engineName(this.getEngineName())
				.engineType(this.getEngineType());

		// Result Delivery
		if (searchResultEntity.size() > 0) {
			return resultEntityBuilder
					.searchResult(searchResultEntity)
					.engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
					.build();
		} else if (elements.isEmpty()) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SEARCH_RESULT)
					.build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN)
					.build();
		}
	}

	private TorrentSearchResultEntity extractEntity(Element element) {
		if (ObjectUtils.isEmpty(element)) return null;

		TorrentSearchResultEntityBuilder searchResultEntity = TorrentSearchResultEntity.builder();

		Element torrentNameElement = element.selectFirst("div.torrentname > div > a");
		if (ObjectUtils.isEmpty(torrentNameElement)) return null;
		searchResultEntity.torrentName(torrentNameElement.text());

		String torrentURL = torrentNameElement.attr("href");
		searchResultEntity.torrentUrl(BASE_URL + torrentURL);

		Element category = element.selectFirst("div.torrentname > div > span > strong");
		if (ObjectUtils.isEmpty(category)) return null;
		searchResultEntity.category(category.text());

		Element magnetIcon = element.selectFirst("div.iaconbox > a[rel]");
		if (ObjectUtils.isEmpty(magnetIcon)) return null;

		String href = magnetIcon.attr("href");
		String[] urls = href.split("url=");
		if (urls.length < 2) return null;

		String link = ConversionUtil.decodeURL(urls[1]);

		if (ObjectUtils.isNotEmpty(link)) {
			String cleanLink = ConversionUtil.decodeURL(link);
			searchResultEntity.magneticLink(cleanLink);
		}

		Elements otherElements = element.select("td.center");
		for (Element otherElement : otherElements) {
			if (otherElement.hasClass("nobr")) {
				searchResultEntity.torrentSize(otherElement.text());
			} else if (otherElement.hasClass("green")) {
				searchResultEntity.seeders(ConversionUtil.parseInt(otherElement.text()));
			} else if (otherElement.hasClass("red")) {
				searchResultEntity.leechers(ConversionUtil.parseInt(otherElement.text()));
			} else {
				searchResultEntity.uploadedDate(otherElement.text());
			}
		}

		return searchResultEntity.build();
	}

}
