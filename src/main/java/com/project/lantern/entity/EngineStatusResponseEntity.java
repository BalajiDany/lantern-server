package com.project.lantern.entity;

import com.project.lantern.types.EngineType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineStatusResponseEntity {

    private String engineName;

    private EngineType engineType;

    private boolean isEnabled;

    private boolean hasLocationSupport;

    private boolean hasLanguageSupport;

    private boolean hasPaginationSupport;

    private long lastSearchDuration;

}
