package com.project.lantern.engine.impl.piratebay;

import com.project.lantern.constant.RequestHeaders;
import com.project.lantern.engine.EngineConstant;
import com.project.lantern.engine.spec.TorrentSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.lantern.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.TorrentSearchResultEntity.TorrentSearchResultEntityBuilder;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineResultType;
import com.project.lantern.types.EngineType;
import com.project.lantern.utils.ConversionUtil;
import com.project.lantern.utils.TorrentUtil;
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
public class PirateBay10TorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
        .engineId(UUID.fromString("4911ca35-a762-468c-8879-08981982da0d"))
        .engineName(EngineConstant.ENGINE_PIRATEBAY_PROXY)
        .engineType(EngineType.TORRENT)
        .hasLocationSupport(false)
        .hasLanguageSupport(false)
        .hasPaginationSupport(false)
        .maxAllowedContinuousTimeoutCount(5)
        .maxAllowedContinuousBreakdownCount(5)
        .build();

    private final EngineStateEntity engineState = EngineStateEntity.builder()
        .isEnabled(false)
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

        URI uri = new URIBuilder()
            .setScheme("https").setHost("torrent-finder.com").setPath("/tpb")
            .addParameter("q", searchEntity.getQuery())
            .build();

        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(HttpHeaders.ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        return request;
    }

    @Override
    public SearchResultEntity<TorrentSearchResultEntity> getResponseEntity(String response) {
        List<TorrentSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("table#searchResult tr");

        for (Element element : elements) {
            TorrentSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
            .<TorrentSearchResultEntity>builder()
            .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity.size())) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(elements.size())) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private TorrentSearchResultEntity extractEntity(Element element) {
        if (ObjectUtils.isEmpty(element)) return null;
        TorrentSearchResultEntityBuilder searchResultEntity = TorrentSearchResultEntity.builder();

        Element titleElement = element.selectFirst("div.detName > a");
        Element typeElement = element.selectFirst("td.vertTh > center > a");
        Element magneticLinkElement = element.selectFirst("td > a");
        Element otherDetails = element.selectFirst("td > font.detDesc");
        Elements seedersAndLeechers = element.select("td[align=right]");

        if (ObjectUtils.anyNull(titleElement, typeElement, magneticLinkElement)) return null;
        if (ObjectUtils.anyNull(otherDetails, seedersAndLeechers)) return null;
        if (seedersAndLeechers.size() < 2) return null;

        Element seederElement = seedersAndLeechers.get(0);
        Element leecherElement = seedersAndLeechers.get(1);

        String otherDetail = otherDetails.text();
        String magneticLink = magneticLinkElement.attr("href");
        if (StringUtils.isBlank(otherDetail)) return null;

        searchResultEntity
            .torrentName(titleElement.text())
            .torrentUrl(titleElement.attr("href"))
            .category(typeElement.text())
            .magneticLink(TorrentUtil.extractCoreMagneticLink(magneticLink))
            .seeders(ConversionUtil.parseInt(seederElement.text()))
            .leechers(ConversionUtil.parseInt(leecherElement.text()));

        for (String detail : otherDetail.split(",")) {
            if (detail.contains("Uploaded")) {
                searchResultEntity.uploadedDate(detail.replace("Uploaded", "").trim());
            } else if (detail.contains("Size")) {
                searchResultEntity.torrentSize(detail.replace("Size", "").trim());
            }
        }

        return searchResultEntity.build();
    }

}

/*
 * LOG DETAILS
 * URL: https://torrent-finder.com/tpb?q=The+Big+Bang+Theory
 *      1. No Pagination support
 *      2. Just Proxy for PirateBay Search Api.
 *
 * * It Is disabled, only proxy for PirateBay,
 * * Unstable
 * * No Pagination or sort support.
 * * It will return only the core magnetic links, Trackers need to be added if need.
 *
 */
