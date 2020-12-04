package com.project.eniac.controller.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResponseEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;
import com.project.eniac.service.spec.CommonSearchService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
public class SearchV1Controller {

	private final CommonSearchService searchService;

	@GetMapping("/general")
	SearchResponseEntity<GeneralSearchResultEntity> searchGeneral(String query){
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");
		return searchService.generalSearch(searchEntity);
	}

	@GetMapping("/torrent")
	SearchResponseEntity<TorrentSearchResultEntity> searchVideos(String query){
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");
		return searchService.torrentSearch(searchEntity);
	}

	@GetMapping("/video")
	SearchResponseEntity<VideoSearchResultEntity> searchImages(String query){
		MainSearchEntity searchEntity = new MainSearchEntity();
		searchEntity.setQuery(query);
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");
		return searchService.videoSearch(searchEntity);
	}

}