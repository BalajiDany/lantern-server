package com.project.eniac.service.spec;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;

public interface EngineDiagnosisService {

	void diagnosisEngine(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response);

	void disableEngine(BaseSearchEngine<?> searchEngine);

	void enableEngine(BaseSearchEngine<?> searchEngine);

}
