package ru.shiler.likeit.database;

/**
 * Created by Evgeny Yushkevich on 12.01.2017.
 */
public class Utils {

    public static java.sql.Date convert(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }

}
