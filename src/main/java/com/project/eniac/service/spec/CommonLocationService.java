package com.project.eniac.service.spec;

import java.util.Map;

public interface CommonLocationService {

    String getDefaultLocation();

    boolean isValidLocation(String location);

    String getSupportedLocation(String sampleLocation);

    Map<String, String> getAllSupportedLocation();

}
