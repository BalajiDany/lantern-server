package com.project.eniac.engine.impl.bing;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.bing.utils.BingRequestUtil;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class BingVideoSearchEngine extends VideoSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("5458d198-7474-454a-8add-b5a1ebfd2d30"))
            .engineName(EngineConstant.ENGINE_BING_VIDEO)
            .engineType(EngineType.VIDEO)
            .hasLocationSupport(false)
            .hasLanguageSupport(false)
            .hasPaginationSupport(false)
            .build();

    private final EngineStateEntity engineState = EngineStateEntity.builder()
            .isEnabled(true)
            .continuousTimeoutCount(0)
            .continuousBreakdownCount(0)
            .build();

    private static final int SEARCH_COUNT = 10;
    private static final String THUMBNAIL_URL = "https://th.bing.com/th?id={0}&w=249&h=140&c=7&rs=1&pid=2.1";

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
        String language = BingRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = BingRequestUtil.getRegion(searchEntity.getLocation());
        int startCount = searchEntity.getPageNo() * SEARCH_COUNT;

        URI uri = new URIBuilder()
                .setScheme("https").setHost("www.bing.com").setPath("/videos/asyncv2")
                .addParameter("q", searchEntity.getQuery())
                .addParameter("setlang", language)
                .addParameter("setmkt", region)
                .addParameter("first", Integer.toString(startCount))
                .addParameter("count", Integer.toString(SEARCH_COUNT))
                .build();

        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(HttpHeaders.ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        return request;
    }

    @Override
    public SearchResultEntity<VideoSearchResultEntity> getResponseEntity(String response) {
        List<VideoSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements results = document.select("div.dg_u");

        for (Element result : results) {
            VideoSearchResultEntity resultEntity = this.extractEntity(result);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<VideoSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<VideoSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (searchResultEntity.size() != 0) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (!document.select("div.nr_indicator").isEmpty()) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private VideoSearchResultEntity extractEntity(Element result) {

        // title, duration, thumbnai, url,
        Element videoHeaderDataElement = result.selectFirst("div.vrhdata");
        Elements metaRowElement = result.select("div.mc_vtvc_meta_block > div.mc_vtvc_meta_row > span");

        if (ObjectUtils.anyNull(videoHeaderDataElement, metaRowElement)) return null;

        String videoHeaderData = videoHeaderDataElement.attr("vrhm");
        if (StringUtils.isBlank(videoHeaderData)) return null;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(videoHeaderData);
        } catch (JSONException ignored) {
            return null;
        }

        String title = jsonObject.getString("vt");
        String duration = jsonObject.getString("du");
        String videoUrl = jsonObject.getString("pgurl");
        String thumbnailId = jsonObject.getString("thid");

        if (StringUtils.isAnyEmpty(title, duration, videoUrl, thumbnailId)) return null;

        String uploadedDate = "";
        if (metaRowElement.size() > 0) {
            boolean isSingleElement = metaRowElement.size() == 1;
            Element dataElement = isSingleElement ? metaRowElement.get(0) : metaRowElement.get(1);
            uploadedDate = dataElement.text();
        }

        return VideoSearchResultEntity.builder()
                .title(title)
                .content(title)
                .url(videoUrl)
                .uploadedDate(uploadedDate)
                .duration(duration)
                .thumbnailUrl(MessageFormat.format(THUMBNAIL_URL, thumbnailId))
                .build();
    }

}

/*
 * LOG DETAILS
 * URL: https://www.bing.com/videos/asyncv2?q=The+Big+Bang+Theory&setlang=en&setmkt=en-us&first=0&count=10
 *      1. for language &setlang=en :URL
 *      2. for location &setmkt=en-us :URL
 *      3. for pagination &first=0 &count=10 :URL It is kind of offset and count, we are using 10
 *      4. US and CN Location is not supported directly
 *
 *
 */
