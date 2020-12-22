package com.project.eniac.service.spec;

import java.util.Map;

public interface CommonLanguageService {

    String getDefaultLanguage();

    boolean isValidLanguage(String sampleLanguage);

    String getSupportedLanguage(String sampleLanguage);

    Map<String, String> getAllSupportedLanguage();

}
