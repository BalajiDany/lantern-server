package com.project.eniac.engine.impl.piratebay;

import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity.TorrentSearchResultEntityBuilder;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.ConversionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
public class PirateBay10TorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private static final String BASE_URL = "https://torrent-finder.com/tpb";

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_PIRATEBAY_PROXY;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String url = new StringBuilder(BASE_URL).toString();
        try {

            URI uri = new URIBuilder(BASE_URL)
                    .addParameter("q", searchEntity.getQuery())
                    .build();

            HttpGet request = new HttpGet(uri);
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
        Elements elements = document.select("table#searchResult tr"); // Select all results

        for (Element element : elements) {
            TorrentSearchResultEntity torrentSearchResultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(torrentSearchResultEntity)) searchResultEntity.add(torrentSearchResultEntity);
        }

        SearchResultEntity.SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<TorrentSearchResultEntity>builder()
                .engineName(this.getEngineName())
                .engineType(this.getEngineType());

        // Result Delivery
        if (searchResultEntity.size() > 0) {
            return resultEntityBuilder
                    .searchResults(searchResultEntity)
                    .engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
                    .build();
        } else if (elements.size() > 0) {
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

        Element titleElement = element.selectFirst("div.detName > a");
        Element typeElement = element.selectFirst("td.vertTh > center > a");
        Element magneticLinkElement = element.selectFirst("td > a");
        Element otherDetails = element.selectFirst("td > font.detDesc");
        Elements seedersAndLeechers = element.select("td[align=right]");

        if (ObjectUtils.anyNull(titleElement, typeElement, magneticLinkElement)) return null;
        if (ObjectUtils.anyNull(otherDetails, seedersAndLeechers)) return null;

        searchResultEntity
                .torrentName(titleElement.text())
                .torrentUrl(titleElement.attr("href"))
                .category(typeElement.text())
                .magneticLink(magneticLinkElement.attr("href"));

        String otherDetail = otherDetails.text();
        if (StringUtils.isBlank(otherDetail)) return null;
        for (String detail : otherDetail.split(",")) {
            if (detail.contains("Uploaded")) {
                searchResultEntity.uploadedDate(detail.replace("Uploaded", ""));
            } else if (detail.contains("Size")) {
                searchResultEntity.torrentSize(detail.replace("Size", ""));
            }
        }

        Element seederElement = seedersAndLeechers.get(0);
        Element leecherElement = seedersAndLeechers.get(1);
        if (ObjectUtils.isEmpty(seederElement) && ObjectUtils.isEmpty(leecherElement)) return null;
        searchResultEntity.seeders(ConversionUtil.parseInt(seederElement.text()));

        return searchResultEntity.build();
    }

}
