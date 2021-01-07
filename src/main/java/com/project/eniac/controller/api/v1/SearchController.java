package com.project.eniac.controller.api.v1;

import com.project.eniac.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;
import com.project.eniac.service.spec.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/general")
    public SearchResponseEntity<GeneralSearchResultEntity> generalSearch(String query, String location) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);
        searchEntity.setLanguage("en");

        return searchService.generalSearch(searchEntity);
    }

    @GetMapping("/video")
    public SearchResponseEntity<VideoSearchResultEntity> videoSearch(String query, String location) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);
        searchEntity.setLanguage("en");

        return searchService.videoSearch(searchEntity);
    }

    @GetMapping("/torrent")
    public SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(String query, String location) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);
        searchEntity.setLanguage("en");

        return searchService.torrentSearch(searchEntity);
    }

    @GetMapping("/code")
    public SearchResponseEntity<CodeSearchResultEntity> codeSearch(String query, String location) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);
        searchEntity.setLanguage("en");

        return searchService.codeSearch(searchEntity);
    }

}
