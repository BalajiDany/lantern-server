package com.project.eniac.service.spec;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.ResultEntity.SearchResponseEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;

public interface CommonSearchService {

	SearchResponseEntity<GeneralSearchResultEntity> generalSearch(MainSearchEntity searchEntity);

	SearchResponseEntity<VideoSearchResultEntity> videoSearch(MainSearchEntity searchEntity);

	SearchResponseEntity<TorrentSearchResultEntity> torrentSearch(MainSearchEntity searchEntity);

}
