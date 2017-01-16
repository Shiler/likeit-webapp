package ru.shiler.likeit.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class CommandUtils {

    public static String getBackUri(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String uri = getCommandFromUri(referer);
        if (uri != null) {
            return uri;
        } else {
            return "/index";
        }
    }

    public static String getCommandFromUri(String uri) {
        if (uri == null) return null;
        char[] chars = uri.toCharArray();
        for (int i = 0, counter = 0; i < chars.length; i++) {
            if (chars[i] == '/') {
                counter++;
            }
            if (counter == 3) {
                return uri.substring(i);
            }
        }
        return null;
    }

}
