package com.project.eniac.engine.impl.yahoo;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.yahoo.utils.YahooRequestUtil;
import com.project.eniac.engine.spec.GeneralSearchEngine;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineSpecEntity;
import com.project.eniac.entity.EngineStateEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.types.EngineType;
import com.project.eniac.utils.ConversionUtil;
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
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class YahooGeneralSearchEngine extends GeneralSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("9ac857a2-2a80-45d0-8883-a3072a9c3003"))
            .engineName(EngineConstant.ENGINE_YAHOO)
            .engineType(EngineType.GENERAL)
            .hasLocationSupport(true)
            .hasLanguageSupport(false)
            .hasPaginationSupport(true)
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
        return this.httpClientProviderService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String domain = YahooRequestUtil.getDomainByLocation(searchEntity.getLocation());
        int resultCount = 10;
        int offset = searchEntity.getPageNo() * resultCount;

        URI uri = new URIBuilder()
                .setScheme("https").setHost(domain).setPath("/search")
                .addParameter("p", searchEntity.getQuery())
                .setParameter("b", Integer.toString(offset))
                .setParameter("pz", Integer.toString(resultCount))
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
        Elements elements = document.select("div.relsrch");

        for (Element element : elements) {
            GeneralSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<GeneralSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<GeneralSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(document.select("#results"))) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private GeneralSearchResultEntity extractEntity(Element element) {

        Element anchorElement = element.selectFirst("div.compTitle > h3 > a");
        Element contentElement = element.selectFirst("div.compText > p");

        boolean isInValidElement = ObjectUtils.anyNull(anchorElement, contentElement);
        if (isInValidElement) return null;

        String url = anchorElement.attr("href");
        String title = anchorElement.text();
        String content = contentElement.text();

        boolean isInvalidContent = StringUtils.isAnyEmpty(url, title, content);
        if (isInvalidContent) return null;

        String extractedUrl = this.extractURL(url);
        return GeneralSearchResultEntity.builder()
                .url(extractedUrl).title(title).content(content)
                .build();
    }

    private String extractURL(String url) {
        String yahooExtracted = extractYahooClick(url);
        return yahooExtracted.contains("www.bing.com/aclick")
                ? this.extractBingClick(yahooExtracted) : yahooExtracted;
    }

    private String extractYahooClick(String url) {
        for (String splitURL : url.split("/")) {
            if (!splitURL.startsWith("RU=")) continue;

            String extractedURL = splitURL.replace("RU=", "");
            String correctedUrl = ConversionUtil.decodeURL(extractedURL);

            return StringUtils.isEmpty(correctedUrl) ? url : correctedUrl;
        }
        return url;
    }

    private String extractBingClick(String url) {
        for (String splitURL : url.split("&")) {
            if (!splitURL.startsWith("u=")) continue;

            String extractedURL = splitURL.replace("u=", "");
            String base64DecodedURL = ConversionUtil.base64UrlDecoder(extractedURL);
            String correctedUrl = ConversionUtil.decodeURL(base64DecodedURL);

            return StringUtils.isEmpty(correctedUrl) ? url : correctedUrl;
        }
        return url;
    }

}

/*
 * LOG DETAILS
 * URL: https://in.search.yahoo.com/search?p=The+Big+Bang+Theory&b=0&pz=10
 *      1. for location :DOMAIN
 *      2. no language support but based on location. (Can't Configure)
 *      3. for pagination &b=0 &pz=10 :URL b is offset and pz is result count
 *
 */