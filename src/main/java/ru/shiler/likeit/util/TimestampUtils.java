package ru.shiler.likeit.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TimestampUtils {

    /**
     * @param timestamp time value
     * @param locale    to perform locale-dependent processing
     * @return <code>String</code> value represents localized DateTime. (eg 00:00 01 Jan 2017)
     */
    public static String formatTimestamp(Timestamp timestamp, String locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:HH dd MMM yyyy", new Locale(locale));
        return dateFormat.format(timestamp);
    }

}
