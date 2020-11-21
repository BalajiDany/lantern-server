package com.project.eniac.utils;

import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;

public class ConvertionUtil {

	private static DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");

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
		return dateFormat.format(new Date(adjusted));
	}

}
