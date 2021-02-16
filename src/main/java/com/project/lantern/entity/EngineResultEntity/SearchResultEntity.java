package com.project.lantern.entity.EngineResultEntity;

import com.project.lantern.types.EngineResultType;
import com.project.lantern.types.EngineType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResultEntity<T> {

    private String engineName;

    private List<T> searchResults;

    private EngineType engineType;

    private EngineResultType engineResultType;

    private long duration;

}
