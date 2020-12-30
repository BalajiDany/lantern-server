package com.project.eniac.engine.spec;

import com.project.eniac.entity.EngineResultEntity.CodeSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class CodeSearchEngine extends BaseSearchEngine<CodeSearchResultEntity> {

    @Override
    public EngineType getEngineType() {
        return EngineType.CODE;
    }
}
