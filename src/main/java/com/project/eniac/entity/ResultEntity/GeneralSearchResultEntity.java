package com.project.eniac.entity.ResultEntity;

import com.project.eniac.engine.BaseSearchEngine;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeneralSearchResultEntity extends BaseSearchResultEntity {

	private String title;

	private String url;

	private String content;

	public static GeneralSearchResultEntity getInstanceByEngine(BaseSearchEngine<?> searchEngine) {
		GeneralSearchResultEntity resultEngine = new GeneralSearchResultEntity();

		resultEngine.setEngineName(searchEngine.getEngineName());
		resultEngine.setEngineCategory(searchEngine.getEngineCategory());

		return resultEngine;
	}
}
