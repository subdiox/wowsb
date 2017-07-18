package com.facebook.unity;

import android.os.Bundle;
import com.facebook.CallbackManager;

public class FBUnityLoginActivity extends BaseActivity {
    public static final String LOGIN_PARAMS = "login_params";
    public static final String LOGIN_TYPE = "login_type";

    public enum LoginType {
        READ,
        PUBLISH,
        TV_READ,
        TV_PUBLISH
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginType type = (LoginType) getIntent().getSerializableExtra(LOGIN_TYPE);
        String loginParams = getIntent().getStringExtra(LOGIN_PARAMS);
        switch (type) {
            case READ:
                FBLogin.loginWithReadPermissions(loginParams, this);
                return;
            case PUBLISH:
                FBLogin.loginWithPublishPermissions(loginParams, this);
                return;
            case TV_READ:
                FBLogin.loginForTVWithReadPermissions(loginParams, this);
                return;
            case TV_PUBLISH:
                FBLogin.loginForTVWithPublishPermissions(loginParams, this);
                return;
            default:
                return;
        }
    }

    public CallbackManager getCallbackManager() {
        return this.mCallbackManager;
    }
}
