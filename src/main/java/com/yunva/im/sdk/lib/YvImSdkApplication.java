package com.yunva.im.sdk.lib;

import android.app.Application;
import android.content.Context;

public class YvImSdkApplication extends Application {
    public void onCreate() {
        super.onCreate();
        YvLoginInit.initApplicationOnCreate(this, getYvImSdkAppId(this), 1800000);
    }

    public String getYvImSdkAppId(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.get("YvImSdkAppId").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
