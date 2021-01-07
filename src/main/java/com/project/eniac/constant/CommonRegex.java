package com.project.eniac.constant;

import java.util.regex.Pattern;

public class CommonRegex {

    private CommonRegex() {}

    public static Pattern YOUTUBE_VIDEO_ID_PATTERN = Pattern.compile(
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
            , Pattern.CASE_INSENSITIVE);

}
