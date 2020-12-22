package com.project.eniac.controller.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.SearchService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchController {

	private final SearchService searchService;

	@GetMapping("/general")
	public SearchResponseEntity<GeneralSearchResultEntity> generalSearch(String query) {
		SearchRequestEntity searchEntity = new SearchRequestEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");

		return searchService.generalSearch(searchEntity);
	}

	@GetMapping("/video")
	public SearchResponseEntity<VideoSearchResultEntity> videoSearch(String query) {
		SearchRequestEntity searchEntity = new SearchRequestEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");

		return searchService.videoSearch(searchEntity);
	}

	@GetMapping("/torrent")
	public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(String query) {
		SearchRequestEntity searchEntity = new SearchRequestEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");

		return searchService.torrentSearch(searchEntity);
	}

}
