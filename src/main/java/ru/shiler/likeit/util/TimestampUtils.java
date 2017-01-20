package ru.shiler.likeit.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Evgeny Yushkevich on 20.01.2017.
 */
public class TimestampUtils {

    public static String formatTimestamp(Timestamp timestamp, String locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy", new Locale(locale));
        return dateFormat.format(timestamp);
    }

}
