package com.project.lantern.controller.api.v1;

import com.project.lantern.entity.EngineStatusResponseEntity;
import com.project.lantern.service.spec.EngineStatusService;
import com.project.lantern.types.EngineType;
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
