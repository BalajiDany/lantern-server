package com.project.lantern.service.spec;

import com.project.lantern.entity.EngineStatusResponseEntity;
import com.project.lantern.types.EngineType;

import java.util.List;

public interface EngineStatusService {

    List<EngineStatusResponseEntity> getEngineStatus(EngineType engineType);

    List<EngineStatusResponseEntity> getEngineStatus();

}
