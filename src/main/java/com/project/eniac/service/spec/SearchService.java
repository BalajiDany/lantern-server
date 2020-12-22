package com.project.eniac.service.spec;

import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.entity.SearchRequestEntity;
import com.project.eniac.entity.SearchResponseEntity;

public interface SearchService {

    SearchResponseEntity<GeneralSearchResultEntity> generalSearch(SearchRequestEntity searchEntity);

    SearchResponseEntity<VideoSearchResultEntity> videoSearch(SearchRequestEntity searchEntity);

    SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(SearchRequestEntity searchEntity);

}
