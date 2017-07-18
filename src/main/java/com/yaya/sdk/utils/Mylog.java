package com.yaya.sdk.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.yaya.sdk.constants.Constants;

public class Mylog {
    public static void d(String tag, Object message) {
        if (Constants.IS_LOG_ON) {
            Log.d(tag, message + "");
        }
    }

    public static void i(String tag, Object message) {
        if (Constants.IS_LOG_ON) {
            Log.i(tag, message + "");
        }
    }

    public static void e(String tag, Object message) {
        if (Constants.IS_LOG_ON) {
            Log.e(tag, message + "");
        }
    }

    public static void v(String tag, Object message) {
        if (Constants.IS_LOG_ON) {
            Log.v(tag, message + "");
        }
    }

    public static void w(String tag, Object message) {
        if (Constants.IS_LOG_ON) {
            Log.w(tag, message + "");
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, 0).show();
    }
}
