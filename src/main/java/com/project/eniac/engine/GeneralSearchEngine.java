package com.project.eniac.engine;

import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class GeneralSearchEngine implements BaseSearchEngine<GeneralSearchResultEntity> {

	@Override
	public EngineType getEngineType() {
		return EngineType.GENERAL;
	}

}
