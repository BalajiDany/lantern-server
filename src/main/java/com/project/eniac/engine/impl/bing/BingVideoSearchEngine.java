package com.project.eniac.engine.impl.bing;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.EngineConstant;
import com.project.eniac.engine.impl.bing.utils.BingRequestUtil;
import com.project.eniac.engine.spec.VideoSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BingVideoSearchEngine extends VideoSearchEngine {

    private final HttpClientProviderService httpClientProviderService;

    @Override
    public String getEngineName() {
        return EngineConstant.ENGINE_BING_VIDEO;
    }

    @Override
    public HttpClientProviderService getHttpClientService() {
        return this.httpClientProviderService;
    }

    @Override
    public HttpUriRequest getRequest(SearchRequestEntity searchEntity) {
        String language = BingRequestUtil.getLanguage(searchEntity.getLanguage());
        String region = BingRequestUtil.getRegion(searchEntity.getLocation());

        String url = "https://www.bing.com/video/search";

        try {

            URI uri = new URIBuilder(url)
                    .addParameter("q", searchEntity.getQuery())
                    .addParameter("setlang", language)
                    .addParameter("setmkt", region)
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
        Elements results = document.select("div.dg_u");

        for (Element result : results) {
            VideoSearchResultEntity resultEntity = this.extractEntity(result);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<VideoSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<VideoSearchResultEntity>builder()
                .engineName(this.getEngineName())
                .engineType(this.getEngineType());

        // Result Delivery
        if (searchResultEntity.size() != 0) {
            return resultEntityBuilder
                    .searchResults(searchResultEntity)
                    .engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
                    .build();
        } else if (!document.select("div.nr_indicator").isEmpty()) {
            return resultEntityBuilder
                    .engineResultType(EngineResultType.NO_SEARCH_RESULT).build();
        } else {
            return resultEntityBuilder
                    .engineResultType(EngineResultType.ENGINE_BREAK_DOWN).build();
        }
    }

    private VideoSearchResultEntity extractEntity(Element result) {

        Element titleElement = result.selectFirst("div.mc_vtvc_title");
        Element durationElement = result.selectFirst("div.mc_bc_rc_w.b_smText > div.items");
        Element thumbnailUrlElement = result.selectFirst("div.mc_vtvc_th > div.cico > div.rms_iac");

        Elements metaRowElement = result.select("div.mc_vtvc_meta_block > div.mc_vtvc_meta_row > span");
        Element videoUrlElement = result.selectFirst("div.vrhdata");

        if (ObjectUtils.anyNull(titleElement, metaRowElement, durationElement)) return null;
        if (ObjectUtils.anyNull(thumbnailUrlElement, videoUrlElement)) return null;
        if (metaRowElement.size() == 0) return null;
        Element uploadedDateElement = metaRowElement.size() < 2 ? metaRowElement.get(0) : metaRowElement.get(1);

        String videoUrl = this.extractVideoUrl(videoUrlElement.attr("vrhm"));
        if (StringUtils.isBlank(videoUrl)) return null;

        String title = titleElement.text();

        return VideoSearchResultEntity.builder()
                .title(title)
                .content(title)
                .url(videoUrl)
                .uploadedDate(uploadedDateElement.text())
                .duration(durationElement.text())
                .thumbnailUrl(thumbnailUrlElement.attr("data-src"))
                .build();
    }

    private String extractVideoUrl(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            return object.getString("pgurl");
        } catch (JSONException exception) {
            log.error("Exception on Parsing the Response : {}", jsonString);
        }
        return null;
    }
}
