package com.project.eniac.engine;

import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.types.EngineCategory;

public abstract class GeneralSearchEngine implements BaseSearchEngine<GeneralSearchResultEntity> {

	@Override
	public EngineCategory getEngineCategory() {
		return EngineCategory.GENERAL;
	}

}
