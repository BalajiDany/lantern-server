package com.project.eniac.controller.api.v1;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.eniac.entity.EngineStatusResponseEntity;
import com.project.eniac.service.spec.EngineStatusService;
import com.project.eniac.types.EngineType;

import lombok.RequiredArgsConstructor;

@RestController()
@RequiredArgsConstructor
@RequestMapping("api/v1/status")
public class StatusController {

	private final EngineStatusService engineStatusService;

	@GetMapping("/all")
	public List<EngineStatusResponseEntity> allEngineHealth() {
		return engineStatusService.getEngineStatus();
	}

	@GetMapping("/{engineCategory}")
	public List<EngineStatusResponseEntity> engineHealth(@PathVariable("engineCategory") String engineCategory) {
		EngineType engineType = EngineType.fromString(engineCategory);
		return engineStatusService.getEngineStatus(engineType);
	}

}
