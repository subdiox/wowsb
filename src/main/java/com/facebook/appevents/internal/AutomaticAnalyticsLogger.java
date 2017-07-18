package com.facebook.appevents.internal;

import android.content.Context;
import android.os.Bundle;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.FetchedAppSettingsManager;

class AutomaticAnalyticsLogger {
    AutomaticAnalyticsLogger() {
    }

    public static void logActivityTimeSpentEvent(Context context, String appId, String activityName, long timeSpentInSeconds) {
        AppEventsLogger l = AppEventsLogger.newLogger(context);
        if (FetchedAppSettingsManager.queryAppSettings(appId, false).getAutomaticLoggingEnabled() && timeSpentInSeconds > 0) {
            Bundle params = new Bundle(1);
            params.putCharSequence(Constants.AA_TIME_SPENT_SCREEN_PARAMETER_NAME, activityName);
            l.logEvent(Constants.AA_TIME_SPENT_EVENT_NAME, (double) timeSpentInSeconds, params);
        }
    }
}
