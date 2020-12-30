package com.project.eniac.entity;

import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseEntity<T> {

    private long duration;

    private List<SearchResultEntity<T>> searchResults;

}
