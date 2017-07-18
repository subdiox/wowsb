package com.facebook.unity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookSdk;

public abstract class BaseActivity extends Activity {
    public static final String ACTIVITY_PARAMS = "activity_params";
    protected CallbackManager mCallbackManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCallbackManager = Factory.create();
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mCallbackManager.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
