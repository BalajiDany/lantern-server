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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class PirateBayTorrentSearchEngine extends TorrentSearchEngine {

    private final HttpClientProviderService httpClientService;

    private final EngineSpecEntity engineSpec = EngineSpecEntity.builder()
            .engineId(UUID.fromString("0a8758ad-980c-41a6-8e2d-5dbdf8c61efe"))
            .engineName(EngineConstant.ENGINE_PIRATEBAY)
            .engineType(EngineType.TORRENT)
            .hasLocationSupport(false)
            .hasLanguageSupport(false)
            .hasPaginationSupport(false)
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
        return this.httpClientService;
    }

    @Override
    @SneakyThrows
    public HttpUriRequest getSearchRequest(SearchRequestEntity searchEntity) {
        URI uri = new URIBuilder()
                .setScheme("https").setHost("apibay.org").setPath("/q.php")
                .addParameter("q", searchEntity.getQuery())
                .addParameter("cat", "0")
                .build();

        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(HttpHeaders.ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        return request;
    }

    @Override
    public SearchResultEntity<TorrentSearchResultEntity> getResponseEntity(String response) {

        List<TorrentSearchResultEntity> searchResultEntity = new ArrayList<>();
        JSONArray resultArray = null;
        try {
            resultArray = new JSONArray(response);
        } catch (JSONException ignored) {
            log.error("Exception on Parsing the Response : {}", response);
        }

        int size = ObjectUtils.isEmpty(resultArray) ? 0 : resultArray.length();
        for (int count = 0; count < size; count++) {
            JSONObject resultObject = resultArray.getJSONObject(count);
            if (resultObject.getLong("id") == 0) break;

            TorrentSearchResultEntity resultEntity = this.extractEntity(resultObject);
            if (ObjectUtils.isNotEmpty(resultEntity)) searchResultEntity.add(resultEntity);
        }

        SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
                .<TorrentSearchResultEntity>builder()
                .searchResults(searchResultEntity);

        if (ObjectUtils.isNotEmpty(searchResultEntity)) {
            resultEntityBuilder.engineResultType(EngineResultType.FOUND_SEARCH_RESULT);
        } else if (ObjectUtils.isNotEmpty(resultArray)) {
            resultEntityBuilder.engineResultType(EngineResultType.NO_SEARCH_RESULT);
        } else {
            resultEntityBuilder.engineResultType(EngineResultType.ENGINE_BREAK_DOWN);
        }

        return resultEntityBuilder.build();
    }

    private TorrentSearchResultEntity extractEntity(JSONObject jsonObject) {

        TorrentSearchResultEntityBuilder searchResultEntityBuilder = TorrentSearchResultEntity.builder();

        String torrentName = jsonObject.getString("name");
        if (StringUtils.isBlank(torrentName)) return null;

        String infoHash = jsonObject.getString("info_hash");
        long torrentSize = jsonObject.getLong("size");
        long torrentId = jsonObject.getLong("id");
        long uploadedDate = jsonObject.getLong("added");
        int category = jsonObject.getInt("category");
        int seeders = jsonObject.getInt("seeders");
        int leechers = jsonObject.getInt("leechers");

        return searchResultEntityBuilder
                .torrentName(torrentName)
                .torrentSize(ConversionUtil.convertBytesToReadable(torrentSize))
                .torrentUrl("https://thepiratebay.org/description.php?id=" + torrentId)
                .magneticLink("magnet:?xt=urn:btih:" + infoHash)
                .uploadedDate(ConversionUtil.parseDate(uploadedDate))
                .category(TorrentUtil.getCategory(category))
                .seeders(seeders)
                .leechers(leechers)
                .build();
    }

}

/*
 * LOG DETAILS
 * URL: https://apibay.org/q.php?q=The+Big+Bang+Theory&cat=0
 *      1.
 * * No Pagination or sort support As of Now.
 * * It will return only the core magnetic links, Trackers need to be added if need.
 * * For Trackers Ref: https://github.com/JimmyLaurent/torrent-search-api/blob/master/lib/providers/thepiratebay.js#L28
 *
 */
