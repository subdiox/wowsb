package com.deltadna.android.sdk.notifications;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceIdService;

public final class InstanceIdListenerService extends FirebaseInstanceIdService {
    private static final String TAG = ("deltaDNA " + InstanceIdListenerService.class.getSimpleName());

    public void onTokenRefresh() {
        Log.d(TAG, "Registration token has been refreshed");
        RegistrationTokenFetcher.fetch(this);
    }
}
