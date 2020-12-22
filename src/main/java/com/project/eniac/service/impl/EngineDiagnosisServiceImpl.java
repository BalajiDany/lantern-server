package com.project.eniac.service.impl;

import com.project.eniac.engine.spec.BaseSearchEngine;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.service.spec.EngineDiagnosisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

// TODO Move the count and enable flag to db.
@Slf4j
@RequiredArgsConstructor
public class EngineDiagnosisServiceImpl implements EngineDiagnosisService {

    private static final int MAX_ALLOWED_BREAKDOWN_COUNT_IN_ROW = 5;

    private static final int MAX_ALLOWED_TIMEOUT_COUNT_IN_ROW = 5;

    private static final int ENGINE_HALT_TIME = 6000;

    private final TaskScheduler taskScheduler;

    @Override
    @Async("engineDiagnosisTaskExecutor")
    public void diagnosisEngine(BaseSearchEngine<?> searchEngine, SearchResultEntity<?> response) {
        switch (response.getEngineResultType()) {
            case ENGINE_BREAK_DOWN:
                performEngineBreakdownDiagnosis(searchEngine);
                break;
            case ENGINE_TIME_OUT:
                performEngineTimeoutDiagnosis(searchEngine);
                break;
            default:
                searchEngine.setTimeoutCountInRow(0);
                searchEngine.setBreakdownCountInRow(0);
        }
    }

    @Override
    public void disableEngine(BaseSearchEngine<?> searchEngine) {
        if (!searchEngine.isEnabled()) return;

        log.info("Disabling the Engine : {} Type : {}",
                searchEngine.getEngineName(), searchEngine.getEngineType());
        searchEngine.setEnabled(false);
    }

    @Override
    public void enableEngine(BaseSearchEngine<?> searchEngine) {
        if (searchEngine.isEnabled()) return;

        log.info("Enabling the Engine : {} Type : {}",
                searchEngine.getEngineName(), searchEngine.getEngineType());
        searchEngine.setEnabled(true);
    }

    private void performEngineBreakdownDiagnosis(BaseSearchEngine<?> searchEngine) {
        int breakdownCount = searchEngine.getBreakdownCountInRow();
        searchEngine.setBreakdownCountInRow(++breakdownCount);
        disableEngine(searchEngine);

        if (searchEngine.getBreakdownCountInRow() > MAX_ALLOWED_BREAKDOWN_COUNT_IN_ROW) {
            log.error("Shutting down the engine : {} type : {} Reason: Reached Max allowed breakdown",
                    searchEngine.getEngineName(), searchEngine.getEngineType());
        } else {
            Date enableDate = new Date(System.currentTimeMillis() + ENGINE_HALT_TIME);
            taskScheduler.schedule(() -> enableEngine(searchEngine), enableDate);
        }
    }

    private void performEngineTimeoutDiagnosis(BaseSearchEngine<?> searchEngine) {
        int timeoutCount = searchEngine.getTimeoutCountInRow();
        searchEngine.setTimeoutCountInRow(++timeoutCount);

        if (searchEngine.getTimeoutCountInRow() > MAX_ALLOWED_TIMEOUT_COUNT_IN_ROW) {
            disableEngine(searchEngine);
            log.error("Shutting down the engine : {} type : {} Reason: Reached Max allowed timeout",
                    searchEngine.getEngineName(), searchEngine.getEngineType());
        }
    }

}
