package com.appsflyer;

import android.util.Log;

class a {
    public static final String LOG_TAG = ("AppsFlyer_" + AppsFlyerLib.SERVER_BUILD_NUMBER + "." + AppsFlyerLib.SDK_BUILD_NUMBER);

    public static void afLog(String logMessage) {
        if (shouldLog()) {
            Log.i(LOG_TAG, logMessage);
        }
    }

    public static void afDebugLog(String logMessage) {
        if (shouldLog()) {
            Log.d(LOG_TAG, logMessage);
        }
    }

    public static void afLogE(String message, Throwable ex) {
        if (shouldLog()) {
            Log.e(LOG_TAG, message, ex);
        }
    }

    public static void afWarnLog(String warnMessage) {
        if (shouldLog()) {
            Log.w(LOG_TAG, warnMessage);
        }
    }

    private static boolean shouldLog() {
        return AppsFlyerProperties.getInstance().isEnableLog();
    }

    public static void afLogM(String logMessage) {
        if (!noLogsAllowed()) {
            Log.d(LOG_TAG, logMessage);
        }
    }

    private static boolean noLogsAllowed() {
        return AppsFlyerProperties.getInstance().isLogsDisabledCompletely();
    }
}
