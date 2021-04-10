package com.project.lantern.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Base64;
import java.util.Date;

public class ConversionUtil {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM, yyyy");

    public static String convertBytesToReadable(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.2f %cB", value / 1024.0, ci.current());
    }

    public static String parseDate(long date) {
        long adjusted = date * 1000;
        return DATE_FORMAT.format(new Date(adjusted));
    }

    public static int parseInt(String integerStr) {
        try {
            return Integer.parseInt(integerStr);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder
                .decode(url.replace("+", "%2B"), "UTF-8")
                .replace("%2B", "+");
        } catch (UnsupportedEncodingException exception) {
            return null;
        }
    }

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return StringUtils.EMPTY;
        }
    }

    public static String base64UrlDecoder(String encodedUrl) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUrl);
        return new String(decodedBytes);
    }

}
