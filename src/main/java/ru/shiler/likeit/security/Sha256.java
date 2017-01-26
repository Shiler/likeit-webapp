package ru.shiler.likeit.security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 * Google Guava encrypt algorithm wrapper.
 */
public class Sha256 {

    /**
     * Returns a hash function implementing the SHA-256 algorithm (256 hash bits) by delegating to the
     * SHA-256 {@link MessageDigest}.
     */
    public static String encrypt(String inputString) {
        return Hashing.sha256()
                .hashString(inputString, StandardCharsets.UTF_8)
                .toString();
    }

}
