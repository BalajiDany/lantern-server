package com.project.lantern.engine.impl.google;

import com.project.lantern.constant.RequestHeaders;
import com.project.lantern.engine.EngineConstant;
import com.project.lantern.engine.impl.google.utils.GoogleRequestUtil;
import com.project.lantern.engine.spec.GeneralSearchEngine;
import com.project.lantern.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineResultType;
import com.project.lantern.types.EngineType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GoogleGeneralSearchEngine extends GeneralSearchEngine {

    private final HttpClientProviderService httpClientService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
        .engineId(UUID.fromString("8a1cbe20-5591-44d8-a945-56074a21fd73"))
        .engineName(EngineConstant.ENGINE_GOOGLE)
        .engineType(EngineType.GENERAL)
        .hasLocationSupport(true)
        .hasLanguageSupport(true)
        .hasPaginationSupport(true)
        .maxAllowedContinuousTimeoutCount(5)
        .maxAllowedContinuousBreakdownCount(5)
        .build();

    private final EngineStateEntity engineState = EngineStateEntity.builder()
        .isEnabled(true)
        .continuousTimeoutCount(0)
        .continuousBreakdownCount(0)
        .build();

    @Override
    public EngineSpecEntity getEngineSpec() {
        return this.engineSpec;
    }

    @Override
    public EngineStateEntity getEngineState() {
        return this.engineState;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String language = GoogleRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = GoogleRequestUtil.getRegion(searchEntity.getLocation());
        String hostAddress = "www." + GoogleRequestUtil.getDomain(region);
        String pageNo = String.valueOf(searchEntity.getPageNo() * 10);

        URI uri = new URIBuilder()
            .setScheme("https").setHost(hostAddress).setPath("/search")
            .addParameter("q", searchEntity.getQuery())
            .addParameter("hl", language)
            .addParameter("gl", region)
            .addParameter("start", pageNo)
            .build();

        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(HttpHeaders.ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        return request;
    }

    @Override
    public SearchResultEntity<GeneralSearchResultEntity> getResponseEntity(String response) {

        List<GeneralSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("#search div.g");

        for (Element element : elements) {
            Set<String> classNames = element.classNames();

            // It is not proper search result
            if (classNames.size() > 1) continue;

            GeneralSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<GeneralSearchResultEntity> resultEntityBuilder = SearchResultEntity
            .<GeneralSearchResultEntity>builder()
            .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(document.selectFirst("#search"))) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private GeneralSearchResultEntity extractEntity(Element element) {

        Element anchorElement = element.selectFirst("div.yuRUbf > a");
        Element titleElement = element.selectFirst("div.yuRUbf > a > h3");
        Element contentElement = element.selectFirst("div.IsZvec span.aCOpRe");

        boolean isInvalidElement = ObjectUtils.anyNull(anchorElement, titleElement, contentElement);
        if (isInvalidElement) return null;

        String url = anchorElement.attr("href");
        String title = titleElement.text();
        String content = contentElement.text();

        boolean isInvalidContent = StringUtils.isAnyEmpty(url, title, content);
        if (isInvalidContent) return null;

        return GeneralSearchResultEntity.builder()
            .url(url).title(title).content(content)
            .build();
    }

}

/*
 * LOG DETAILS
 * URL: https://www.google.com/search?q=The+Big+Bang+Theory&hl=ja&gl=JP&start=0
 *      1. for language &hl=ja :URL
 *      2. for location &gl=JP :URL
 *      3. for pagination &start=0 :URL increment the value by 10
 *
 * *. If result has junk Response then try adding this param &ie=utf8&oe=utf8
 *
 */
