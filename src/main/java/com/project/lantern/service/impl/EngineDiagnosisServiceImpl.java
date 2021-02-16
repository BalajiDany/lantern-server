package com.project.lantern.service.impl;

import com.project.lantern.engine.spec.BaseSearchEngine;
import com.project.lantern.entity.EngineResultEntity.SearchResultEntity;
import com.project.lantern.entity.EngineSpecEntity;
import com.project.lantern.entity.EngineStateEntity;
import com.project.lantern.service.spec.EngineDiagnosisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

// TODO Move the count and enable flag to db.
@Slf4j
@RequiredArgsConstructor
public class EngineDiagnosisServiceImpl implements EngineDiagnosisService {

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
                EngineStateEntity engineState = searchEngine.getEngineState();
                engineState.setContinuousBreakdownCount(0);
                engineState.setContinuousTimeoutCount(0);
        }
    }

    @Override
    public void disableEngine(BaseSearchEngine<?> searchEngine) {
        EngineStateEntity engineState = searchEngine.getEngineState();
        EngineSpecEntity engineSpec = searchEngine.getEngineSpec();
        if (!engineState.isEnabled()) return;

        log.info("Disabling the Engine : {} Type : {}",
                engineSpec.getEngineName(), engineSpec.getEngineType());
        engineState.setEnabled(false);
    }

    @Override
    public void enableEngine(BaseSearchEngine<?> searchEngine) {
        EngineStateEntity engineState = searchEngine.getEngineState();
        EngineSpecEntity engineSpec = searchEngine.getEngineSpec();
        if (engineState.isEnabled()) return;

        log.info("Enabling the Engine : {} Type : {}",
                engineSpec.getEngineName(), engineSpec.getEngineType());
        engineState.setEnabled(true);
    }

    private void performEngineBreakdownDiagnosis(BaseSearchEngine<?> searchEngine) {
        EngineStateEntity engineState = searchEngine.getEngineState();
        EngineSpecEntity engineSpec = searchEngine.getEngineSpec();

        int breakdownCount = engineState.getContinuousBreakdownCount();
        engineState.setContinuousBreakdownCount(++breakdownCount);
        disableEngine(searchEngine);

        if (breakdownCount > engineSpec.getMaxAllowdedContinousBreakdownCount()) {
            log.error("Shutting down the engine : {} - {} Type",
                    engineSpec.getEngineName(), engineSpec.getEngineType());
            log.error("\t Reason: Reached Max allowed breakdown");
        } else {
            Date enableDate = new Date(System.currentTimeMillis() + ENGINE_HALT_TIME);
            taskScheduler.schedule(() -> enableEngine(searchEngine), enableDate);
        }
    }

    private void performEngineTimeoutDiagnosis(BaseSearchEngine<?> searchEngine) {
        EngineStateEntity engineState = searchEngine.getEngineState();
        EngineSpecEntity engineSpec = searchEngine.getEngineSpec();

        int timeoutCount = engineState.getContinuousTimeoutCount();
        engineState.setContinuousTimeoutCount(++timeoutCount);

        if (timeoutCount > engineSpec.getMaxAllowdedContinousTimeoutCount()) {
            disableEngine(searchEngine);
            log.error("Shutting down the engine : {} type : {} Reason: Reached Max allowed timeout",
                    engineSpec.getEngineName(), engineSpec.getEngineType());
        }
    }

}
