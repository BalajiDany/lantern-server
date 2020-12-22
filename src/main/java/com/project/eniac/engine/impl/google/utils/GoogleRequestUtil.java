package com.project.eniac.engine.impl.google.utils;

import java.util.HashMap;
import java.util.Map;

public class GoogleRequestUtil {

    private static final String DEFAULT_DOMAIN = "google.com";

    private static final Map<String, String> domainMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("CH", "google.ch");
            put("CN", "google.cn");
            put("IN", "google.co.in");
            put("JP", "google.co.jp");
            put("KR", "google.co.kr");
            put("UK", "google.co.uk");
            put("BR", "google.com.br");
            put("DE", "google.de");
            put("FR", "google.fr");
            put("IT", "google.it");
        }
    };

    private static final Map<String, String> locationOverrideMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("CH", "US");
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
