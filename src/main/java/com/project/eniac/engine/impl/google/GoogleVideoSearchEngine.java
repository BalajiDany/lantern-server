package com.project.eniac.engine.impl.google;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.google.utils.GoogleRequestUtil;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
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
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class GoogleVideoSearchEngine extends VideoSearchEngine {

    private final HttpClientProviderService httpClientService;

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_GOOGLE;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return httpClientService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String language = GoogleRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = GoogleRequestUtil.getRegion(searchEntity.getLocation());

        String url = new StringBuilder()
                .append("https://www.")
                .append(GoogleRequestUtil.getDomain(region))
                .append("/search")
                .toString();
        try {

            URI uri = new URIBuilder(url)
                    .addParameter("q", searchEntity.getQuery())
                    .addParameter("hl", language)
                    .addParameter("lang", language)
                    .addParameter("lr", "lang_" + language)
                    .addParameter("gl", region)
                    .addParameter("ie", "utf8")
                    .addParameter("oe", "utf8")
                    .addParameter("tbm", "vid")
//					.addParameter("safe", "high")
                    .build();

            HttpGet request = new HttpGet(uri);
            request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
            request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

            return request;
        } catch (URISyntaxException exception) {
            log.error("Exception on Creating URL : {}", url);
            log.error("\t Additional Param Region : {} and Language : {}", region, language);
            return null;
        }
    }

    @Override
    public SearchResultEntity<VideoSearchResultEntity> getResponse(String response) {
        List<VideoSearchResultEntity> searchResultEntity = new ArrayList<>();

        Document document = Jsoup.parse(response);
        Elements elements = document.select("#search > div > div > div.g"); // Select all results

        for (Element element : elements) {
            Set<String> classNames = element.classNames();

            // It is not common search result
            if (classNames.size() > 1) continue;

            Element anchorElement = element.selectFirst("div.yuRUbf > a");
            Element titleElement = element.selectFirst("div.yuRUbf > a > h3 > span");
            Element contentElement = element.selectFirst("div.IsZvec span.aCOpRe");
            Element uploadedDateElement = element.selectFirst("div.IsZvec div.fG8Fp");
            Element thumbnailElement = element.selectFirst("div.IsZvec > div > div > a");
            Element durationElement = element.selectFirst("div.ij69rd.UHe5G");

            boolean isInValidElement = anchorElement == null
                    || titleElement == null
                    || contentElement == null
                    || uploadedDateElement == null
                    || thumbnailElement == null
                    || durationElement == null;

            if (isInValidElement) continue;

            String url = anchorElement.attr("href");
            String title = titleElement.text();
            String content = contentElement.text();
            String uploadedDate = uploadedDateElement.ownText();
            String thumbnailUrl = thumbnailElement.attr("href");
            String duration = durationElement.ownText();

            boolean isInvalidContent = StringUtils.isEmpty(url)
                    || StringUtils.isEmpty(title)
                    || StringUtils.isEmpty(content)
                    || StringUtils.isEmpty(uploadedDate)
                    || StringUtils.isEmpty(thumbnailUrl)
                    || StringUtils.isEmpty(duration);

            if (isInvalidContent) continue;

            VideoSearchResultEntity resultEntity = VideoSearchResultEntity.builder()
                    .url(url).title(title).content(content)
                    .uploadedDate(uploadedDate).duration(duration).thumbnailUrl(thumbnailUrl).build();
            searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<VideoSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<VideoSearchResultEntity>builder()
                .engineName(this.getEngineName())
                .engineType(this.getEngineType());

        // Result Delivery
        if (searchResultEntity.size() != 0) {
            return resultEntityBuilder
                    .searchResult(searchResultEntity)
                    .engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
                    .build();
        } else if (!document.select("#search").isEmpty()) {
            return resultEntityBuilder
                    .engineResultType(EngineResultType.NO_SEARCH_RESULT).build();
        } else {
            if (!document.select("#captcha-form").isEmpty()) log.error("Google Captch Required");
            return resultEntityBuilder
                    .engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
        }
    }

}
