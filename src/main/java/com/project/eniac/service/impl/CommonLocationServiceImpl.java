package com.project.eniac.service.impl;

import com.project.eniac.service.spec.CommonLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonLocationServiceImpl implements CommonLocationService {

    private static final Map<String, String> SUPPORTED_LOCATION = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("US", "United States");
            put("CN", "China");
            put("JP", "Japan");
            put("DE", "Germany");
            put("IN", "India");
            put("GB", "United Kingdom");
            put("KR", "South Korea");
            put("BR", "Brazil");
            put("FR", "France");
            put("IT", "Italy");
        }
    };

    @Value("${project.eniac.configuration.location}")
    private String defaultLocation;

    @PostConstruct
    public void init() {

        // To ensure location is capitalized.
        if (ObjectUtils.isNotEmpty(this.defaultLocation)) {
            this.defaultLocation = this.defaultLocation.toUpperCase();
        }

        // To ensure configured location is correct.
        boolean invalidLanguage = !this.isValidLocation(defaultLocation);
        if (invalidLanguage) {
            log.warn("Default Location mentioned in properties is not supported - {}", defaultLocation);
            this.defaultLocation = "US";
        }
        log.info("Enabling default location - {}", SUPPORTED_LOCATION.get(this.defaultLocation));
    }

    @Override
    public String getDefaultLocation() {
        return this.defaultLocation;
    }

    @Override
    public boolean isValidLocation(String location) {
        return SUPPORTED_LOCATION.containsKey(location);
    }

    @Override
    public String getSupportedLocation(String sampleLocation) {
        return this.isValidLocation(sampleLocation) ? sampleLocation : this.defaultLocation;
    }

    @Override
    public Map<String, String> getAllSupportedLocation() {
        return CommonLocationServiceImpl.SUPPORTED_LOCATION;
    }

}
