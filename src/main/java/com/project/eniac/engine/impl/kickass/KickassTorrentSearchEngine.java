package com.project.eniac.engine.impl.kickass;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity.TorrentSearchResultEntityBuilder;
import com.project.eniac.entity.EngineSpecEntity;
import com.project.eniac.entity.EngineStateEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.types.EngineType;
import com.project.eniac.utils.ConversionUtil;
import com.project.eniac.utils.TorrentUtil;
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
public class KickassTorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private static final String BASE_URL = "https://kickass.onl";

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("09f3237b-c0a9-449a-8205-a5cbcfb11fbc"))
            .engineName(EngineConstant.ENGINE_KICKASS)
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

        String pathWithQuery = "/usearch/" + ConversionUtil.encodeURL(searchEntity.getQuery()) +
                "/" + (searchEntity.getPageNo() + 1) + "/";

        URI uri = new URIBuilder()
                .setScheme("https").setHost("kickass.onl").setPath(pathWithQuery)
                .addParameter("field", "seeders")
                .addParameter("sorder", "desc")
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
        Elements elements = document.select("tr#torrent_latest_torrents");

        for (Element element : elements) {
            TorrentSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<TorrentSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isEmpty(elements)) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private TorrentSearchResultEntity extractEntity(Element element) {

        TorrentSearchResultEntityBuilder searchResultEntity = TorrentSearchResultEntity.builder();

        Element torrentNameElement = element.selectFirst("div.torrentname > div > a");
        Element category = element.selectFirst("div.torrentname > div > span > strong");
        Element magnetIcon = element.selectFirst("div.iaconbox > a[rel]");
        Elements otherElements = element.select("td.center");

        boolean isInvalidElement = ObjectUtils.anyNull(torrentNameElement, category, magnetIcon);
        isInvalidElement = isInvalidElement || ObjectUtils.anyNull(otherElements);
        if (isInvalidElement) return null;

        String torrentURL = torrentNameElement.attr("href");
        String magneticLink = this.extractMagneticUrl(magnetIcon);

        searchResultEntity.torrentName(torrentNameElement.text());
        searchResultEntity.torrentUrl(BASE_URL + torrentURL);
        searchResultEntity.category(category.text());
        searchResultEntity.magneticLink(magneticLink);

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

    private String extractMagneticUrl(Element magnetIcon) {
        String href = magnetIcon.attr("href");
        String[] urls = href.split("url=");
        if (urls.length < 2) return StringUtils.EMPTY;

        String link = ConversionUtil.decodeURL(urls[1]);
        if (StringUtils.isBlank(link)) return StringUtils.EMPTY;

        String cleanLink = ConversionUtil.decodeURL(link);
        return TorrentUtil.extractCoreMagneticLink(cleanLink);
    }

}

/*
 * LOG DETAILS
 * URL: https://kickass.onl/usearch/The+Big+Bang+Theory/1/?field=seeders&sorder=desc
 *      1. for order use &field=seeder :URL (for date use time_add)
 *      2. for desc use &order=desc
 *      3. for pagination it is in path variable.
 *
 * * It will return only the core magnetic links, Trackers need to be added if need.
 */
