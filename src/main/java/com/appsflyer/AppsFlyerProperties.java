package com.appsflyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class AppsFlyerProperties {
    public static final String ADDITIONAL_CUSTOM_DATA = "additionalCustomData";
    public static final String AF_KEY = "AppsFlyerKey";
    public static final String APP_ID = "appid";
    public static final String APP_USER_ID = "AppUserId";
    public static final String CHANNEL = "channel";
    public static final String COLLECT_ANDROID_ID = "collectAndroidId";
    public static final String COLLECT_FACEBOOK_ATTR_ID = "collectFacebookAttrId";
    public static final String COLLECT_FINGER_PRINT = "collectFingerPrint";
    public static final String COLLECT_IMEI = "collectIMEI";
    public static final String COLLECT_MAC = "collectMAC";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String DEVICE_TRACKING_DISABLED = "deviceTrackingDisabled";
    public static final String DISABLE_LOGS_COMPLETELY = "disableLogs";
    public static final String DISABLE_OTHER_SDK = "disableOtherSdk";
    public static final String EMAIL_CRYPT_TYPE = "userEmailsCryptType";
    public static final String ENABLE_GPS_FALLBACK = "enableGpsFallback";
    public static final String EXTENSION = "sdkExtension";
    public static final String GCM_PROJECT_NUMBER = "gcmProjectNumber";
    public static final String IS_MONITOR = "shouldMonitor";
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String PUSH_PAYLOAD_HISTORY_SIZE = "pushPayloadHistorySize";
    public static final String PUSH_PAYLOAD_MAX_AGING = "pushPayloadMaxAging";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_EMAILS = "userEmails";
    public static final String USE_HTTP_FALLBACK = "useHttpFallback";
    private static AppsFlyerProperties instance = new AppsFlyerProperties();
    private boolean isLaunchCalled;
    private boolean isOnReceiveCalled;
    private Map<String, Object> properties = new HashMap();
    private boolean propertiesLoadedFlag = false;
    private String referrer;

    public enum EmailsCryptType {
        NONE(0),
        SHA1(1),
        MD5(2);
        
        private final int value;

        private EmailsCryptType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private AppsFlyerProperties() {
    }

    public static AppsFlyerProperties getInstance() {
        return instance;
    }

    public void set(String key, String value) {
        this.properties.put(key, value);
    }

    public void set(String key, String[] value) {
        this.properties.put(key, value);
    }

    public void set(String key, int value) {
        this.properties.put(key, Integer.toString(value));
    }

    public void set(String key, long value) {
        this.properties.put(key, Long.toString(value));
    }

    public void set(String key, boolean value) {
        this.properties.put(key, Boolean.toString(value));
    }

    public void setCustomData(String customData) {
        this.properties.put(ADDITIONAL_CUSTOM_DATA, customData);
    }

    public void setUserEmails(String emails) {
        this.properties.put(USER_EMAILS, emails);
    }

    public String[] getStringArray(String key) {
        return (String[]) this.properties.get(key);
    }

    public String getString(String key) {
        return (String) this.properties.get(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String string = getString(key);
        return string == null ? defaultValue : Boolean.valueOf(string).booleanValue();
    }

    public int getInt(String key, int defaultValue) {
        String string = getString(key);
        return string == null ? defaultValue : Integer.valueOf(string).intValue();
    }

    public long getLong(String key, long defaultValue) {
        String string = getString(key);
        return string == null ? defaultValue : Long.valueOf(string).longValue();
    }

    protected boolean isOnReceiveCalled() {
        return this.isOnReceiveCalled;
    }

    protected void setOnReceiveCalled() {
        this.isOnReceiveCalled = true;
    }

    protected boolean isFirstLaunchCalled() {
        return this.isLaunchCalled;
    }

    protected void setFirstLaunchCalled(boolean val) {
        this.isLaunchCalled = val;
    }

    protected void setFirstLaunchCalled() {
        this.isLaunchCalled = true;
    }

    protected void setReferrer(String referrer) {
        set("AF_REFERRER", referrer);
        this.referrer = referrer;
    }

    public String getReferrer(Context context) {
        if (this.referrer != null) {
            return this.referrer;
        }
        if (getString("AF_REFERRER") != null) {
            return getString("AF_REFERRER");
        }
        if (context != null) {
            return context.getSharedPreferences("appsflyer-data", 0).getString("referrer", null);
        }
        return null;
    }

    public void enableLogOutput(boolean shouldEnable) {
        set("shouldLog", shouldEnable);
    }

    public boolean isEnableLog() {
        return getBoolean("shouldLog", true);
    }

    public boolean isLogsDisabledCompletely() {
        return getBoolean(DISABLE_LOGS_COMPLETELY, false);
    }

    public boolean isOtherSdkStringDisabled() {
        return getBoolean(DISABLE_OTHER_SDK, false);
    }

    @SuppressLint({"CommitPrefEdits"})
    public void saveProperties(SharedPreferences sharedPreferences) {
        String jSONObject = new JSONObject(this.properties).toString();
        Editor edit = sharedPreferences.edit();
        edit.putString("savedProperties", jSONObject);
        if (VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }

    public void loadProperties(Context context) {
        if (!isPropertiesLoaded()) {
            String string = context.getSharedPreferences("appsflyer-data", 0).getString("savedProperties", null);
            if (string != null) {
                a.afDebugLog("Loading properties..");
                try {
                    JSONObject jSONObject = new JSONObject(string);
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        string = (String) keys.next();
                        if (this.properties.get(string) == null) {
                            this.properties.put(string, jSONObject.getString(string));
                        }
                    }
                    this.propertiesLoadedFlag = true;
                } catch (Throwable e) {
                    a.afLogE("Failed loading properties", e);
                }
                a.afDebugLog("Done loading properties: " + this.propertiesLoadedFlag);
            }
        }
    }

    private boolean isPropertiesLoaded() {
        return this.propertiesLoadedFlag;
    }
}
