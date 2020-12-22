package com.project.eniac.service.spec;

import java.util.List;

import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.types.EngineType;

public interface EngineStatusService {

	List<EngineStatusResponseEntity> getEngineStatus(EngineType engineType);

	List<EngineStatusResponseEntity> getEngineStatus();

}
