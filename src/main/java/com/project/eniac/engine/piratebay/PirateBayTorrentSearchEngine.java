package com.project.eniac.engine.piratebay;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.TorrentSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity.TorrentSearchResultEntityBuilder;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.ConvertionUtil;
import com.project.eniac.utils.TorrentUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PirateBayTorrentSearchEngine extends TorrentSearchEngine {

	private final HttpClientProviderService httpClientService;

	@Override
	public HttpClientProviderService getHttpClientService() {
		return this.httpClientService;
	}

	@Override
	public String getEngineName() {
		return "Pirate Bay";
	}

	@Override
	public HttpGet getRequest(MainSearchEntity searchEntity) {
		String language = searchEntity.getLanguage();
		String region = searchEntity.getLocation();

		String url = "https://apibay.org/q.php";

		try {

			URI uri = new URIBuilder(url)
					.addParameter("q", searchEntity.getQuery())
					.addParameter("cat", "0")
					.build();

			String acceptLanguage = new StringBuilder()
					.append(language).append("-").append(region).append(",")
					.append(language).append(";q=0.9")
					.toString();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, acceptLanguage);
			request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

			return request;
		} catch (URISyntaxException exception) {
			log.error("Exception on Creating URL : {}", url);
			return null;
		}
	}

	@Override
	public SearchResultEntity<TorrentSearchResultEntity> getResponse(String response) {

		List<TorrentSearchResultEntity> searchResultEntity = new ArrayList<TorrentSearchResultEntity>();
		JSONArray resultArray = null;
		try {
			resultArray = new JSONArray(response);
			int size = resultArray.length();

			for (int count = 0; count < size; count++) {
				JSONObject resultObject = resultArray.getJSONObject(count);

				long id = resultObject.getLong("id");
				if (id == 0) break;

				// Extract Data
				TorrentSearchResultEntity entity = this.formEntity(resultObject);

				if (entity.getTorrentName() == null) continue;

				searchResultEntity.add(entity);
			}
		} catch (JSONException exception) {
			log.error("Exception on Parsing the Response : {}", response);
		}

		SearchResultEntityBuilder<TorrentSearchResultEntity> resultEntityBuilder = SearchResultEntity
				.<TorrentSearchResultEntity>builder()
				.engineName(this.getEngineName())
				.engineType(this.getEngineType());

		// Result Delivery
		if (searchResultEntity.size() > 0) {
			return resultEntityBuilder
					.searchResult(searchResultEntity)
					.engineResultType(EngineResultType.FOUND_SEARCH_RESULT)
					.build();
		} else if (resultArray != null) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT)
					.build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN)
					.build();
		}
	}

	private TorrentSearchResultEntity formEntity(JSONObject jsonObject) {
		if (jsonObject == null) return null;

		TorrentSearchResultEntityBuilder searchResultEntityBuilder = TorrentSearchResultEntity.builder();

		String torrentName = jsonObject.getString("name");
		searchResultEntityBuilder.torrentName(torrentName);

		long torrentSize = jsonObject.getLong("size");
		searchResultEntityBuilder.torrentSize(ConvertionUtil.convertBytesToReadable(torrentSize));

		long torrentId = jsonObject.getLong("id");
		searchResultEntityBuilder.torrentUrl("https://thepiratebay.org/description.php?id=" + torrentId);

		String infoHash = jsonObject.getString("info_hash");
		searchResultEntityBuilder.magneticLink("magnet:?xt=urn:btih:" + infoHash);

		long uploadedDate = jsonObject.getLong("added");
		searchResultEntityBuilder.uploadedDate(ConvertionUtil.parseDate(uploadedDate));

		int category = jsonObject.getInt("category");
		searchResultEntityBuilder.category(TorrentUtil.getCategory(category));

		int seeders = jsonObject.getInt("seeders");
		searchResultEntityBuilder.seeders(seeders);

		int leechers = jsonObject.getInt("leechers");
		searchResultEntityBuilder.leechers(leechers);

		return searchResultEntityBuilder.build();
	}

}
