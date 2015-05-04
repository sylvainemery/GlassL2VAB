package com.hackncheese.glassl2vab.helper;

import android.util.Base64;

import java.security.MessageDigest;

public class EncodeHelper {

    public static String base64(String s) {
        return Base64.encodeToString(s.getBytes(), 2);
    }

    public static String base64(byte ab[]) {
        return Base64.encodeToString(ab, 2);
    }

    public static String byteToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private static byte[] computeSHA(String s, String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            md.update(s.getBytes("UTF-8"));
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] sha1(String s) {
        return computeSHA(s, "SHA-1");
    }

    public static byte[] sha512(String s) {
        return computeSHA(s, "SHA-512");
    }
}