package com.project.eniac.engine.impl.duckduckgo.utils;

import java.util.HashMap;
import java.util.Map;

public class DuckDuckGoRequestUtil {
	private static final Map<String, String> locationOverideMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("US", "us-en");
			put("CN", "cn-zh");
			put("JP", "jp-jp");
			put("DE", "de-de");
			put("IN", "in-en");
			put("GB", "uk-en");
			put("KR", "kr-kr");
			put("BR", "br-pt");
			put("FR", "fr-fr");
			put("IT", "it-it");
		}
	};

	public static String getRegion(String inputRegion) {
		return locationOverideMap.containsKey(inputRegion) ? locationOverideMap.get(inputRegion) : inputRegion;
	}

}
