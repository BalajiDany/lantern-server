package com.project.eniac.engine;

import com.project.eniac.entity.ResultEntity.VideoSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class VideoSearchEngine extends BaseSearchEngine<VideoSearchResultEntity> {

	@Override
	public EngineType getEngineType() {
		return EngineType.VIDEO;
	}

}
