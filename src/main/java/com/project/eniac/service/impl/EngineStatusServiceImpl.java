package com.project.eniac.service.impl;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.entity.EngineSpecEntity;
import com.project.eniac.entity.EngineStateEntity;
import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.entity.EngineStatusResponseEntity.EngineStatusResponseEntityBuilder;
import com.project.eniac.service.spec.EngineStatusService;
import com.project.eniac.types.EngineType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EngineStatusServiceImpl implements EngineStatusService {

    @Autowired(required = false)
    private final List<BaseSearchEngine<?>> searchEngines = new ArrayList<>();

    @Override
    public List<EngineStatusResponseEntity> getEngineStatus(EngineType engineType) {
        return searchEngines.stream()
                .filter(searchEngine -> searchEngine.getEngineSpec().getEngineType() == engineType)
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
        EngineSpecEntity engineSpecEntity = searchEngine.getEngineSpec();
        EngineStateEntity engineStateEntity = searchEngine.getEngineState();

        statusResponseEntity.engineName(engineSpecEntity.getEngineName());
        statusResponseEntity.engineType(engineSpecEntity.getEngineType());
        statusResponseEntity.isEnabled(engineStateEntity.isEnabled());

        statusResponseEntity.hasLanguageSupport(engineSpecEntity.isHasLanguageSupport());
        statusResponseEntity.hasLocationSupport(engineSpecEntity.isHasLocationSupport());
        statusResponseEntity.hasPaginationSupport(engineSpecEntity.isHasPaginationSupport());

        return statusResponseEntity.build();
    }

}
