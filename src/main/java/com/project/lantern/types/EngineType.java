package com.project.lantern.types;

public enum EngineType {

    GENERAL,
    TORRENT,
    VIDEO,
    CODE;

    public static EngineType fromString(String engineType) {
        String uppercaseEngineType = engineType.toUpperCase();
        return EngineType.valueOf(uppercaseEngineType);
    }

}
