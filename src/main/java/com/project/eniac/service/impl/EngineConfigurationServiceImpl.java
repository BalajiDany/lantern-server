package com.project.eniac.service.impl;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.service.spec.EngineConfigurationService;
import com.project.eniac.types.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EngineConfigurationServiceImpl implements EngineConfigurationService {

    @Autowired(required = false)
    private final List<BaseSearchEngine<?>> searchEngines = new ArrayList<>();

    @Override
    public void resetAllEngines() {
        searchEngines.stream()
                .forEach(searchEngine -> searchEngine.resetEngine());
    }

    @Override
    public void resetByEngineType(EngineType engineType) {
        searchEngines.stream()
                .filter(searchEngines -> searchEngines.getEngineSpec().getEngineType() == engineType)
                .forEach(searchEngine -> searchEngine.resetEngine());
    }

    @Override
    public void resetLocationBasedEngines() {
        searchEngines.stream()
                .filter(searchEngines -> searchEngines.getEngineSpec().isHasLocationSupport())
                .forEach(searchEngine -> searchEngine.resetEngine());
    }

}
