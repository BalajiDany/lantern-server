package com.project.eniac.entity.EngineResultEntity;

import java.util.List;

import com.project.eniac.types.EngineResultType;
import com.project.eniac.types.EngineType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResultEntity<T> {

	private String engineName;

	private List<T> searchResult;

	private EngineType engineType;

	private EngineResultType engineResultType;

	private long duration;

}
