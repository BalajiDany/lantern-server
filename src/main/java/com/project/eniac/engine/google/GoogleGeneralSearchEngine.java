package com.project.eniac.engine.google;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.google.service.GoogleDomainService;
import com.project.eniac.engine.google.service.GoogleDomainServiceImpl;
import com.project.eniac.engine.google.service.GoogleLanguageService;
import com.project.eniac.engine.google.service.GoogleLanguageServiceImpl;
import com.project.eniac.engine.google.service.GoogleRegionService;
import com.project.eniac.engine.google.service.GoogleRegionServiceImpl;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;

public class GoogleGeneralSearchEngine extends GeneralSearchEngine {

	private final GoogleRegionService googleRegionService;
	private final GoogleDomainService googleDomainService;
	private final GoogleLanguageService googleLanguageService;

	// TODO
	// on Setting up the bean make googleDomainProvider as a bean and inject it.
	public GoogleGeneralSearchEngine() {
		this.googleRegionService = new GoogleRegionServiceImpl();
		this.googleDomainService = new GoogleDomainServiceImpl();
		this.googleLanguageService = new GoogleLanguageServiceImpl();
	}

	@Override
	public String getEngineName() {
		return "Google";
	}

	@Override
	public HttpGet getRequest(MainSearchEntity searchEntity) {
		String language = googleLanguageService.getValidLanguage(searchEntity.getLanguage());
		String region = googleRegionService.getValidRegion(searchEntity.getLocation());

		String url = new StringBuilder()
				.append("https://www.")
				.append(googleDomainService.getDomainByLocation(region))
				// .append(googleDomainService.getRandomDomain())
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
					.append(language).append("-").append(region).append(",").append(language).append(";")
					.append("q=0.8,").append(language).append(";")
					.append("q=0.5")
					.toString();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, acceptLanguage);
			request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

			return request;
		} catch (URISyntaxException e) {
			// Disable the Engine
			return null;
		}
	}

	@Override
	public GeneralSearchResultEntity getResponse(String reponse) {
		GeneralSearchResultEntity resultEntity = GeneralSearchResultEntity.getInstanceByEngine(this);

		Document document = Jsoup.parse(reponse);
		Elements elements = document.select("div.g");

		for (Element element : elements) {
			element.select("a[href]");
		}

		resultEntity.setTitle("");
		resultEntity.setUrl("");
		resultEntity.setTitle("");

		return resultEntity;
	}

}
