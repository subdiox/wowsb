package com.deltadna.android.sdk.notifications;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

final class UnityForwarder {
    private static final Class<?> PLAYER;
    private static final Class<?> PLAYER_ACTIVITY;
    private static final String TAG = ("deltaDNA " + UnityForwarder.class.getSimpleName());
    private static UnityForwarder instance;
    private final Queue<Message> deferred = new LinkedList();
    private boolean loaded;

    private static final class Message {
        final String gameObject;
        final String message;
        final String methodName;

        Message(String gameObject, String methodName, String message) {
            this.gameObject = gameObject;
            this.methodName = methodName;
            this.message = message;
        }
    }

    static {
        Class<?> playerActivity;
        Class<?> player;
        try {
            playerActivity = Class.forName("com.unity3d.player.UnityPlayerActivity");
        } catch (ClassNotFoundException e) {
            playerActivity = null;
        }
        PLAYER_ACTIVITY = playerActivity;
        try {
            player = Class.forName("com.unity3d.player.UnityPlayer");
        } catch (ClassNotFoundException e2) {
            player = null;
        }
        PLAYER = player;
    }

    static boolean isPresent() {
        return PLAYER_ACTIVITY != null;
    }

    static synchronized UnityForwarder getInstance() {
        UnityForwarder unityForwarder;
        synchronized (UnityForwarder.class) {
            if (instance == null) {
                instance = new UnityForwarder();
            }
            unityForwarder = instance;
        }
        return unityForwarder;
    }

    private UnityForwarder() {
    }

    void markLoaded() {
        Log.d(TAG, "Marked as loaded");
        this.loaded = true;
        if (!this.deferred.isEmpty()) {
            Message message = (Message) this.deferred.remove();
            sendMessage(message.gameObject, message.methodName, message.message);
        }
    }

    void forward(String gameObject, String methodName, String message) {
        if (this.loaded) {
            sendMessage(gameObject, methodName, message);
            return;
        }
        Log.d(TAG, "Deferring message due to not loaded");
        this.deferred.add(new Message(gameObject, methodName, message));
    }

    private static void sendMessage(String gameObject, String methodName, String message) {
        try {
            PLAYER.getDeclaredMethod("UnitySendMessage", new Class[]{String.class, String.class, String.class}).invoke(PLAYER, new Object[]{gameObject, methodName, message});
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Failed sending message to Unity", e);
        } catch (InvocationTargetException e2) {
            Log.e(TAG, "Failed sending message to Unity", e2);
        } catch (IllegalAccessException e3) {
            Log.e(TAG, "Failed sending message to Unity", e3);
        }
    }
}
