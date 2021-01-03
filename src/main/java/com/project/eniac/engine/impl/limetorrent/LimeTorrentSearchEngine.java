package com.project.eniac.engine.impl.limetorrent;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.spec.TorrentSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
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
public class LimeTorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private static final String BASE_URL = "https://limetorrents.unblockninja.com/";

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_LIME_TORRENT;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String url = new StringBuilder(BASE_URL)
                .append("search/all/")
                .append(ConversionUtil.encodeURL(searchEntity.getQuery()))
                .append("/seeds/1/").toString();
        try {

            URI uri = new URIBuilder(url).build();

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
    public SearchResultEntity<TorrentSearchResultEntity> getResponse(String response) {
        List<TorrentSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("table.table2 > tbody > tr"); // Select all results

        for (Element element : elements) {
            if (!element.hasAttr("bgcolor")) continue;

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
        TorrentSearchResultEntity.TorrentSearchResultEntityBuilder searchResultEntity = TorrentSearchResultEntity.builder();

        Element seederElement = element.selectFirst("td.tdseed");
        Element leechElement = element.selectFirst("td.tdleech");
        Elements anchorElements = element.select("div.tt-name > a");
        Elements dateTypeAndSizeElements = element.select("td.tdnormal");

        if (ObjectUtils.anyNull(seederElement, leechElement)) return null;
        if (ObjectUtils.anyNull(anchorElements, dateTypeAndSizeElements)) return null;

        String seedCount = seederElement.text().replace(",", "");
        String leechCount = leechElement.text().replace(",", "");
        searchResultEntity
                .seeders(ConversionUtil.parseInt(seedCount))
                .leechers(ConversionUtil.parseInt(leechCount));

        for (Element anchorElement : anchorElements) {
            if (anchorElement.hasClass("csprite_dl14")) {
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

        if (ObjectUtils.isEmpty(splittedUrl)) return null;
        return new StringBuilder("magnet:?xt=urn:btih:")
                .append(splittedUrl[0])
                .toString();
    }

}
