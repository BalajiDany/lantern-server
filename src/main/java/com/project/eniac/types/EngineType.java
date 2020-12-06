package com.project.eniac.types;

public enum EngineType {

	GENERAL,
	TORRENT,
	VIDEO;

	public static EngineType fromString(String engineType) {
		String uppercasedEngineType = engineType.toUpperCase();
		return EngineType.valueOf(uppercasedEngineType);
	}

}
