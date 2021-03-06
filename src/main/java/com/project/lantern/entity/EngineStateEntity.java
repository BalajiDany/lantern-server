package com.project.lantern.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineStateEntity {

    private boolean isEnabled;

    private int continuousBreakdownCount;

    private int continuousTimeoutCount;

}
