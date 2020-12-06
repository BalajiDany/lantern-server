package com.project.eniac.entity;

import java.util.List;

import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponseEntity<T> {

	private long searchDuration;

	private List<SearchResultEntity<T>> searchResult;

}
