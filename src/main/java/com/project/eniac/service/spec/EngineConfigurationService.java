package com.project.eniac.service.spec;

import com.project.eniac.types.EngineType;

public interface EngineConfigurationService {

    void resetAllEngines();

    void resetByEngineType(EngineType engineType);

    void resetLocationBasedEngines();

}
