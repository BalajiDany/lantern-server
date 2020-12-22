package com.project.eniac.service.spec;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;

public interface EngineDiagnosisService {

	public void diagnosisEngine(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response);

	public void disableEngine(BaseSearchEngine<?> searchEngine);

	public void enableEngine(BaseSearchEngine<?> searchEngine);

}
