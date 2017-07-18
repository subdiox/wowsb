package com.deltadna.android.sdk.notifications;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PushMessage implements Serializable {
    protected static final String MESSAGE = "alert";
    private static final String TAG = ("deltaDNA " + PushMessage.class.getSimpleName());
    protected static final String TITLE = "title";
    public final Map<String, String> data;
    public final String from;
    @DrawableRes
    public final int icon;
    public final long id;
    public final String message;
    public final String title;

    PushMessage(Context context, String from, Map<String, String> data) {
        this.from = from;
        this.data = new HashMap(data);
        this.id = getId(data);
        this.icon = getIcon(context);
        this.title = getTitle(context, data);
        this.message = getMessage(data);
    }

    protected long getId(Map<String, String> data) {
        if (data.containsKey("_ddCampaign")) {
            try {
                return Long.parseLong((String) data.get("_ddCampaign"));
            } catch (NumberFormatException e) {
                Log.w(TAG, "Failed parsing _ddCampaign to a long", e);
            }
        }
        return -1;
    }

    @DrawableRes
    protected int getIcon(Context context) {
        Bundle metaData = MetaData.get(context);
        if (metaData.containsKey("ddna_notification_icon")) {
            try {
                int identifier = context.getResources().getIdentifier(metaData.getString("ddna_notification_icon"), "drawable", context.getPackageName());
                if (identifier != 0) {
                    return identifier;
                }
                Log.w(TAG, "Failed to find drawable resource for ddna_notification_icon");
            } catch (NotFoundException e) {
                Log.w(TAG, "Failed to find drawable resource for ddna_notification_icon", e);
            }
        }
        try {
            int res = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).icon;
            if (res != 0) {
                return res;
            }
            Log.w(TAG, "Failed to find application's icon");
            return R.drawable.ddna_ic_stat_logo;
        } catch (NameNotFoundException e2) {
            Log.w(TAG, "Failed to find application's icon", e2);
        }
    }

    protected String getTitle(Context context, Map<String, String> data) {
        if (data.containsKey("title")) {
            return (String) data.get("title");
        }
        Bundle metaData = MetaData.get(context);
        if (metaData.containsKey("ddna_notification_title")) {
            Object value = metaData.get("ddna_notification_title");
            if (value instanceof String) {
                return (String) value;
            }
            if (value instanceof Integer) {
                try {
                    return context.getString(((Integer) value).intValue());
                } catch (NotFoundException e) {
                    Log.w(TAG, "Failed to find string resource for ddna_notification_title", e);
                }
            } else {
                Log.w(TAG, String.format(Locale.US, "Found %s for %s, only string literals or string resources allowed", new Object[]{value, "ddna_notification_title"}));
            }
        }
        return String.valueOf(context.getPackageManager().getApplicationLabel(context.getApplicationInfo()));
    }

    protected String getMessage(Map<String, String> data) {
        if (data.containsKey(MESSAGE)) {
            return (String) data.get(MESSAGE);
        }
        Log.w(TAG, "Failed to find notification message in playload");
        return "";
    }

    public String toString() {
        return String.format(Locale.US, "%s{from: %s, data: %s, id: %d, icon: %s, title: %s, message: %s}", new Object[]{getClass().getSimpleName(), this.from, this.data, Long.valueOf(this.id), Integer.valueOf(this.icon), this.title, this.message});
    }
}
