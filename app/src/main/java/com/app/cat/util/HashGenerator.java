package com.app.cat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Static hash generator class to create hashes.
 *
 * @author Andreas Sekulski
 */
public class HashGenerator {

    /**
     * Creates an ha1 hash from given input.
     * @param username Username
     * @param domain Domain
     * @param password Password
     * @return An ha1 if operation successfully otherwise null.
     */
    public static String ha1(String username, String domain, String password) {
        try {
            return md5(username + ":" + domain + ":" + password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an ha1b hash from given input.
     * @param username Username
     * @param domain Domain
     * @param password Password
     * @return An ha1b if operation successfully otherwise null.
     */
    public static String ha1b(String username, String domain, String password) {
        try {
            return md5(username + "@" + domain +  ":" + domain + ":" + password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates an md5 hash from input.
     * @param input Input to hash.
     * @return An md5 hash as an string if operation successfully.
     * @throws NoSuchAlgorithmException If algo not supported.
     */
    private static String md5(String input) throws NoSuchAlgorithmException {
        byte[] digest;
        StringBuffer sb = new StringBuffer();
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        md5.reset();
        md5.update(input.getBytes());
        digest = md5.digest();

        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }

        return sb.toString();
    }
}