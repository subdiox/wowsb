package com.yaya.sdk.utils;

public class EncrypUtil {
    public static String encryptString(String content) throws Exception {
        return AESUtils.encode(Base64Util.encryptString(content));
    }

    public static String decryptString(String content) throws Exception {
        return Base64Util.decryptString(AESUtils.decode(content));
    }
}
