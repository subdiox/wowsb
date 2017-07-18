package com.yaya.sdk.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class PollingUtil {
    private static String TAG = PollingUtil.class.getSimpleName();

    public static void startPollingService(Context context, int hours, Class className) {
        ((AlarmManager) context.getSystemService("alarm")).setRepeating(3, SystemClock.elapsedRealtime() + 5000, (long) (3600000 * hours), PendingIntent.getService(context, 0, new Intent(context, className), 134217728));
    }

    public static void stopPollingService(Context context, Class className) {
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getService(context, 0, new Intent(context, className), 134217728));
    }
}
