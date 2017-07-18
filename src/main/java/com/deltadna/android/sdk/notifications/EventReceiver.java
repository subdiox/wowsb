package com.deltadna.android.sdk.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public abstract class EventReceiver extends BroadcastReceiver {
    private static final String TAG = ("deltaDNA " + EventReceiver.class.getSimpleName());

    public final void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            NotificationInfo info = (NotificationInfo) (intent.hasExtra(Actions.NOTIFICATION_INFO) ? intent.getSerializableExtra(Actions.NOTIFICATION_INFO) : null);
            String action = intent.getAction();
            Object obj = -1;
            switch (action.hashCode()) {
                case -777539326:
                    if (action.equals(Actions.NOTIFICATION_OPENED)) {
                        obj = 4;
                        break;
                    }
                    break;
                case -749410856:
                    if (action.equals(Actions.NOTIFICATION_POSTED)) {
                        obj = 3;
                        break;
                    }
                    break;
                case -552748396:
                    if (action.equals(Actions.MESSAGE_RECEIVED)) {
                        obj = 2;
                        break;
                    }
                    break;
                case 1841574429:
                    if (action.equals(Actions.REGISTERED)) {
                        obj = null;
                        break;
                    }
                    break;
                case 1881823560:
                    if (action.equals(Actions.REGISTRATION_FAILED)) {
                        obj = 1;
                        break;
                    }
                    break;
                case 1987669328:
                    if (action.equals(Actions.NOTIFICATION_DISMISSED)) {
                        obj = 5;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    String token = intent.getStringExtra(Actions.REGISTRATION_TOKEN);
                    if (TextUtils.isEmpty(token)) {
                        Log.w(TAG, "Registration token is null or empty");
                        return;
                    } else {
                        onRegistered(context, token);
                        return;
                    }
                case 1:
                    Throwable reason = (Throwable) intent.getSerializableExtra(Actions.REGISTRATION_FAILURE_REASON);
                    if (reason == null) {
                        Log.w(TAG, "Failed to deserialise registration failure reason");
                        return;
                    } else {
                        onRegistrationFailed(context, reason);
                        return;
                    }
                case 2:
                    PushMessage message = (PushMessage) intent.getSerializableExtra(Actions.PUSH_MESSAGE);
                    if (message == null) {
                        Log.w(TAG, "Failed to find or deserialise push message");
                        return;
                    } else {
                        onMessageReceived(context, message);
                        return;
                    }
                case 3:
                    if (info == null) {
                        Log.w(TAG, "Failed to find or deserialise notification info");
                        return;
                    } else {
                        onNotificationPosted(context, info);
                        return;
                    }
                case 4:
                    if (info == null) {
                        Log.w(TAG, "Failed to find or deserialise notification info");
                        return;
                    } else {
                        onNotificationOpened(context, info);
                        return;
                    }
                case 5:
                    if (info == null) {
                        Log.w(TAG, "Failed to find or deserialise notification info");
                        return;
                    } else {
                        onNotificationDismissed(context, info);
                        return;
                    }
                default:
                    Log.w(TAG, "Unknown action: " + intent.getAction());
                    return;
            }
        }
    }

    protected void onRegistered(Context context, String registrationId) {
    }

    protected void onRegistrationFailed(Context context, Throwable reason) {
    }

    protected void onMessageReceived(Context context, PushMessage message) {
    }

    protected void onNotificationPosted(Context context, NotificationInfo info) {
    }

    protected void onNotificationOpened(Context context, NotificationInfo info) {
    }

    protected void onNotificationDismissed(Context context, NotificationInfo info) {
    }
}
