package com.yaya.sdk.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String aaaa = "T~rYw2^3%'_9#iGc";

    public static String encode(String in) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String hex = "";
        byte[] bytIn = in.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(aaaa.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, skeySpec);
        return byte2hexString(cipher.doFinal(bytIn));
    }

    public static String decode(String hex) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String rr = "";
        byte[] bytIn = hex2Bin(hex);
        SecretKeySpec skeySpec = new SecretKeySpec(aaaa.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, skeySpec);
        return new String(cipher.doFinal(bytIn));
    }

    private static byte[] hex2Bin(String src) {
        if (src.length() < 1) {
            return null;
        }
        byte[] encrypted = new byte[(src.length() / 2)];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, (i * 2) + 1), 16);
            encrypted[i] = (byte) ((high * 16) + Integer.parseInt(src.substring((i * 2) + 1, (i * 2) + 2), 16));
        }
        return encrypted;
    }

    private static String byte2hexString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append(Integer.toString((buf[i] >> 4) & 15, 16) + Integer.toString(buf[i] & 15, 16));
        }
        return strbuf.toString();
    }
}
