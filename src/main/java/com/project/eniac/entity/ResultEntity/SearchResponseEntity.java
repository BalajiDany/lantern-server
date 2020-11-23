package com.project.eniac.entity.ResultEntity;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponseEntity<T> {

	private long searchDuration;

	private List<SearchResultEntity<T>> searchResult;

}
