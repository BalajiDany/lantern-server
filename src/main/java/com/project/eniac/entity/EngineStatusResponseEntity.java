package com.project.eniac.entity;

import com.project.eniac.types.EngineType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EngineStatusResponseEntity {

	private String engineName;

	private EngineType engineType;

	private boolean isEnabled;

	private long lastSearchDuration;

}
