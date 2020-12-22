package com.project.eniac.engine.spec;

import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class VideoSearchEngine extends BaseSearchEngine<VideoSearchResultEntity> {

    @Override
    public EngineType getEngineType() {
        return EngineType.VIDEO;
    }

}
