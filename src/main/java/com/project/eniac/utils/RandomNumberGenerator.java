package com.project.eniac.utils;

import java.util.Random;

public class RandomNumberGenerator {
	
	private static Random random = new Random();

	public static int getRandomInt(int range) {
		return random.nextInt(range);
	}
}
