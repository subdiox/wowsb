package com.deltadna.android.sdk.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class NotificationFactory {
    protected final Context context;

    public NotificationFactory(Context context) {
        this.context = context;
    }

    public Builder configure(Builder builder, PushMessage message) {
        return builder.setSmallIcon(message.icon).setContentTitle(message.title).setContentText(message.message).setAutoCancel(true);
    }

    @Nullable
    public Notification create(Builder builder, NotificationInfo info) {
        boolean backgrounded;
        if (Utils.inForeground(this.context)) {
            backgrounded = false;
        } else {
            backgrounded = true;
        }
        if (!backgrounded) {
            Log.d(BuildConfig.LOG_TAG, "Notifying SDK of notification opening");
            DDNANotifications.recordNotificationOpened(Utils.convert(info.message.data), false);
        }
        builder.setContentIntent(PendingIntent.getBroadcast(this.context, 0, new Intent(Actions.NOTIFICATION_OPENED).putExtra(Actions.NOTIFICATION_INFO, info).putExtra("launch", backgrounded), 134217728));
        builder.setDeleteIntent(PendingIntent.getBroadcast(this.context, 0, new Intent(Actions.NOTIFICATION_DISMISSED).putExtra(Actions.NOTIFICATION_INFO, info), 134217728));
        return builder.build();
    }
}
