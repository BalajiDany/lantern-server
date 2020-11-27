package com.project.eniac.engine.yahoo.utils;

import java.util.HashMap;
import java.util.Map;

public class YahooRequestUtil {

	private static final String DEFAULT_DOMAIN = "search.yahoo.com";

	private static final Map<String, String> domainMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("US", "us.search.yahoo.com");
			put("CN", "search.yahoo.com");
			put("IN", "in.search.yahoo.com");
			put("JP", "search.yahoo.co.jp");
			put("KR", "search.yahoo.com");
			put("UK", "uk.search.yahoo.com");
			put("BR", "br.search.yahoo.com");
			put("DE", "de.search.yahoo.com");
			put("FR", "fr.search.yahoo.com");
			put("IT", "it.search.yahoo.com");
		}

		// https://sg.search.yahoo.com/search?p=covid+19&fr=yfp-t&fp=1&toggle=1&cop=mss&ei=UTF-8
		// https://www.wordsapi.com/mashape/words/tamil?when=2020-11-27T09:37:53.726Z&encrypted=8cfdb18ae723929be89607bfe658bdbeaeb52e0937fd91b8
		// https://wordweb.info/developer/SQL/search.pl?w=search
	};

	public static String getDomainByLocation(String location) {
		return domainMap.getOrDefault(location, DEFAULT_DOMAIN);
	}
}
