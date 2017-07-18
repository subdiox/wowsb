package com.customsdk.wargaming.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.customsdk.wargaming.util.GameHelper.GameHelperListener;
import com.customsdk.wargaming.util.GameHelper.SignInFailureReason;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class BaseGameActivity extends FragmentActivity implements GameHelperListener {
    public static final int CLIENT_ALL = 11;
    public static final int CLIENT_GAMES = 1;
    public static final int CLIENT_PLUS = 2;
    private static final String TAG = "BaseGameActivity";
    protected boolean mDebugLog = false;
    protected GameHelper mHelper;
    protected int mRequestedClients = 1;

    protected BaseGameActivity() {
    }

    protected BaseGameActivity(int requestedClients) {
        setRequestedClients(requestedClients);
    }

    protected void setRequestedClients(int requestedClients) {
        this.mRequestedClients = requestedClients;
    }

    public GameHelper getGameHelper() {
        if (this.mHelper == null) {
            this.mHelper = new GameHelper(this, this.mRequestedClients);
            this.mHelper.enableDebugLog(this.mDebugLog);
        }
        return this.mHelper;
    }

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (this.mHelper == null) {
            getGameHelper();
        }
        this.mHelper.setup(this);
    }

    protected void onStart() {
        super.onStart();
        this.mHelper.onStart(this);
    }

    protected void onStop() {
        super.onStop();
        this.mHelper.onStop();
    }

    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        this.mHelper.onActivityResult(request, response, data);
    }

    protected GoogleApiClient getApiClient() {
        return this.mHelper.getApiClient();
    }

    protected boolean isSignedIn() {
        return this.mHelper.isSignedIn();
    }

    protected void beginUserInitiatedSignIn() {
        this.mHelper.beginUserInitiatedSignIn();
    }

    protected void signOut() {
        this.mHelper.signOut();
    }

    protected void showAlert(String message) {
        this.mHelper.makeSimpleDialog(message).show();
    }

    protected void showAlert(String title, String message) {
        this.mHelper.makeSimpleDialog(title, message).show();
    }

    protected void enableDebugLog(boolean enabled) {
        this.mDebugLog = true;
        if (this.mHelper != null) {
            this.mHelper.enableDebugLog(enabled);
        }
    }

    @Deprecated
    protected void enableDebugLog(boolean enabled, String tag) {
        Log.w(TAG, "BaseGameActivity.enabledDebugLog(bool,String) is deprecated. Use enableDebugLog(boolean)");
        enableDebugLog(enabled);
    }

    protected String getInvitationId() {
        return this.mHelper.getInvitationId();
    }

    protected void reconnectClient() {
        this.mHelper.reconnectClient();
    }

    protected boolean hasSignInError() {
        return this.mHelper.hasSignInError();
    }

    protected SignInFailureReason getSignInError() {
        return this.mHelper.getSignInError();
    }
}
