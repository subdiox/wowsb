package com.deltadna.android.sdk.notifications;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

class MetaData {
    static final String APPLICATION_ID = "ddna_application_id";
    static final String NOTIFICATION_ICON = "ddna_notification_icon";
    static final String NOTIFICATION_TITLE = "ddna_notification_title";
    static final String SENDER_ID = "ddna_sender_id";
    @Deprecated
    static final String START_LAUNCH_INTENT = "ddna_start_launch_intent";
    static Bundle values;

    static synchronized Bundle get(Context context) {
        Bundle bundle;
        synchronized (MetaData.class) {
            if (values == null) {
                try {
                    values = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                    if (values == null) {
                        values = Bundle.EMPTY;
                    }
                } catch (NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            bundle = values;
        }
        return bundle;
    }

    private MetaData() {
    }
}
