package com.project.lantern.service.spec;

import com.project.lantern.types.EngineType;

public interface EngineConfigurationService {

    void resetAllEngines();

    void resetByEngineType(EngineType engineType);

    void resetLocationBasedEngines();

}
