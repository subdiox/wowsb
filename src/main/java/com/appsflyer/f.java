package com.appsflyer;

import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Map;

class f {
    f() {
    }

    public String getHashCode(Map<String, String> params) {
        String str = (String) params.get("af_timestamp");
        return toSHA1(((String) params.get("appsflyerKey")).substring(0, 7) + ((String) params.get("uid")).substring(0, 7) + str.substring(str.length() - 7));
    }

    public String getHashCodeV2(Map<String, String> params) {
        return toSHA1(toMD5(((((((String) params.get("appsflyerKey")) + ((String) params.get("af_timestamp"))) + ((String) params.get("uid"))) + ((String) params.get("installDate"))) + ((String) params.get("counter"))) + ((String) params.get("iaecounter"))));
    }

    public static String toSHA1(String input) {
        String str = null;
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.reset();
            instance.update(input.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            str = byteToHex(instance.digest());
        } catch (Exception e) {
            a.afLog(e.toString());
        }
        return str;
    }

    public static String toMD5(String input) {
        String str = null;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(input.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            str = byteToHex(instance.digest());
        } catch (Exception e) {
            a.afLog(e.toString());
        }
        return str;
    }

    private static String byteToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        int length = hash.length;
        for (int i = 0; i < length; i++) {
            formatter.format("%02x", new Object[]{Byte.valueOf(hash[i])});
        }
        String formatter2 = formatter.toString();
        formatter.close();
        return formatter2;
    }
}
