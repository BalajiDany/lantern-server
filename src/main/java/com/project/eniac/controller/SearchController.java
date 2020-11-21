package com.project.eniac.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.service.spec.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@RequestMapping("/all")
	List<GeneralSearchResultEntity> searchAll(MainSearchEntity searchEntity){
		return searchService.searchAll(searchEntity);
	}

	@RequestMapping("/videos")
	List<GeneralSearchResultEntity> searchVideos(MainSearchEntity searchEntity){
		return null;
	}

	@RequestMapping("/images")
	List<GeneralSearchResultEntity> searchImages(MainSearchEntity searchEntity){
		return null;
	}

}
