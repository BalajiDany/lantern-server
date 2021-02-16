package com.project.lantern.engine.impl.limetorrent;

import com.project.lantern.constant.RequestHeaders;
import com.project.lantern.engine.EngineConstant;
import com.project.lantern.engine.spec.TorrentSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineResultType;
import com.project.lantern.types.EngineType;
import com.project.lantern.utils.ConversionUtil;
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
public class LimeTorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("5759f1f4-4a7d-499f-9ce3-84fab3a607e2"))
            .engineName(EngineConstant.ENGINE_LIME_TORRENT)
            .engineType(EngineType.TORRENT)
            .hasLocationSupport(false)
            .hasLanguageSupport(false)
            .hasPaginationSupport(true)
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
        return this.httpClientProviderService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String pathWithQuery = "/search/all/" + ConversionUtil.encodeURL(searchEntity.getQuery()) +
                "/seeds/" + (searchEntity.getPageNo() + 1) + "/";

        URI uri = new URIBuilder()
                .setScheme("https").setHost("limetorrents.unblockninja.com")
                .setPath(pathWithQuery)
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
        Elements elements = document.select("table.table2 > tbody > tr");

        for (Element element : elements) {
            boolean isValidRow = element.hasAttr("bgcolor");
            if (!isValidRow) continue;

            TorrentSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntity.SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<TorrentSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(elements)) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private TorrentSearchResultEntity extractEntity(Element element) {
        TorrentSearchResultEntity.TorrentSearchResultEntityBuilder searchResultEntity = TorrentSearchResultEntity.builder();

        Element seederElement = element.selectFirst("td.tdseed");
        Element leechElement = element.selectFirst("td.tdleech");
        Elements anchorElements = element.select("div.tt-name > a");
        Elements dateTypeAndSizeElements = element.select("td.tdnormal");

        boolean isInvalidElement = ObjectUtils.anyNull(seederElement, leechElement, anchorElements);
        isInvalidElement = isInvalidElement || ObjectUtils.anyNull(dateTypeAndSizeElements);

        if (isInvalidElement) return null;

        String seedCount = seederElement.text().replace(",", "");
        String leechCount = leechElement.text().replace(",", "");
        searchResultEntity
                .seeders(ConversionUtil.parseInt(seedCount))
                .leechers(ConversionUtil.parseInt(leechCount));

        for (Element anchorElement : anchorElements) {
            if (anchorElement.hasAttr("rel")) {
                String url = anchorElement.attr("href");
                String magneticLink = this.extractMagneticLink(url);
                searchResultEntity
                        .torrentUrl(url)
                        .magneticLink(magneticLink);
            } else {
                searchResultEntity.torrentName(anchorElement.text());
            }
        }

        for (Element dateTypeAndSizeElement : dateTypeAndSizeElements) {
            String content = dateTypeAndSizeElement.text();
            String[] splittedContent = content.split(" - in ");
            if (splittedContent.length > 1) {
                searchResultEntity
                        .uploadedDate(splittedContent[0])
                        .category(splittedContent[1]);
            } else {
                searchResultEntity.torrentSize(content);
            }
        }

        return searchResultEntity.build();
    }

    private String extractMagneticLink(String url) {
        if (StringUtils.isBlank(url)) return null;

        String domainRemoved = url.replace("http://itorrents.org/torrent/", "");
        String[] splittedUrl = domainRemoved.split(".torrent");

        return "magnet:?xt=urn:btih:" + splittedUrl[0];
    }

}

/*
 * LOG DETAILS
 * URL: https://limetorrents.unblockninja.com/search/all/The+Big+Bang/seeds/1/
 *      1. for order use [seeds] (path variable) :URL for date use [date]
 *      2. for pagination it is in path variable.
 *
 * * It will return only the core magnetic links, Trackers need to be added if need.
 *
 */
