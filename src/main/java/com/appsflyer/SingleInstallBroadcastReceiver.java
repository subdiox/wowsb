package com.appsflyer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SingleInstallBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        String stringExtra = intent == null ? null : intent.getStringExtra("referrer");
        if (stringExtra == null || sharedPreferences.getString("referrer", null) == null) {
            stringExtra = AppsFlyerProperties.getInstance().getString("referrer_timestamp");
            long currentTimeMillis = System.currentTimeMillis();
            if (stringExtra == null || currentTimeMillis - Long.valueOf(stringExtra).longValue() >= 2000) {
                a.afLog("SingleInstallBroadcastReceiver called");
                AppsFlyerLib.getInstance().onReceive(context, intent);
                AppsFlyerProperties.getInstance().set("referrer_timestamp", String.valueOf(System.currentTimeMillis()));
                return;
            }
            return;
        }
        AppsFlyerLib.getInstance().addReferrer(context, stringExtra);
    }
}
