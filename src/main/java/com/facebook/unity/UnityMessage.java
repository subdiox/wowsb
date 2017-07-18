package com.facebook.unity;

import android.util.Log;
import com.facebook.internal.AnalyticsEvents;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UnityMessage {
    static final /* synthetic */ boolean $assertionsDisabled = (!UnityMessage.class.desiredAssertionStatus());
    private String methodName;
    private Map<String, Serializable> params = new HashMap();

    public UnityMessage(String methodName) {
        this.methodName = methodName;
    }

    public UnityMessage put(String name, Serializable value) {
        this.params.put(name, value);
        return this;
    }

    public UnityMessage putCancelled() {
        put(AnalyticsEvents.PARAMETER_SHARE_OUTCOME_CANCELLED, Boolean.valueOf(true));
        return this;
    }

    public UnityMessage putID(String id) {
        put("id", id);
        return this;
    }

    public void sendError(String errorMsg) {
        put("error", errorMsg);
        send();
    }

    public void send() {
        if ($assertionsDisabled || this.methodName != null) {
            String message = new UnityParams(this.params).toString();
            Log.v(FB.TAG, "sending to Unity " + this.methodName + "(" + message + ")");
            try {
                UnityReflection.SendMessage("UnityFacebookSDKPlugin", this.methodName, message);
                return;
            } catch (UnsatisfiedLinkError e) {
                Log.v(FB.TAG, "message not send, Unity not initialized");
                return;
            }
        }
        throw new AssertionError("no method specified");
    }

    public static UnityMessage createWithCallbackFromParams(String methodName, UnityParams params) {
        UnityMessage unityMessage = new UnityMessage(methodName);
        if (params.hasString(Constants.CALLBACK_ID_KEY).booleanValue()) {
            unityMessage.put(Constants.CALLBACK_ID_KEY, params.getString(Constants.CALLBACK_ID_KEY));
        }
        return unityMessage;
    }
}
