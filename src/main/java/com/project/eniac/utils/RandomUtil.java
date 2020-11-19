package com.project.eniac.utils;

import java.util.List;
import java.util.Random;

import lombok.NonNull;

public class RandomUtil {
	
	private static Random random = new Random();

	public static int getRandomInt(int range) {
		return random.nextInt(range);
	}

	public static <T> T getRandomElement(@NonNull List<T> list) {
		int size = list.size();
		if (size == 0) {
			return null;
		}

		int index = RandomUtil.getRandomInt(size);
		return list.get(index);
	}

	public static <T> T getRandomElement(@NonNull T... array) {
		if (array.length == 0) {
			return null;
		}

		int index = RandomUtil.getRandomInt(array.length);
		return array[index];
	}

}
