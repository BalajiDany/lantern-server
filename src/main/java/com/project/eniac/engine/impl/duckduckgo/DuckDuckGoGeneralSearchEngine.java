package com.project.eniac.engine.impl.duckduckgo;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.duckduckgo.utils.DuckDuckGoRequestUtil;
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
public class DuckDuckGoGeneralSearchEngine extends GeneralSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("349adcf2-52f7-4a19-a746-cf4b8a713180"))
            .engineName(EngineConstant.ENGINE_DUCK_DUCK_GO)
            .engineType(EngineType.TORRENT)
            .hasLocationSupport(true)
            .hasLanguageSupport(false)
            .hasPaginationSupport(false)
            .maxAllowdedContinousTimeoutCount(5)
            .maxAllowdedContinousBreakdownCount(5)
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
        return httpClientProviderService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String location = DuckDuckGoRequestUtil.getRegion(searchEntity.getLocation());

        URI uri = new URIBuilder()
                .setScheme("https").setHost("html.duckduckgo.com").setPath("/html")
                .addParameter("q", searchEntity.getQuery())
                .addParameter("kl", location)
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
        Elements elements = document.select("div.result__body");

        for (Element element : elements) {
            GeneralSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<GeneralSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<GeneralSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (searchResultEntity.size() != 0) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (!document.select("div.result--no-result").isEmpty()) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private GeneralSearchResultEntity extractEntity(Element element) {

        // Block Add
        Element addElement = element.selectFirst("a.badge--ad");
        if (ObjectUtils.isNotEmpty(addElement)) return null;

        Element anchorElement = element.selectFirst("h2.result__title > a");
        Element bodyElement = element.selectFirst("a.result__snippet");

        boolean isInvalidElement = ObjectUtils.anyNull(anchorElement, bodyElement);
        if (isInvalidElement) return null;

        String url = anchorElement.attr("href");
        String title = anchorElement.text();
        String content = bodyElement.text();

        boolean isInvalidContent = StringUtils.isAnyEmpty(url, title, content);
        if (isInvalidContent) return null;

        return GeneralSearchResultEntity.builder()
                .url(this.extractUrl(url)).title(title).content(content)
                .build();
    }

    private String extractUrl(String url) {
        for (String splitURL : url.split("\\?")) {
            if (!splitURL.startsWith("uddg=")) continue;

            String extractedURL = splitURL.replace("uddg=", "");
            String correctedUrl = ConversionUtil.decodeURL(extractedURL);

            return StringUtils.isEmpty(correctedUrl) ? url : correctedUrl;
        }
        return url;
    }

}

/*
 * LOG DETAILS
 * URL: https://html.duckduckgo.com/html?q=The+Big+Bang+Theory&kl=us-en
 *      1. for language &kl=us-en :URL
 *
 * * We use plain html duck duck go.
 * * As of now only language is supported.
 *
 */