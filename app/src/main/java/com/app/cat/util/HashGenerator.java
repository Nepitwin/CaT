/*
 * Copyright (c) 2016.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.app.cat.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Static hash generator class to create hashes.
 *
 * @author Andreas Sekulski
 */
public class HashGenerator {

    private static final String MD5 = "MD5";

    /**
     * Creates an ha1 hash from given input.
     * @param username Username
     * @param domain Domain
     * @param password Password
     * @return An ha1 if operation successfully otherwise null.
     * @throws NoSuchAlgorithmException If algo not supported.
     */
    public static String ha1(String username, String domain, String password) throws NoSuchAlgorithmException {
        Log.d("HA1", md5(username + ":" + domain + ":" + password));
        return md5(username + ":" + domain + ":" + password);
    }

    /**
     * Creates an ha1b hash from given input.
     * @param username Username
     * @param domain Domain
     * @param password Password
     * @return An ha1b if operation successfully otherwise null.
     * @throws NoSuchAlgorithmException If algo not supported.
     */
    public static String ha1b(String username, String domain, String password) throws NoSuchAlgorithmException {
        Log.d("HA1B", md5(username + "@" + domain +  ":" + domain + ":" + password));
        return md5(username + "@" + domain +  ":" + domain + ":" + password);
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
        MessageDigest md5 = MessageDigest.getInstance(MD5);

        md5.reset();
        md5.update(input.getBytes());
        digest = md5.digest();

        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1,3));
        }

        return sb.toString();
    }
}