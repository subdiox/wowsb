package com.yaya.sdk.utils;

public final class YayaBase64 {
    private static final char[] lookup = new char[64];
    private static final byte[] reverseLookup = new byte[256];

    static {
        int i;
        for (i = 0; i < 26; i++) {
            lookup[i] = (char) (i + 65);
        }
        i = 26;
        int j = 0;
        while (i < 52) {
            lookup[i] = (char) (j + 97);
            i++;
            j++;
        }
        i = 52;
        j = 0;
        while (i < 62) {
            lookup[i] = (char) (j + 48);
            i++;
            j++;
        }
        lookup[62] = '+';
        lookup[63] = '/';
        for (i = 0; i < 256; i++) {
            reverseLookup[i] = (byte) -1;
        }
        for (i = 90; i >= 65; i--) {
            reverseLookup[i] = (byte) (i - 65);
        }
        for (i = 122; i >= 97; i--) {
            reverseLookup[i] = (byte) ((i - 97) + 26);
        }
        for (i = 57; i >= 48; i--) {
            reverseLookup[i] = (byte) ((i - 48) + 52);
        }
        reverseLookup[43] = (byte) 62;
        reverseLookup[47] = (byte) 63;
        reverseLookup[61] = (byte) 0;
    }

    private YayaBase64() {
    }

    public static String encode(byte[] bytes) {
        StringBuilder buf = new StringBuilder(((bytes.length + 2) / 3) * 4);
        int end = bytes.length - 2;
        int i = 0;
        while (i < end) {
            int i2 = i + 1;
            i = i2 + 1;
            i2 = i + 1;
            int chunk = (((bytes[i] & 255) << 16) | ((bytes[i2] & 255) << 8)) | (bytes[i] & 255);
            buf.append(lookup[chunk >> 18]);
            buf.append(lookup[(chunk >> 12) & 63]);
            buf.append(lookup[(chunk >> 6) & 63]);
            buf.append(lookup[chunk & 63]);
            i = i2;
        }
        int len = bytes.length;
        if (i < len) {
            i2 = i + 1;
            chunk = (bytes[i] & 255) << 16;
            buf.append(lookup[chunk >> 18]);
            if (i2 < len) {
                chunk |= (bytes[i2] & 255) << 8;
                buf.append(lookup[(chunk >> 12) & 63]);
                buf.append(lookup[(chunk >> 6) & 63]);
            } else {
                buf.append(lookup[(chunk >> 12) & 63]);
                buf.append('=');
            }
            buf.append('=');
        }
        return buf.toString();
    }

    public static byte[] decode(String encoded) {
        int i;
        int padding = 0;
        for (i = encoded.length() - 1; encoded.charAt(i) == '='; i--) {
            padding++;
        }
        int length = ((encoded.length() * 6) / 8) - padding;
        byte[] bytes = new byte[length];
        int index = 0;
        int n = encoded.length();
        for (i = 0; i < n; i += 4) {
            int word = (((reverseLookup[encoded.charAt(i)] << 18) + (reverseLookup[encoded.charAt(i + 1)] << 12)) + (reverseLookup[encoded.charAt(i + 2)] << 6)) + reverseLookup[encoded.charAt(i + 3)];
            int j = 0;
            while (j < 3 && index + j < length) {
                bytes[index + j] = (byte) (word >> ((2 - j) * 8));
                j++;
            }
            index += 3;
        }
        return bytes;
    }
}
