package com.deltadna.android.sdk.notifications;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import com.deltadna.android.sdk.DDNA;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions.Builder;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class DDNANotifications {
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    private static final String NAME = "deltadna-sdk-notifications";
    private static final String TAG = ("deltaDNA " + DDNANotifications.class.getSimpleName());

    public static void register(Context context) {
        register(context, false);
    }

    public static void register(final Context context, boolean secondary) {
        Log.d(TAG, "Registering for push notifications");
        try {
            Bundle metaData = MetaData.get(context);
            String applicationId = context.getString(metaData.getInt("ddna_application_id"));
            String senderId = context.getString(metaData.getInt("ddna_sender_id"));
            synchronized (DDNANotifications.class) {
                String name = secondary ? NAME : FirebaseApp.DEFAULT_APP_NAME;
                boolean found = false;
                for (FirebaseApp app : FirebaseApp.getApps(context)) {
                    if (app.getName().equals(name)) {
                        found = true;
                    }
                }
                if (found) {
                    EXECUTOR.execute(new Runnable() {
                        public void run() {
                            RegistrationTokenFetcher.fetch(context);
                        }
                    });
                } else {
                    FirebaseApp.initializeApp(context, new Builder().setApplicationId(applicationId).setGcmSenderId(senderId).build(), name);
                }
            }
        } catch (NotFoundException e) {
            throw new IllegalStateException(String.format(Locale.US, "Failed to find configuration meta-data, have %s and %s been defined in the manifest?", new Object[]{"ddna_application_id", "ddna_sender_id"}), e);
        }
    }

    public static void unregister() {
        if (UnityForwarder.isPresent()) {
            throw new UnsupportedOperationException("Unity SDK should unregister from its own code");
        }
        Log.d(TAG, "Unregistering from push notifications");
        DDNA.instance().clearRegistrationId();
    }

    public static void recordNotificationOpened(Bundle payload, boolean launch) {
        if (UnityForwarder.isPresent()) {
            Bundle copy = new Bundle(payload);
            copy.putString("_ddCommunicationSender", "GOOGLE_NOTIFICATION");
            copy.putBoolean("_ddLaunch", launch);
            UnityForwarder.getInstance().forward("DeltaDNA.AndroidNotifications", launch ? "DidLaunchWithPushNotification" : "DidReceivePushNotification", Utils.convert(copy));
            return;
        }
        DDNA.instance().recordNotificationOpened(launch, payload);
    }

    public static void recordNotificationDismissed(Bundle payload) {
        if (!UnityForwarder.isPresent()) {
            DDNA.instance().recordNotificationDismissed(payload);
        }
    }

    public static void markUnityLoaded() {
        UnityForwarder.getInstance().markLoaded();
    }

    private DDNANotifications() {
    }
}
