package com.project.eniac.engine.google.util;

import java.util.HashMap;
import java.util.Map;

public class GoogleRequestUtil{

	private static String DEFAULT_DOMAIN = "google.com";

	private static Map<String, String> domainMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			domainMap.put("CH", "google.ch");
			domainMap.put("CN", "google.cn");
			domainMap.put("IN", "google.co.in");
			domainMap.put("JP", "google.co.jp");
			domainMap.put("KR", "google.co.kr");
			domainMap.put("UK", "google.co.uk");
			domainMap.put("BR", "google.com.br");
			domainMap.put("DE", "google.de");
			domainMap.put("FR", "google.fr");
			domainMap.put("IT", "google.it");
		}
	};

	private static final Map<String, String> locationOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CH", "US");
			put("GB", "UK");
		}
	};

	private static final Map<String, String> languageOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("de", "en");
		}
	};

	public static String getLanguage(String inputLanguage) {
		return languageOverideMap.containsKey(inputLanguage) ? languageOverideMap.get(inputLanguage) : inputLanguage;
	}

	public static String getRegion(String inputRegion) {
		return locationOverideMap.containsKey(inputRegion) ? locationOverideMap.get(inputRegion) : inputRegion;
	}

	public static String getDomain(String location) {
		return domainMap.getOrDefault(location, getDefaultDomain());
	}

	public static String getDefaultDomain() {
		return DEFAULT_DOMAIN;
	}

}
