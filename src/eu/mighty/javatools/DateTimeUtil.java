package eu.mighty.javatools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

	static String[] formats = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mmZ",
		"yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ","yyyy-MM-dd-HH-mm-ss", };

	public static String getDTString() {
		SimpleDateFormat sdf = new SimpleDateFormat(formats[4], Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}

	public static String getDTString(int id) {
		SimpleDateFormat sdf = new SimpleDateFormat(formats[id%formats.length], Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}
}
