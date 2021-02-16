package com.project.lantern.engine.impl.google.utils;

import java.util.HashMap;
import java.util.Map;

public class GoogleRequestUtil {

    private static final String DEFAULT_DOMAIN = "google.com";

    private static final Map<String, String> domainMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("US", "google.com");
            put("CN", "google.com.kh");
            put("JP", "google.co.jp");
            put("DE", "google.de");
            put("IN", "google.co.in");
            put("UK", "google.co.uk");
            put("KR", "google.co.kr");
            put("BR", "google.com.br");
            put("FR", "google.fr");
            put("IT", "google.it");
        }
    };

    private static final Map<String, String> locationOverrideMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("GB", "UK");
        }
    };

    private static final Map<String, String> languageOverrideMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("de", "en");
        }
    };

    public static String getLanguage(String inputLanguage) {
        return languageOverrideMap.getOrDefault(inputLanguage, inputLanguage);
    }

    public static String getRegion(String inputRegion) {
        return locationOverrideMap.getOrDefault(inputRegion, inputRegion);
    }

    public static String getDomain(String location) {
        return domainMap.getOrDefault(location, getDefaultDomain());
    }

    public static String getDefaultDomain() {
        return DEFAULT_DOMAIN;
    }

}
