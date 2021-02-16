package com.project.lantern.service.spec;

import com.project.lantern.engine.spec.BaseSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;

public interface EngineDiagnosisService {

    void diagnosisEngine(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response);

    void disableEngine(BaseSearchEngine<?> searchEngine);

    void enableEngine(BaseSearchEngine<?> searchEngine);

}
