package com.project.lantern.service.impl;

import com.project.lantern.engine.spec.BaseSearchEngine;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.entity.EngineStatusResponseEntity;
import com.project.lantern.entity.EngineStatusResponseEntity.EngineStatusResponseEntityBuilder;
import com.project.lantern.service.spec.EngineStatusService;
import com.project.lantern.types.EngineType;
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
