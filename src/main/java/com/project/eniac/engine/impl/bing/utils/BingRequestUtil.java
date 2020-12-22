package com.project.eniac.engine.impl.bing.utils;

import java.util.HashMap;
import java.util.Map;

public class BingRequestUtil {

	private static final Map<String, String> locationOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("US", "en-us");
			put("CN", "zh-cn");
			put("JP", "ja-jp");
			put("DE", "de-de");
			put("IN", "en-in");
			put("GB", "en-gb");
			put("KR", "ko-kr");
			put("BR", "pt-br");
			put("FR", "fr-fr");
			put("IT", "it-it");
		}
	};

	private static final Map<String, String> languageOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("pt", "pt-pt");
			put("zh", "zh-hans");
		}
	};

	public static String getLanguage(String inputLanguage) {
		return languageOverideMap.containsKey(inputLanguage) ? languageOverideMap.get(inputLanguage) : inputLanguage;
	}

	public static String getRegion(String inputRegion) {
		return locationOverideMap.containsKey(inputRegion) ? locationOverideMap.get(inputRegion) : inputRegion;
	}

}
