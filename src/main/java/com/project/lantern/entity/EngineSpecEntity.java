package com.project.lantern.entity;

import com.project.lantern.types.EngineType;
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

    private int maxAllowedContinuousTimeoutCount = 5;

    private int maxAllowedContinuousBreakdownCount = 5;

}
