package com.project.eniac.controller.api.v1;

import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;
import com.project.eniac.service.spec.SearchService;
import com.project.eniac.types.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String location) {
        SearchRequestEntity searchEntity = new SearchRequestEntity();
        searchEntity.setQuery(query);
        searchEntity.setLocation(location);

        EngineType engineType = EngineType.fromString(searchType);

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
