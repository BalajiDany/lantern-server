package com.project.eniac.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.entity.EngineStatusResponseEntity.EngineStatusResponseEntityBuilder;
import com.project.eniac.service.spec.EngineStatusService;
import com.project.eniac.types.EngineType;

public class EngineStatusServiceImpl implements EngineStatusService {

	@Autowired
	private List<BaseSearchEngine<?>> searchEngines;

	@Override
	public List<EngineStatusResponseEntity> getEngineStatus(EngineType engineType) {
		return searchEngines.stream()
				.filter(searchEngine -> searchEngine.getEngineType() == engineType)
				.map(this::formResponseEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<EngineStatusResponseEntity> getEngineStatus() {
		return searchEngines.stream()
				.map(this::formResponseEntity)
				.collect(Collectors.toList());
	}

	private EngineStatusResponseEntity formResponseEntity(BaseSearchEngine<?> searchEngine) {
		EngineStatusResponseEntityBuilder statusResponseEntity = EngineStatusResponseEntity.builder();

		statusResponseEntity.engineName(searchEngine.getEngineName());
		statusResponseEntity.engineType(searchEngine.getEngineType());
		statusResponseEntity.isEnabled(searchEngine.isEnabled());

		return statusResponseEntity.build();
	}

}
