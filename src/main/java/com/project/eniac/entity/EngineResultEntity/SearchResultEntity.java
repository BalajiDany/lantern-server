package com.project.eniac.entity.EngineResultEntity;

import com.project.eniac.types.EngineResultType;
import com.project.eniac.types.EngineType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResultEntity<T> {

    private String engineName;

    private List<T> searchResult;

    private EngineType engineType;

    private EngineResultType engineResultType;

    private long duration;

}
