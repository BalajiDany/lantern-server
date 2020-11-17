package com.project.eniac.utils;

public class UserAgent {

	private static String[] userAgents = new String[] {
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36",
	};
	
	public static String getRandomUserAgent() {
		int index = RandomNumberGenerator.getRandomInt(userAgents.length);
		return userAgents[index];
	}
}
