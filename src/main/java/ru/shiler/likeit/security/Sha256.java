package ru.shiler.likeit.security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Created by Evgeny Yushkevich on 16.01.2017.
 */
public class Sha256 {

    public static String encrypt(String inputString) {
        return Hashing.sha256()
                .hashString(inputString, StandardCharsets.UTF_8)
                .toString();
    }

}
