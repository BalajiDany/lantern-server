package com.project.lantern.controller.api.v1;

import com.project.lantern.service.spec.EngineConfigurationService;
import com.project.lantern.types.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/config")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfigController {

    private final EngineConfigurationService engineConfigurationService;

    @GetMapping("/reset/all")
    public void resetAll() {
        engineConfigurationService.resetAllEngines();
    }

    // TODO Change to reset by Id and form front sent all the location based engine id's
    @GetMapping("/reset/locationBasedEngines")
    public void resetLocationBasedEngines() {
        engineConfigurationService.resetLocationBasedEngines();
    }

    @GetMapping("/reset/{engineCategory}")
    public void reset(
            @PathVariable("engineCategory") String engineCategory) {
        EngineType engineType = EngineType.fromString(engineCategory);
        engineConfigurationService.resetByEngineType(engineType);
    }

}
