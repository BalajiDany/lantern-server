package com.project.eniac.entity;

import com.project.eniac.types.EngineType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EngineSpecEntity {

    private UUID engineId;

    private String engineName;

    private EngineType engineType;

    private String scheme;

    private String host;

    private String path;

    private boolean hasLanguageSupport;

    private boolean hasLocationSupport;

    private boolean hasPaginationSupport;

    private int maxAllowdedContinousTimeoutCount = 5;

    private int maxAllowdedContinousBreakdownCount = 5;

}
