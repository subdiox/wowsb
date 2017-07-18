package com.deltadna.android.sdk.notifications;

import java.io.Serializable;
import java.util.Locale;

public class NotificationInfo implements Serializable {
    public final int id;
    public final PushMessage message;

    NotificationInfo(int id, PushMessage message) {
        this.id = id;
        this.message = message;
    }

    public String toString() {
        return String.format(Locale.US, "%s{id: %d, message: %s}", new Object[]{getClass().getSimpleName(), Integer.valueOf(this.id), this.message});
    }
}
