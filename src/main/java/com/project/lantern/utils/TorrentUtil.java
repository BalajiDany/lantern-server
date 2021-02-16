package com.project.lantern.utils;

import org.apache.commons.lang3.StringUtils;

public class TorrentUtil {

    public static String extractCoreMagneticLink(String magneticLink) {
        if (StringUtils.isBlank(magneticLink)) return StringUtils.EMPTY;
        return magneticLink.split("&")[0];
    }

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
            return "Adults Only";
        } else {
            return "Others";
        }
    }

}
