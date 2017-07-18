package com.deltadna.android.sdk.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public final class NotificationInteractionReceiver extends BroadcastReceiver {
    private static final String TAG = ("deltaDNA " + NotificationInteractionReceiver.class.getSimpleName());

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received " + intent);
        String action = intent.getAction();
        if (action != null) {
            NotificationInfo info = (NotificationInfo) intent.getSerializableExtra(Actions.NOTIFICATION_INFO);
            boolean z = true;
            switch (action.hashCode()) {
                case -777539326:
                    if (action.equals(Actions.NOTIFICATION_OPENED)) {
                        z = false;
                        break;
                    }
                    break;
                case 1987669328:
                    if (action.equals(Actions.NOTIFICATION_DISMISSED)) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    if (info == null) {
                        Log.w(TAG, "Failed to find or deserialise notification info");
                    }
                    if (intent.getBooleanExtra("launch", false) && info != null) {
                        Log.d(TAG, "Notifying SDK of notification opening");
                        DDNANotifications.recordNotificationOpened(Utils.convert(info.message.data), true);
                    }
                    if (MetaData.get(context).containsKey("ddna_start_launch_intent")) {
                        Log.w(TAG, "Use of ddna_start_launch_intent in the manifest has been deprecated");
                    }
                    if (MetaData.get(context).getBoolean("ddna_start_launch_intent", true)) {
                        Log.d(TAG, "Starting activity with launch intent");
                        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
                        return;
                    }
                    return;
                case true:
                    Bundle bundle;
                    if (info == null) {
                        Log.w(TAG, "Failed to find or deserialise notification info");
                    }
                    Log.d(TAG, "Notifying SDK of notification dismissal");
                    if (info == null) {
                        bundle = Bundle.EMPTY;
                    } else {
                        bundle = Utils.convert(info.message.data);
                    }
                    DDNANotifications.recordNotificationDismissed(bundle);
                    return;
                default:
                    Log.w(TAG, "Unexpected action " + action);
                    return;
            }
        }
        Log.w(TAG, "Null action");
    }
}
