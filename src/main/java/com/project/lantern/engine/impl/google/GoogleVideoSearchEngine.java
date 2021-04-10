package com.project.lantern.engine.impl.google;

import com.project.lantern.constant.CommonRegex;
import com.project.lantern.constant.RequestHeaders;
import com.project.lantern.engine.EngineConstant;
import com.project.lantern.engine.impl.google.utils.GoogleRequestUtil;
import com.project.lantern.engine.spec.VideoSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.lantern.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.service.spec.HttpClientProviderService;
import com.project.lantern.types.EngineResultType;
import com.project.lantern.types.EngineType;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;

@Slf4j
@RequiredArgsConstructor
public class GoogleVideoSearchEngine extends VideoSearchEngine {

    private static final String YOUTUBE_SITE = "site:.youtube.com";
    private static final String THUMBNAIL_URL = "https://i.ytimg.com/vi/{0}/default.jpg";
    private final HttpClientProviderService httpClientService;
    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
        .engineId(UUID.fromString("3f336ebc-862a-4c4f-b540-9bed5bfb8aa0"))
        .engineName(EngineConstant.ENGINE_GOOGLE_VIDEO)
        .engineType(EngineType.VIDEO)
        .hasLocationSupport(true)
        .hasLanguageSupport(true)
        .hasPaginationSupport(true)
        .maxAllowedContinuousTimeoutCount(5)
        .maxAllowedContinuousBreakdownCount(5)
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
        return httpClientService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        String language = GoogleRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = GoogleRequestUtil.getRegion(searchEntity.getLocation());
        String hostAddress = "www." + GoogleRequestUtil.getDomain(region);
        String pageNo = String.valueOf(searchEntity.getPageNo() * 10);
        String searchQuery = searchEntity.getQuery() + StringUtils.SPACE + YOUTUBE_SITE;

        URI uri = new URIBuilder()
            .setScheme("https").setHost(hostAddress).setPath("/search")
            .addParameter("q", searchQuery)
            .addParameter("hl", language)
            .addParameter("gl", region)
            .addParameter("tbm", "vid")
            .addParameter("start", pageNo)
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
        Elements elements = document.select("#search div.g");

        for (Element element : elements) {
            Set<String> classNames = element.classNames();

            // It is not proper search result
            if (classNames.size() > 1) continue;

            VideoSearchResultEntity resultEntity = this.extractEntity(element);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<VideoSearchResultEntity> resultEntityBuilder = SearchResultEntity
            .<VideoSearchResultEntity>builder()
            .searchResults(searchResultEntity);

        // Result Delivery
        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(document.selectFirst("#search"))) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private VideoSearchResultEntity extractEntity(Element element) {

        Element anchorElement = element.selectFirst("div.yuRUbf > a");
        Element titleElement = element.selectFirst("div.yuRUbf > a > h3");
        Element contentElement = element.selectFirst("div.IsZvec span.aCOpRe");
        Element uploadedDateElement = element.selectFirst("div.IsZvec div.fG8Fp");
        Element durationElement = element.selectFirst("div.ij69rd.UHe5G");

        boolean isInvalidElement = ObjectUtils.anyNull(anchorElement, titleElement, contentElement);
        isInvalidElement = isInvalidElement || ObjectUtils.anyNull(uploadedDateElement, durationElement);
        if (isInvalidElement) return null;

        String url = anchorElement.attr("href");
        String title = titleElement.text();
        String content = contentElement.text();
        String uploadedDate = uploadedDateElement.ownText();
        String thumbnailUrl = this.extractThumbnailUrl(url);
        String duration = durationElement.ownText();

        boolean isInvalidContent = StringUtils.isAnyEmpty(url, title, content);
        isInvalidContent = isInvalidContent || StringUtils.isAnyEmpty(uploadedDate, thumbnailUrl, duration);
        if (isInvalidContent) return null;

        return VideoSearchResultEntity.builder()
            .url(url).title(title).content(content)
            .duration(duration)
            .uploadedDate(uploadedDate)
            .thumbnailUrl(thumbnailUrl)
            .build();
    }

    private String extractThumbnailUrl(String url) {
        if (StringUtils.isBlank(url)) return url;
        String videoId = this.extractYoutubeId(url);

        if (StringUtils.isBlank(videoId)) return url;
        return MessageFormat.format(THUMBNAIL_URL, videoId);
    }

    public String extractYoutubeId(String url) {
        Matcher matcher = CommonRegex.YOUTUBE_VIDEO_ID_PATTERN.matcher(url);
        return matcher.find() ? matcher.group(1) : StringUtils.EMPTY;
    }

}

/*
 * LOG Details
 * URL: https://www.google.com/search?q=Big+Bang+site:.youtube.com&hl=en&gl=JP&tbm=vid&start=0
 *      1. +site:.youtube.com in search query to search only in youtube.
 *      2. for video search &tbm=vid :URL
 *      3. for language &hl=ja :URL
 *      4. for location &gl=JP :URL
 *      5. for pagination &start=0 :URL increment the value by 10
 *      6. if document has id #captcha-form then engine blocks ip.
 *
 * *. If result has junk Response then try adding this param &ie=utf8&oe=utf8
 *
 */
