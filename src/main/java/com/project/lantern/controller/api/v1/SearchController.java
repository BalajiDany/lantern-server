package com.project.lantern.controller.api.v1;

import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.entity.SearchResponseEntity;
import com.project.lantern.service.spec.SearchService;
import com.project.lantern.types.EngineType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{searchType}")
    public SearchResponseEntity<?> search(
            @PathVariable("searchType") String searchType,
            @RequestParam String query,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String language) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);
        searchEntity.setLanguage(language);

        EngineType engineType = EngineType.fromString(searchType);
        log.info("Searching: {} [{}]", query, engineType);

        switch (engineType) {
            case GENERAL:
                return searchService.generalSearch(searchEntity);
            case TORRENT:
                return searchService.torrentSearch(searchEntity);
            case VIDEO:
                return searchService.videoSearch(searchEntity);
            case CODE:
                return searchService.codeSearch(searchEntity);
            default:
                return null;
        }
    }

}
