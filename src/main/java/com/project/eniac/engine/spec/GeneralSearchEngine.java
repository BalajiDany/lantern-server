package com.project.eniac.engine.spec;

import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class GeneralSearchEngine extends BaseSearchEngine<GeneralSearchResultEntity> {

	@Override
	public EngineType getEngineType() {
		return EngineType.GENERAL;
	}

}
