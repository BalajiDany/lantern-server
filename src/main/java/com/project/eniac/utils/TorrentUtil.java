package com.project.eniac.utils;

public class TorrentUtil {

	public static String getCategory(int category) {
		if (category < 100) {
			return "All";
		} else if (category < 200) {
			return "Audio";
		} else if (category < 300) {
			return "Video";
		} else if (category < 400) {
			return "Application";
		} else if (category < 500) {
			return "Games";
		} else if (category < 600) {
			return "AO";
		} else {
			return "Others";
		}
	}

}
