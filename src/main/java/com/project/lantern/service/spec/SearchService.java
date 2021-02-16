package com.project.lantern.service.spec;

import com.project.lantern.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.lantern.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.lantern.entity.SearchRequestEntity;
import com.project.lantern.entity.SearchResponseEntity;

public interface SearchService {

    SearchResponseEntity<GeneralSearchResultEntity> generalSearch(SearchRequestEntity searchEntity);

    SearchResponseEntity<VideoSearchResultEntity> videoSearch(SearchRequestEntity searchEntity);

    SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(SearchRequestEntity searchEntity);

    SearchResponseEntity<CodeSearchResultEntity> codeSearch(SearchRequestEntity searchEntity);

}
