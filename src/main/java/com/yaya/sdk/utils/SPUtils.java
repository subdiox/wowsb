package com.yaya.sdk.utils;

import android.content.Context;
import com.yaya.sdk.constants.Constants;

public class SPUtils {
    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(Constants.SP_NAME, 0).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(Constants.SP_NAME, 0).getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences(Constants.SP_NAME, 0).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(Constants.SP_NAME, 0).getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences(Constants.SP_NAME, 0).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return context.getSharedPreferences(Constants.SP_NAME, 0).getInt(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        context.getSharedPreferences(Constants.SP_NAME, 0).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, Long defValue) {
        return context.getSharedPreferences(Constants.SP_NAME, 0).getLong(key, defValue.longValue());
    }
}
