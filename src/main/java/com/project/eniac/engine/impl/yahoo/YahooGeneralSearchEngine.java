package com.project.eniac.engine.impl.yahoo;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.yahoo.utils.YahooRequestUtil;
import com.project.eniac.engine.spec.GeneralSearchEngine;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.ConversionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class YahooGeneralSearchEngine extends GeneralSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_YAHOO;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String domain = YahooRequestUtil.getDomainByLocation(searchEntity.getLocation());

        String url = new StringBuilder()
                .append("https://")
                .append(domain)
                .append("/search")
                .toString();
        try {

            URI uri = new URIBuilder(url)
                    .addParameter("p", searchEntity.getQuery())
                    .addParameter("fr", "yfp-t")
                    .addParameter("fp", "1")
                    .addParameter("toggle", "1")
                    .addParameter("cop", "mss")
                    .addParameter("ie", "utf8")
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

        List<GeneralSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("div.relsrch"); // Select all results

        for (Element element : elements) {
            Element anchorElement = element.selectFirst("div.compTitle > h3 > a");
            Element contentElement = element.selectFirst("div.compText > p");

            boolean isInValidElement = anchorElement == null
                    || contentElement == null;

            if (isInValidElement) continue;

            String url = anchorElement.attr("href");
            String title = anchorElement.text();
            String content = contentElement.text();

            boolean isInvalidContent = StringUtils.isEmpty(url)
                    || StringUtils.isEmpty(title)
                    || StringUtils.isEmpty(content);

            if (isInvalidContent) continue;

            GeneralSearchResultEntity resultEntity = GeneralSearchResultEntity.builder()
                    .url(this.extractURL(url)).title(title).content(content).build();
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
        } else if (!document.select("#results").isEmpty()) {
            return resultEntityBuilder
                    .engineResultType(EngineResultType.NO_SEARCH_RESULT).build();
        } else {
            return resultEntityBuilder
                    .engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
        }
    }

    private String extractURL(String url) {
        String yahooExtracted = extractYahooClick(url);
        return yahooExtracted.contains("www.bing.com/aclick") ? this.extractBingClick(yahooExtracted) : yahooExtracted;
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
