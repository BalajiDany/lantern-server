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
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;

public class GoogleGeneralSearchEngine extends GeneralSearchEngine {

	@Override
	public String getEngineName() {
		return "Google";
	}

	@Override
	public HttpGet getRequest(MainSearchEntity searchEntity) {
		String url = "https://www.google.co.in/search";
		String language = searchEntity.getLanguage();
		String location = searchEntity.getLocation();
		String languageAndLocation = language + "-" + location;

		try {
			URI uri = new URIBuilder(url)
					.addParameter("q", searchEntity.getQuery())
					.addParameter("hl", languageAndLocation)
					.addParameter("lr", "lang_" + language)
					.addParameter("ie", "utf8").addParameter("oe", "utf8").addParameter("safe", "high")
					.build();

			String acceptLanguage = new StringBuilder()
					.append(languageAndLocation + "," + language + ";")
					.append("q=0.8," + language + ";")
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
		
		for (Element element: elements) {
			element.select("a[]");
		}

		resultEntity.setTitle("");
		resultEntity.setUrl("");
		resultEntity.setTitle("");

		return resultEntity;
	}
}
