package com.yaya.sdk.utils;

public class Base64Util {
    public static String encryptString(String originalString) throws Exception {
        return YayaBase64.encode(originalString.getBytes());
    }

    public static String encodeBase64String(byte[] data) {
        return YayaBase64.encode(data);
    }

    public static String decryptString(String encryptString) throws Exception {
        return new String(YayaBase64.decode(encryptString));
    }

    public static byte[] decodeBase64(String encryptString) {
        return YayaBase64.decode(encryptString);
    }
}
