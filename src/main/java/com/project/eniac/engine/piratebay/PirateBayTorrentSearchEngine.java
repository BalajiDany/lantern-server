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
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity.SearchResultEntityBuilder;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.spec.HttpClientService;
import com.project.eniac.types.EngineResultType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PirateBayTorrentSearchEngine extends TorrentSearchEngine {
	
	private final HttpClientService httpClientService;

	@Override
	public HttpClientService getHttpClientService() {
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
		} else if (resultArray == null) {
			return resultEntityBuilder
					.engineResultType(EngineResultType.ENGINE_BREAK_DOWN)
					.build();
		} else {
			return resultEntityBuilder
					.engineResultType(EngineResultType.NO_SERACH_RESULT)
					.build();
		}
	}

	private TorrentSearchResultEntity formEntity(JSONObject jsonObject) {
		if (jsonObject == null) return null;

		String torrentName = jsonObject.getString("name");
		String torrentSize = jsonObject.getString("size");
		String torrentUrl = jsonObject.getString("id");
		String magneticLink = jsonObject.getString("info_hash");
		String uploadedDate = jsonObject.getString("added");
		String category = jsonObject.getString("category");
		int seeders = jsonObject.getInt("seeders");
		int leechers = jsonObject.getInt("leechers");

		return TorrentSearchResultEntity.builder()
				.torrentName(torrentName)
				.torrentSize(torrentSize)
				.torrentUrl(torrentUrl)
				.magneticLink(magneticLink)
				.uploadedDate(uploadedDate)
				.category(category)
				.seeders(seeders)
				.leechers(leechers)
				.build();
	}

}
