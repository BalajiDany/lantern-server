package com.project.eniac.engine.impl.bing;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.bing.utils.BingRequestUtil;
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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
public class BingGeneralSearchEngine extends GeneralSearchEngine {

    private final HttpClientProviderService httpClientService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("124218e8-1e66-41cc-9ff4-c68c6e01c2c4"))
            .engineName(EngineConstant.ENGINE_BING)
            .engineType(EngineType.GENERAL)
            .hasLocationSupport(false)
            .hasLanguageSupport(false)
            .hasPaginationSupport(false)
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
        String language = BingRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = BingRequestUtil.getRegion(searchEntity.getLocation());

        int offset = ((searchEntity.getPageNo() - 1) * 10) + 7;
        offset = Math.max(offset, 0);

        URI uri = new URIBuilder()
                .setScheme("https").setHost("www.bing.com").setPath("/search")
                .addParameter("q", searchEntity.getQuery())
                .addParameter("setlang", language)
                .addParameter("setmkt", region)
                .addParameter("first", Integer.toString(offset))
                .build();

        HttpGet request = new HttpGet(uri);
        request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        return request;
    }

    @Override
    public SearchResultEntity<GeneralSearchResultEntity> getResponseEntity(String response) {

        List<GeneralSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("li.b_algo");

        for (Element element : elements) {
            GeneralSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<GeneralSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<GeneralSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(document.selectFirst("#b_results .b_no"))) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private GeneralSearchResultEntity extractEntity(Element element) {

        Element anchorElement = element.selectFirst("h2 > a");
        Element bodyElement = element.selectFirst("div.b_caption > p");
        if (ObjectUtils.isEmpty(bodyElement)) {
            bodyElement = element.selectFirst("div.tab-content p.b_paractl");
        }

        boolean isInvalidElements = ObjectUtils.anyNull(anchorElement, bodyElement);
        if (isInvalidElements) return null;

        String url = anchorElement.attr("href");
        String title = anchorElement.text();
        String content = bodyElement.text();

        boolean isInvalidContent = ObjectUtils.anyNull(url, title, content);
        if (isInvalidContent) return null;

        return GeneralSearchResultEntity.builder()
                .url(url).title(title).content(content)
                .build();
    }

}

/*
 * LOG DETAILS
 * URL: https://www.bing.com/search?q=The+Big+Bang+Theory&setlang=en&setmkt=en-us&first=21
 *      1. for language &setlang=en :URL
 *      2. for location &setmkt=en-us :URL
 *      3. for pagination &first=0 :URL It is kind of offset value
 *          page 0 = 0
 *          page n = ((n - 1) * 10) + 7 [consider each page(other than page 0) has 14 result]
 *      4. US and CN Location is not supported directly
 *
 * * Both Bing and BingParachute scraping way is similar, so making any change consider other
 *
 */
