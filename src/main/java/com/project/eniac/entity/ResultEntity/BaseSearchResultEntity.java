package com.project.eniac.entity.ResultEntity;

import com.project.eniac.types.EngineCategory;

import lombok.Data;

@Data
public class BaseSearchResultEntity {

	private String engineName;

	private EngineCategory engineCategory;

}
