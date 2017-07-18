package com.appsflyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GetDeepLinkingActivity extends Activity {
    private static String TAG = "AppsFlyerDeepLinkActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        startActivity(new Intent(this, getMainActivityClass()));
        AppsFlyerLib.getInstance().setDeepLinkData(getIntent());
        finish();
    }

    private Class<?> getMainActivityClass() {
        try {
            return Class.forName(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent().getClassName());
        } catch (Exception e) {
            Log.e(TAG, "Unable to find Main Activity Class");
            return null;
        }
    }
}
