package com.project.eniac.controller.api.v1;

import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.service.spec.EngineStatusService;
import com.project.eniac.types.EngineType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatusController {

    private final EngineStatusService engineStatusService;

    @GetMapping("/all")
    public List<EngineStatusResponseEntity> allEngineHealth() {
        return engineStatusService.getEngineStatus();
    }

    @GetMapping("/{engineCategory}")
    public List<EngineStatusResponseEntity> engineHealth(
            @PathVariable("engineCategory") String engineCategory) {
        EngineType engineType = EngineType.fromString(engineCategory);
        return engineStatusService.getEngineStatus(engineType);
    }

}
