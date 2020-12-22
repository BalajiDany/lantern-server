package com.project.eniac.service.spec;

import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.types.EngineType;

import java.util.List;

public interface EngineStatusService {

    List<EngineStatusResponseEntity> getEngineStatus(EngineType engineType);

    List<EngineStatusResponseEntity> getEngineStatus();

}
