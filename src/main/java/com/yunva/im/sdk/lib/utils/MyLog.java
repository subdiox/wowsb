package com.yunva.im.sdk.lib.utils;

import android.util.Log;

public class MyLog {
    public static boolean isDebug = false;

    public static void i(String i, String s) {
        if (isDebug) {
            Log.i(i, s);
        }
    }

    public static void e(String i, String s) {
        if (isDebug) {
            Log.e(i, s);
        }
    }

    public static void w(String i, String s) {
        if (isDebug) {
            Log.i(i, s);
        }
    }

    public static void d(String i, String s) {
        if (isDebug) {
            Log.i(i, s);
        }
    }

    public static void v(String i, String s) {
        if (isDebug) {
            Log.i(i, s);
        }
    }

    public static void setEnabled(boolean isDebug) {
        isDebug = isDebug;
    }

    public static void e(String string) {
        if (isDebug) {
            Log.i("system", string);
        }
    }
}
