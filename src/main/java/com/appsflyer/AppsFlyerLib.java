package com.appsflyer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.appsflyer.AdvertisingIdClient.AdInfo;
import com.appsflyer.AppsFlyerProperties.EmailsCryptType;
import com.appsflyer.cache.CacheManager;
import com.appsflyer.cache.RequestCacheData;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppsFlyerLib {
    public static final String APPS_TRACKING_URL = ("https://t.appsflyer.com/api/v" + SERVER_BUILD_NUMBER + "/androidevent?buildnumber=" + SDK_BUILD_NUMBER + "&app_id=");
    public static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    public static final String ATTRIBUTION_ID_CONTENT_URI = "content://com.facebook.katana.provider.AttributionIdProvider";
    public static final String BUILD_NUMBER = "4.6.2";
    public static final String EVENTS_TRACKING_URL = ("https://events.appsflyer.com/api/v" + SERVER_BUILD_NUMBER + "/androidevent?buildnumber=" + SDK_BUILD_NUMBER + "&app_id=");
    private static final List<String> IGNORABLE_KEYS = Arrays.asList(new String[]{"is_cache"});
    public static final String JENKINS_BUILD_NUMBER = "287";
    public static final String LOG_TAG = ("AppsFlyer_" + SDK_BUILD_NUMBER);
    public static final String PRE_INSTALL_SYSTEM_DEFAULT = "/data/local/tmp/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_DEFAULT_ETC = "/etc/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_RO_PROP = "ro.appsflyer.preinstall.path";
    private static final String REGISTER_URL = ("https://register.appsflyer.com/api/v" + SERVER_BUILD_NUMBER + "/androidevent?buildnumber=" + SDK_BUILD_NUMBER + "&app_id=");
    public static final String SDK_BUILD_NUMBER = BUILD_NUMBER.substring(BUILD_NUMBER.indexOf(".") + 1);
    public static final String SERVER_BUILD_NUMBER = BUILD_NUMBER.substring(0, BUILD_NUMBER.indexOf("."));
    private static ScheduledExecutorService cacheScheduler = null;
    private static AppsFlyerConversionListener conversionDataListener = null;
    private static AppsFlyerLib instance = new AppsFlyerLib();
    private static boolean isDuringCheckCache = false;
    private static long lastCacheCheck;
    private static long timeInApp;
    private static String userCustomAndroidId;
    private static String userCustomImei;
    private static AppsFlyerInAppPurchaseValidatorListener validatorListener = null;
    private boolean isRetargetingTestMode = false;
    Uri latestDeepLink = null;
    private a listener;
    private String pushPayload;
    private Map<Long, String> pushPayloadHistory;
    private long testModeStartTime;

    private abstract class g implements Runnable {
        private HashMap<String, String> additionalParams;
        private String appsFlyerDevKey;
        protected WeakReference<Context> ctxReference = null;
        private String currency;
        private ScheduledExecutorService executorService;
        private String googlePublicKey;
        private String price;
        private String purchaseData;
        private String signature;

        public abstract String getUrl();

        protected abstract void validateCallback(boolean z, String str, String str2, String str3, HashMap<String, String> hashMap, String str4);

        public g(Context context, String str, String str2, String str3, String str4, String str5, String str6, HashMap<String, String> hashMap, ScheduledExecutorService scheduledExecutorService) {
            this.ctxReference = new WeakReference(context);
            this.appsFlyerDevKey = str;
            this.googlePublicKey = str2;
            this.purchaseData = str4;
            this.price = str5;
            this.currency = str6;
            this.additionalParams = hashMap;
            this.signature = str3;
            this.executorService = scheduledExecutorService;
        }

        public void run() {
            HttpURLConnection httpURLConnection;
            OutputStreamWriter outputStreamWriter;
            Throwable th;
            HttpURLConnection httpURLConnection2;
            Throwable th2;
            HttpURLConnection httpURLConnection3 = null;
            if (this.appsFlyerDevKey != null && this.appsFlyerDevKey.length() != 0) {
                try {
                    Context context = (Context) this.ctxReference.get();
                    if (context != null) {
                        Map hashMap = new HashMap();
                        hashMap.put("app_id", context.getPackageName());
                        hashMap.put("dev_key", this.appsFlyerDevKey);
                        hashMap.put("public-key", this.googlePublicKey);
                        hashMap.put("sig-data", this.purchaseData);
                        hashMap.put("signature", this.signature);
                        String jSONObject = new JSONObject(hashMap).toString();
                        httpURLConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
                        try {
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setRequestProperty("Content-Length", jSONObject.getBytes().length + "");
                            httpURLConnection.setRequestProperty("Connection", "close");
                            httpURLConnection.setRequestProperty("Content-Type", "application/json");
                            httpURLConnection.setConnectTimeout(10000);
                            httpURLConnection.setDoOutput(true);
                            try {
                                outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                                try {
                                    outputStreamWriter.write(jSONObject);
                                    if (outputStreamWriter != null) {
                                        outputStreamWriter.close();
                                    }
                                    int responseCode = httpURLConnection.getResponseCode();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                    StringBuilder stringBuilder = new StringBuilder();
                                    while (true) {
                                        jSONObject = bufferedReader.readLine();
                                        if (jSONObject == null) {
                                            break;
                                        }
                                        stringBuilder.append(jSONObject);
                                    }
                                    String stringBuilder2 = stringBuilder.toString();
                                    JSONObject jSONObject2 = new JSONObject(stringBuilder2);
                                    if (responseCode == 200) {
                                        a.afLog("Validate response 200 ok: " + jSONObject2.toString());
                                        validateCallback(jSONObject2.getBoolean("result"), this.purchaseData, this.price, this.currency, this.additionalParams, null);
                                    } else {
                                        a.afLog("Failed Validate request");
                                        validateCallback(false, this.purchaseData, this.price, this.currency, this.additionalParams, stringBuilder2);
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    if (outputStreamWriter != null) {
                                        outputStreamWriter.close();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                outputStreamWriter = httpURLConnection3;
                                if (outputStreamWriter != null) {
                                    outputStreamWriter.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            throw th;
                        }
                        this.executorService.shutdown();
                    } else if (httpURLConnection3 != null) {
                        httpURLConnection3.disconnect();
                    }
                } catch (Throwable th6) {
                    th = th6;
                    httpURLConnection = httpURLConnection3;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            }
        }
    }

    private class a extends g {
        public a(Context context, String str, String str2, String str3, String str4, String str5, String str6, HashMap<String, String> hashMap, ScheduledExecutorService scheduledExecutorService) {
            super(context, str, str2, str3, str4, str5, str6, hashMap, scheduledExecutorService);
        }

        public String getUrl() {
            return "https://sdk-services.appsflyer.com/validate-android-signature";
        }

        protected void validateCallback(boolean validated, String purchaseData, String price, String currency, HashMap<String, String> additionalParams, String error) {
            if (AppsFlyerLib.validatorListener != null) {
                a.afLog("Validate callback parameters: " + purchaseData + " " + price + " " + currency);
                if (error != null) {
                    AppsFlyerLib.validatorListener.onValidateInAppFailure(error);
                    a.afLog("Validate in app purchase failed: error : " + error);
                } else if (validated) {
                    a.afLog("Validate in app purchase success");
                    AppsFlyerLib.validatorListener.onValidateInApp();
                } else {
                    a.afLog("Validate in app purchase failed");
                    AppsFlyerLib.validatorListener.onValidateInAppFailure("Failed validating");
                }
                Map hashMap = new HashMap();
                hashMap.put(AFInAppEventParameterName.VALIDATED, Boolean.valueOf(validated));
                hashMap.put(AFInAppEventParameterName.PARAM_2, purchaseData);
                hashMap.put(AFInAppEventParameterName.REVENUE, price);
                hashMap.put(AFInAppEventParameterName.CURRENCY, currency);
                if (additionalParams != null) {
                    hashMap.put(AFInAppEventParameterName.PARAM_1, additionalParams);
                }
                AppsFlyerLib.this.trackEvent((Context) this.ctxReference.get(), AFInAppEventType.PURCHASE, hashMap);
            }
        }
    }

    private abstract class b implements Runnable {
        private String appsFlyerDevKey;
        protected WeakReference<Context> ctxReference = null;
        private AtomicInteger currentRequestsCounter = new AtomicInteger(0);
        private ScheduledExecutorService executorService;

        protected abstract void attributionCallback(Map<String, String> map);

        protected abstract void attributionCallbackFailure(String str, int i);

        public abstract String getUrl();

        public b(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
            this.ctxReference = new WeakReference(context);
            this.appsFlyerDevKey = str;
            this.executorService = scheduledExecutorService;
        }

        public void run() {
            InputStreamReader inputStreamReader;
            Throwable th;
            BufferedReader bufferedReader;
            Object obj;
            Reader reader = null;
            if (this.appsFlyerDevKey != null && this.appsFlyerDevKey.length() != 0) {
                this.currentRequestsCounter.incrementAndGet();
                HttpURLConnection httpURLConnection;
                try {
                    Context context = (Context) this.ctxReference.get();
                    if (context == null) {
                        this.currentRequestsCounter.decrementAndGet();
                        if (reader != null) {
                            reader.disconnect();
                            return;
                        }
                        return;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    String access$1200 = AppsFlyerLib.this.getCachedChannel(context, AppsFlyerLib.this.getConfiguredChannel(new WeakReference(context)));
                    String str = "";
                    if (access$1200 != null) {
                        str = "-" + access$1200;
                    }
                    StringBuilder append = new StringBuilder().append(getUrl()).append(context.getPackageName()).append(str).append("?devkey=").append(this.appsFlyerDevKey).append("&device_id=").append(g.id(new WeakReference(context)));
                    h.logMessageMaskKey("Calling server for attribution url: " + append.toString());
                    httpURLConnection = (HttpURLConnection) new URL(append.toString()).openConnection();
                    try {
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(10000);
                        httpURLConnection.setRequestProperty("Connection", "close");
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == 200) {
                            AppsFlyerLib.this.saveLongToSharedPreferences(context, "appsflyerGetConversionDataTiming", (System.currentTimeMillis() - currentTimeMillis) / 1000);
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                                try {
                                    BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader);
                                    while (true) {
                                        try {
                                            String readLine = bufferedReader2.readLine();
                                            if (readLine == null) {
                                                break;
                                            }
                                            stringBuilder.append(readLine).append('\n');
                                        } catch (Throwable th2) {
                                            th = th2;
                                            bufferedReader = bufferedReader2;
                                        }
                                    }
                                    if (bufferedReader2 != null) {
                                        bufferedReader2.close();
                                    }
                                    if (inputStreamReader != null) {
                                        inputStreamReader.close();
                                    }
                                    h.logMessageMaskKey("Attribution data: " + stringBuilder.toString());
                                    if (stringBuilder.length() > 0 && context != null) {
                                        Map access$1400 = AppsFlyerLib.this.attributionStringToMap(stringBuilder.toString());
                                        access$1200 = (String) access$1400.get("iscache");
                                        if (access$1200 != null && "false".equals(access$1200)) {
                                            AppsFlyerLib.this.saveLongToSharedPreferences(context, "appsflyerConversionDataCacheExpiration", System.currentTimeMillis());
                                        }
                                        String jSONObject = new JSONObject(access$1400).toString();
                                        if (jSONObject != null) {
                                            AppsFlyerLib.this.saveDataToSharedPreferences(context, "attributionId", jSONObject);
                                        } else {
                                            AppsFlyerLib.this.saveDataToSharedPreferences(context, "attributionId", stringBuilder.toString());
                                        }
                                        a.afDebugLog("iscache=" + access$1200 + " caching conversion data");
                                        if (AppsFlyerLib.conversionDataListener != null && this.currentRequestsCounter.intValue() <= 1) {
                                            Map access$1600;
                                            try {
                                                access$1600 = AppsFlyerLib.this.getConversionData(context);
                                            } catch (b e) {
                                                access$1600 = access$1400;
                                            }
                                            attributionCallback(access$1600);
                                        }
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    if (inputStreamReader != null) {
                                        inputStreamReader.close();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                Reader reader2 = reader;
                                if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                                if (inputStreamReader != null) {
                                    inputStreamReader.close();
                                }
                                throw th;
                            }
                        }
                        if (AppsFlyerLib.conversionDataListener != null) {
                            attributionCallbackFailure("Error connection to server: " + httpURLConnection.getResponseCode(), httpURLConnection.getResponseCode());
                        }
                        h.logMessageMaskKey("AttributionIdFetcher response code: " + httpURLConnection.getResponseCode() + "  url: " + append);
                        this.currentRequestsCounter.decrementAndGet();
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        try {
                            if (AppsFlyerLib.conversionDataListener != null) {
                                attributionCallbackFailure(th.getMessage(), 0);
                            }
                            a.afLogE(th.getMessage(), th);
                            this.currentRequestsCounter.decrementAndGet();
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            this.executorService.shutdown();
                        } catch (Throwable th6) {
                            th = th6;
                            this.currentRequestsCounter.decrementAndGet();
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            throw th;
                        }
                    }
                    this.executorService.shutdown();
                } catch (Throwable th7) {
                    th = th7;
                    obj = reader;
                    this.currentRequestsCounter.decrementAndGet();
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            }
        }
    }

    private class c implements Runnable {
        private WeakReference<Context> ctxReference = null;

        public c(Context context) {
            this.ctxReference = new WeakReference(context);
        }

        public void run() {
            if (!AppsFlyerLib.isDuringCheckCache) {
                AppsFlyerLib.lastCacheCheck = System.currentTimeMillis();
                if (this.ctxReference != null) {
                    AppsFlyerLib.isDuringCheckCache = true;
                    try {
                        String access$200 = AppsFlyerLib.this.getProperty(AppsFlyerProperties.AF_KEY);
                        synchronized (this.ctxReference) {
                            for (RequestCacheData requestCacheData : CacheManager.getInstance().getCachedRequests((Context) this.ctxReference.get())) {
                                a.afLog("resending request: " + requestCacheData.getRequestURL());
                                try {
                                    AppsFlyerLib.this.sendRequestToServer(requestCacheData.getRequestURL() + "&isCachedRequest=true&timeincache=" + Long.toString((System.currentTimeMillis() - Long.parseLong(requestCacheData.getCacheKey(), 10)) / 1000), requestCacheData.getPostData(), access$200, this.ctxReference, requestCacheData.getCacheKey(), false);
                                } catch (Exception e) {
                                    a.afLog("Failed to resend cached request");
                                }
                            }
                        }
                        AppsFlyerLib.isDuringCheckCache = false;
                    } catch (Exception e2) {
                        try {
                            a.afLog("failed to check cache.");
                        } finally {
                            AppsFlyerLib.isDuringCheckCache = false;
                        }
                    }
                    AppsFlyerLib.cacheScheduler.shutdown();
                    AppsFlyerLib.cacheScheduler = null;
                }
            }
        }
    }

    private class d implements Runnable {
        private String appsFlyerKey;
        private WeakReference<Context> context;
        private String eventName;
        private String eventValue;
        private ExecutorService executor;
        private boolean isNewAPI;
        private String referrer;

        private d(WeakReference<Context> weakReference, String str, String str2, String str3, String str4, boolean z, ExecutorService executorService) {
            this.context = weakReference;
            this.appsFlyerKey = str;
            this.eventName = str2;
            this.eventValue = str3;
            this.referrer = str4;
            this.isNewAPI = z;
            this.executor = executorService;
        }

        public void run() {
            AppsFlyerLib.this.sendTrackingWithEvent((Context) this.context.get(), this.appsFlyerKey, this.eventName, this.eventValue, this.referrer, this.isNewAPI);
            this.executor.shutdown();
        }
    }

    private class e extends b {
        public e(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
            super(context, str, scheduledExecutorService);
        }

        public String getUrl() {
            return "https://api.appsflyer.com/install_data/v3/";
        }

        protected void attributionCallback(Map<String, String> conversionData) {
            AppsFlyerLib.conversionDataListener.onInstallConversionDataLoaded(conversionData);
            ((Context) this.ctxReference.get()).getSharedPreferences("appsflyer-data", 0);
            AppsFlyerLib.this.saveIntegerToSharedPreferences((Context) this.ctxReference.get(), "appsflyerConversionDataRequestRetries", 0);
        }

        protected void attributionCallbackFailure(String error, int responseCode) {
            AppsFlyerLib.conversionDataListener.onInstallConversionFailure(error);
            if (responseCode >= 400 && responseCode < 500) {
                AppsFlyerLib.this.saveIntegerToSharedPreferences((Context) this.ctxReference.get(), "appsflyerConversionDataRequestRetries", ((Context) this.ctxReference.get()).getSharedPreferences("appsflyer-data", 0).getInt("appsflyerConversionDataRequestRetries", 0) + 1);
            }
        }
    }

    private class f implements Runnable {
        private WeakReference<Context> ctxReference;
        boolean isLaunch;
        Map<String, String> params;
        private String urlString;

        private f(String str, Map<String, String> map, Context context, boolean z) {
            this.ctxReference = null;
            this.urlString = str;
            this.params = map;
            this.ctxReference = new WeakReference(context);
            this.isLaunch = z;
        }

        public void run() {
            boolean z = true;
            try {
                String referrer;
                boolean z2;
                Context context = (Context) this.ctxReference.get();
                if (context != null) {
                    boolean z3;
                    referrer = AppsFlyerProperties.getInstance().getReferrer(context);
                    if (referrer != null && referrer.length() > 0 && this.params.get("referrer") == null) {
                        this.params.put("referrer", referrer);
                    }
                    SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
                    boolean equals = ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equals(sharedPreferences.getString("sentSuccessfully", ""));
                    referrer = (String) this.params.get("eventName");
                    int access$600 = AppsFlyerLib.this.getCounter(sharedPreferences, "appsFlyerCount", referrer == null);
                    this.params.put("counter", Integer.toString(access$600));
                    Map map = this.params;
                    String str = "iaecounter";
                    AppsFlyerLib appsFlyerLib = AppsFlyerLib.this;
                    String str2 = "appsFlyerInAppEventCount";
                    if (referrer != null) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    map.put(str, Integer.toString(appsFlyerLib.getCounter(sharedPreferences, str2, z3)));
                    this.params.put("timepassedsincelastlaunch", Long.toString(AppsFlyerLib.this.getTimePassedSinceLastLaunch(context, true)));
                    if (this.isLaunch && access$600 == 1) {
                        AppsFlyerProperties.getInstance().setFirstLaunchCalled();
                    }
                    z2 = equals;
                } else {
                    z2 = false;
                }
                Map map2 = this.params;
                String str3 = "isFirstCall";
                if (z2) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                map2.put(str3, Boolean.toString(z2));
                str3 = (String) this.params.get("appsflyerKey");
                if (str3 == null || str3.length() == 0) {
                    a.afDebugLog("Not sending data yet, waiting for dev key");
                    return;
                }
                this.params.put("af_v", new f().getHashCode(this.params));
                this.params.put("af_v2", new f().getHashCodeV2(this.params));
                String jSONObject = new JSONObject(this.params).toString();
                AppsFlyerLib appsFlyerLib2 = AppsFlyerLib.this;
                referrer = this.urlString;
                WeakReference weakReference = this.ctxReference;
                if (!this.isLaunch || AppsFlyerLib.conversionDataListener == null) {
                    z = false;
                }
                appsFlyerLib2.sendRequestToServer(referrer, jSONObject, str3, weakReference, null, z);
            } catch (Throwable e) {
                Throwable th = e;
                if (null != null && this.ctxReference != null && !this.urlString.contains("&isCachedRequest=true&timeincache=")) {
                    CacheManager.getInstance().cacheRequest(new RequestCacheData(this.urlString, null, AppsFlyerLib.SDK_BUILD_NUMBER), (Context) this.ctxReference.get());
                    a.afLogE(th.getMessage(), th);
                }
            } catch (Throwable e2) {
                a.afLogE(e2.getMessage(), e2);
            }
        }
    }

    void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(AppsFlyerProperties.IS_MONITOR);
        if (stringExtra != null) {
            a.afLog("Turning on monitoring.");
            AppsFlyerProperties.getInstance().set(AppsFlyerProperties.IS_MONITOR, stringExtra.equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE));
            monitor(context, null, "START_TRACKING", context.getPackageName());
            return;
        }
        a.afLog("****** onReceive called *******");
        debugAction("******* onReceive: ", "", context);
        AppsFlyerProperties.getInstance().setOnReceiveCalled();
        String stringExtra2 = intent.getStringExtra("referrer");
        a.afLog("Play store referrer: " + stringExtra2);
        if (stringExtra2 != null) {
            stringExtra = intent.getStringExtra("TestIntegrationMode");
            if (stringExtra != null && stringExtra.equals("AppsFlyer_Test")) {
                Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
                edit.clear();
                editorCommit(edit);
                AppsFlyerProperties.getInstance().setFirstLaunchCalled(false);
                startTestMode();
            }
            debugAction("onReceive called. referrer: ", stringExtra2, context);
            saveDataToSharedPreferences(context, "referrer", stringExtra2);
            AppsFlyerProperties.getInstance().setReferrer(stringExtra2);
            if (AppsFlyerProperties.getInstance().isFirstLaunchCalled()) {
                a.afLog("onReceive: isLaunchCalled");
                runInBackground(context, null, null, null, stringExtra2, false);
            }
        }
    }

    void addReferrer(Context context, String referrer) {
        a.afDebugLog("received a new (extra) referrer: " + referrer);
        try {
            JSONObject jSONObject;
            JSONArray jSONArray;
            long currentTimeMillis = System.currentTimeMillis();
            String string = context.getSharedPreferences("appsflyer-data", 0).getString("extraReferrers", null);
            if (string == null) {
                jSONObject = new JSONObject();
                jSONArray = new JSONArray();
            } else {
                JSONObject jSONObject2 = new JSONObject(string);
                if (jSONObject2.has(referrer)) {
                    jSONArray = new JSONArray((String) jSONObject2.get(referrer));
                    jSONObject = jSONObject2;
                } else {
                    jSONArray = new JSONArray();
                    jSONObject = jSONObject2;
                }
            }
            jSONArray.put(currentTimeMillis);
            jSONObject.put(referrer, jSONArray.toString());
            saveDataToSharedPreferences(context, "extraReferrers", jSONObject.toString());
        } catch (JSONException e) {
        } catch (Throwable th) {
            a.afLogE("Couldn't save referrer - " + referrer + ": ", th);
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    private void editorCommit(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    private void startTestMode() {
        a.afLog("Test mode started..");
        this.testModeStartTime = System.currentTimeMillis();
    }

    private void endTestMode() {
        a.afLog("Test mode ended!");
        this.testModeStartTime = 0;
    }

    private boolean isInTestMode(String referrer) {
        return System.currentTimeMillis() - this.testModeStartTime <= 30000 && referrer != null && referrer.contains("AppsFlyer_Test");
    }

    private AppsFlyerLib() {
    }

    public static AppsFlyerLib getInstance() {
        return instance;
    }

    public String getSdkVersion() {
        return "version: 4.6.2 (build 287)";
    }

    private void registerForAppEvents(Application application) {
        if (this.listener == null) {
            AppsFlyerProperties.getInstance().loadProperties(application.getApplicationContext());
            if (VERSION.SDK_INT >= 14) {
                d.init(application);
                this.listener = new a() {
                    public void onBecameForeground(Activity currentActivity) {
                        a.afLog("onBecameForeground");
                        AppsFlyerLib.timeInApp = System.currentTimeMillis();
                        AppsFlyerLib.this.trackEvent(currentActivity, null, null);
                    }

                    public void onBecameBackground(WeakReference<Activity> currentActivity) {
                        a.afLog("onBecameBackground");
                        a.afLog("callStatsBackground background call");
                        AppsFlyerLib.this.callStatsBackground(new WeakReference(((Activity) currentActivity.get()).getApplicationContext()));
                    }
                };
                d.getInstance().registerListener(this.listener);
                return;
            }
            a.afLog("SDK<14 call trackAppLaunch manually");
            trackEvent(application.getApplicationContext(), null, null);
        }
    }

    private synchronized void registerOnGCM(final Context context) {
        final String property = getProperty(AppsFlyerProperties.GCM_PROJECT_NUMBER);
        if (property != null && getProperty("gcmToken") == null) {
            new AsyncTask<Void, Void, e>() {
                protected e doInBackground(Void... params) {
                    try {
                        Class.forName("com.google.android.gms.iid.InstanceID");
                        Class.forName("com.google.android.gms.gcm.GcmReceiver");
                        InstanceID instance = InstanceID.getInstance(context);
                        return new e(System.currentTimeMillis(), instance.getToken(property, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null), instance.getId());
                    } catch (ClassNotFoundException e) {
                        a.afLog("Please integrate Google Play Services in order to support uninstall feature");
                    } catch (IOException e2) {
                        a.afLog("Could not load registration ID");
                    } catch (Throwable th) {
                        a.afLog("Error registering for uninstall feature");
                    }
                    return null;
                }

                protected void onPostExecute(e newGcmToken) {
                    if (newGcmToken != null) {
                        e eVar = new e(AppsFlyerLib.this.getProperty("gcmTokenTimestamp"), AppsFlyerLib.this.getProperty("gcmToken"), AppsFlyerLib.this.getProperty("gcmInstanceId"));
                        if (eVar.update(newGcmToken)) {
                            a.afLog("token=" + eVar.getToken());
                            a.afLog("instance id=" + eVar.getInstanceId());
                            AppsFlyerLib.this.updateServerGcmToken(eVar, context);
                        }
                    }
                }
            }.execute(new Void[0]);
        }
    }

    void updateServerGcmToken(e existingGcmToken, Context context) {
        a.afLog("updateServerGcmToken called");
        AppsFlyerProperties.getInstance().set("gcmToken", existingGcmToken.getToken());
        AppsFlyerProperties.getInstance().set("gcmInstanceId", existingGcmToken.getInstanceId());
        AppsFlyerProperties.getInstance().set("gcmTokenTimestamp", String.valueOf(existingGcmToken.getTokenTimestamp()));
        callRegisterBackground(context);
    }

    @Deprecated
    public void setGCMProjectID(String id) {
        setGCMProjectNumber(id);
    }

    @Deprecated
    public void setGCMProjectNumber(String id) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.GCM_PROJECT_NUMBER, id);
    }

    public void setGCMProjectNumber(Context context, String id) {
        setProperty(AppsFlyerProperties.GCM_PROJECT_NUMBER, id);
        AppsFlyerProperties.getInstance().loadProperties(context);
        registerOnGCM(context);
    }

    public void setDebugLog(boolean shouldEnable) {
        AppsFlyerProperties.getInstance().enableLogOutput(shouldEnable);
    }

    public void setImeiData(String aImei) {
        userCustomImei = aImei;
    }

    public void setAndroidIdData(String aAndroidId) {
        userCustomAndroidId = aAndroidId;
    }

    private void debugAction(String actionMsg, String parameter, Context context) {
        try {
            if (isAppsFlyerPackage(context)) {
                DebugLogQueue.getInstance().push(actionMsg + parameter);
            }
        } catch (Exception e) {
            a.afLog(e.toString());
        }
    }

    private boolean isAppsFlyerPackage(Context context) {
        return context != null && context.getPackageName().length() > 12 && BuildConfig.APPLICATION_ID.equals(context.getPackageName().toLowerCase().substring(0, 13));
    }

    private void saveDataToSharedPreferences(Context context, String key, String value) {
        Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putString(key, value);
        editorCommit(edit);
    }

    private void saveIntegerToSharedPreferences(Context context, String key, int value) {
        Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putInt(key, value);
        editorCommit(edit);
    }

    private void saveLongToSharedPreferences(Context context, String key, long value) {
        Editor edit = context.getSharedPreferences("appsflyer-data", 0).edit();
        edit.putLong(key, value);
        editorCommit(edit);
    }

    private void setProperty(String key, String value) {
        AppsFlyerProperties.getInstance().set(key, value);
    }

    private String getProperty(String key) {
        return AppsFlyerProperties.getInstance().getString(key);
    }

    @Deprecated
    public void setAppUserId(String id) {
        setCustomerUserId(id);
    }

    public void setCustomerUserId(String id) {
        a.afLog("setCustomerUserId = " + id);
        setProperty(AppsFlyerProperties.APP_USER_ID, id);
    }

    public void setAdditionalData(HashMap<String, Object> customData) {
        AppsFlyerProperties.getInstance().setCustomData(new JSONObject(customData).toString());
    }

    public void sendDeepLinkData(Activity activity) {
        a.afLog("getDeepLinkData with activity " + activity.getIntent().getDataString());
        registerForAppEvents(activity.getApplication());
    }

    public void sendPushNotificationData(Activity activity) {
        Object th;
        this.pushPayload = getPushPayloadFromIntent(activity);
        if (this.pushPayload != null) {
            long j;
            long currentTimeMillis = System.currentTimeMillis();
            if (this.pushPayloadHistory == null) {
                a.afLog("pushes: initializing pushes history..");
                this.pushPayloadHistory = new ConcurrentHashMap();
                j = currentTimeMillis;
            } else {
                try {
                    long j2 = AppsFlyerProperties.getInstance().getLong(AppsFlyerProperties.PUSH_PAYLOAD_MAX_AGING, 1800000);
                    j = currentTimeMillis;
                    for (Long l : this.pushPayloadHistory.keySet()) {
                        try {
                            JSONObject jSONObject = new JSONObject(this.pushPayload);
                            JSONObject jSONObject2 = new JSONObject((String) this.pushPayloadHistory.get(l));
                            if (jSONObject.get("pid").equals(jSONObject2.get("pid"))) {
                                a.afLog("PushNotificationMeasurement: A previous payload with same PID was already acknowledged! (old: " + jSONObject2 + ", new: " + jSONObject + ")");
                                this.pushPayload = null;
                                return;
                            }
                            long longValue;
                            if (currentTimeMillis - l.longValue() > j2) {
                                this.pushPayloadHistory.remove(l);
                            }
                            if (l.longValue() <= j) {
                                longValue = l.longValue();
                            } else {
                                longValue = j;
                            }
                            j = longValue;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    j = currentTimeMillis;
                    a.afLog("Error while handling push notification measurement: " + th.getClass().getSimpleName());
                    if (this.pushPayloadHistory.size() == AppsFlyerProperties.getInstance().getInt(AppsFlyerProperties.PUSH_PAYLOAD_HISTORY_SIZE, 2)) {
                        a.afLog("pushes: removing oldest overflowing push (oldest push:" + j + ")");
                        this.pushPayloadHistory.remove(Long.valueOf(j));
                    }
                    this.pushPayloadHistory.put(Long.valueOf(currentTimeMillis), this.pushPayload);
                    registerForAppEvents(activity.getApplication());
                }
            }
            if (this.pushPayloadHistory.size() == AppsFlyerProperties.getInstance().getInt(AppsFlyerProperties.PUSH_PAYLOAD_HISTORY_SIZE, 2)) {
                a.afLog("pushes: removing oldest overflowing push (oldest push:" + j + ")");
                this.pushPayloadHistory.remove(Long.valueOf(j));
            }
            this.pushPayloadHistory.put(Long.valueOf(currentTimeMillis), this.pushPayload);
            registerForAppEvents(activity.getApplication());
        }
    }

    @Deprecated
    public void setUserEmail(String email) {
        setProperty(AppsFlyerProperties.USER_EMAIL, email);
    }

    public void setUserEmails(String... emails) {
        setUserEmails(EmailsCryptType.NONE, emails);
    }

    public void setUserEmails(EmailsCryptType cryptMethod, String... emails) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.EMAIL_CRYPT_TYPE, cryptMethod.getValue());
        HashMap hashMap = new HashMap();
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : emails) {
            stringBuilder.append(append);
            stringBuilder.append(",");
        }
        switch (cryptMethod) {
            case MD5:
                hashMap.put("md5_el_arr", f.toMD5(stringBuilder.toString()));
                break;
            case NONE:
                hashMap.put("plain_el_arr", stringBuilder.toString());
                break;
            default:
                hashMap.put("sha1_el_arr", f.toSHA1(stringBuilder.toString()));
                break;
        }
        AppsFlyerProperties.getInstance().setUserEmails(new JSONObject(hashMap).toString());
    }

    public void setCollectAndroidID(boolean isCollect) {
        setProperty(AppsFlyerProperties.COLLECT_ANDROID_ID, Boolean.toString(isCollect));
    }

    public void setCollectIMEI(boolean isCollect) {
        setProperty(AppsFlyerProperties.COLLECT_IMEI, Boolean.toString(isCollect));
    }

    public void setCollectFingerPrint(boolean isCollect) {
        setProperty(AppsFlyerProperties.COLLECT_FINGER_PRINT, Boolean.toString(isCollect));
    }

    public void startTracking(Application application, String key) {
        a.afLogM("Build Number: 287");
        setProperty(AppsFlyerProperties.AF_KEY, key);
        h.setDevKey(key);
        registerForAppEvents(application);
        if (AppsFlyerProperties.getInstance().getString("gcmToken") == null && AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.GCM_PROJECT_NUMBER) != null) {
            a.afLog("Found a 'Google Project Number' without token. Registering on GCM to get token..");
            registerOnGCM(application.getApplicationContext());
        }
    }

    private void getReInstallData(Context context) {
        if (VERSION.SDK_INT >= 18) {
            AFKeystoreWrapper aFKeystoreWrapper = new AFKeystoreWrapper(context);
            if (aFKeystoreWrapper.loadData()) {
                aFKeystoreWrapper.incrementReInstallCounter();
                setProperty("KSAppsFlyerId", aFKeystoreWrapper.getUid());
                setProperty("KSAppsFlyerRICounter", String.valueOf(aFKeystoreWrapper.getReInstallCounter()));
                return;
            }
            aFKeystoreWrapper.createFirstInstallData(g.id(new WeakReference(context)));
            setProperty("KSAppsFlyerId", aFKeystoreWrapper.getUid());
            setProperty("KSAppsFlyerRICounter", String.valueOf(aFKeystoreWrapper.getReInstallCounter()));
        }
    }

    public String getAppUserId() {
        return getProperty(AppsFlyerProperties.APP_USER_ID);
    }

    public void setAppId(String id) {
        setProperty(AppsFlyerProperties.APP_ID, id);
    }

    public String getAppId() {
        return getProperty(AppsFlyerProperties.APP_ID);
    }

    public void setExtension(String extension) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.EXTENSION, extension);
    }

    public void setIsUpdate(boolean isUpdate) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.IS_UPDATE, isUpdate);
    }

    public void setCurrencyCode(String currencyCode) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.CURRENCY_CODE, currencyCode);
    }

    public void trackLocation(Context context, double latitude, double longitude) {
        Map hashMap = new HashMap();
        hashMap.put(AFInAppEventParameterName.LONGTITUDE, Double.toString(longitude));
        hashMap.put(AFInAppEventParameterName.LATITUDE, Double.toString(latitude));
        trackEvent(context, AFInAppEventType.LOCATION_COORDINATES, hashMap);
    }

    private void callStatsBackground(WeakReference<Context> context) {
        if (context.get() != null) {
            a.afLog("app went to background");
            SharedPreferences sharedPreferences = ((Context) context.get()).getSharedPreferences("appsflyer-data", 0);
            AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
            long currentTimeMillis = System.currentTimeMillis() - timeInApp;
            Map hashMap = new HashMap();
            String property = getProperty(AppsFlyerProperties.AF_KEY);
            String property2 = getProperty("KSAppsFlyerId");
            hashMap.put("app_id", ((Context) context.get()).getPackageName());
            hashMap.put("devkey", property);
            hashMap.put("uid", g.id(context));
            hashMap.put("time_in_app", String.valueOf(currentTimeMillis / 1000));
            hashMap.put("statType", "user_closed_app");
            hashMap.put("platform", "Android");
            hashMap.put("launch_counter", Integer.toString(getCounter(sharedPreferences, "appsFlyerCount", false)));
            hashMap.put("gcd_conversion_data_timing", Long.toString(sharedPreferences.getLong("appsflyerGetConversionDataTiming", 0)));
            hashMap.put(AppsFlyerProperties.CHANNEL, getConfiguredChannel(context));
            hashMap.put("originalAppsflyerId", property2 != null ? property2 : "");
            Object string = AppsFlyerProperties.getInstance().getString("advertiserId");
            property2 = "advertiserId";
            if (string == null) {
                string = "";
            }
            hashMap.put(property2, string);
            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_FINGER_PRINT, true)) {
                String uniquePsuedoID = getUniquePsuedoID();
                if (uniquePsuedoID != null) {
                    hashMap.put("deviceFingerPrintId", uniquePsuedoID);
                }
            }
            try {
                c cVar = new c(null);
                cVar.bodyParameters = hashMap;
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                    a.afDebugLog("Main thread detected. Running callStats task in a new thread.");
                    cVar.execute(new String[]{"https://stats.appsflyer.com/stats"});
                    return;
                }
                a.afDebugLog("Running callStats task (on current thread: " + Thread.currentThread().toString() + " )");
                cVar.onPreExecute();
                cVar.onPostExecute(cVar.doInBackground("https://stats.appsflyer.com/stats"));
            } catch (Throwable th) {
                a.afLogE("Could not send callStats request", th);
            }
        }
    }

    public void trackAppLaunch(Context ctx, String devKey) {
        runInBackground(ctx, devKey, null, null, "", true);
    }

    protected void setDeepLinkData(Intent intent) {
        if (intent != null) {
            try {
                if ("android.intent.action.VIEW".equals(intent.getAction())) {
                    this.latestDeepLink = intent.getData();
                    a.afDebugLog("Unity setDeepLinkData = " + this.latestDeepLink);
                }
            } catch (Throwable th) {
            }
        }
    }

    public void reportTrackSession(Context ctx) {
        trackEvent(ctx, null, null);
    }

    public void trackEvent(Context context, String eventName, Map<String, Object> map) {
        Map hashMap;
        if (map == null) {
            hashMap = new HashMap();
        }
        JSONObject jSONObject = new JSONObject(hashMap);
        String referrer = AppsFlyerProperties.getInstance().getReferrer(context);
        String jSONObject2 = jSONObject.toString();
        if (referrer == null) {
            referrer = "";
        }
        runInBackground(context, null, eventName, jSONObject2, referrer, true);
    }

    private void monitor(Context context, String eventIdentifier, String message, String value) {
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.IS_MONITOR, false)) {
            Intent intent = new Intent("com.appsflyer.MonitorBroadcast");
            intent.setPackage("com.appsflyer.nightvision");
            intent.putExtra(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, message);
            intent.putExtra("value", value);
            intent.putExtra("packageName", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            intent.putExtra("pid", new Integer(Process.myPid()));
            intent.putExtra("eventIdentifier", eventIdentifier);
            intent.putExtra(ServerProtocol.DIALOG_PARAM_SDK_VERSION, SERVER_BUILD_NUMBER + '.' + SDK_BUILD_NUMBER);
            context.sendBroadcast(intent);
        }
    }

    private void callRegisterBackground(Context context) {
        Map hashMap = new HashMap();
        hashMap.put("devkey", getProperty(AppsFlyerProperties.AF_KEY));
        hashMap.put("uid", g.id(new WeakReference(context)));
        hashMap.put("af_gcm_token", AppsFlyerProperties.getInstance().getString("gcmToken"));
        hashMap.put("advertiserId", AppsFlyerProperties.getInstance().getString("advertiserId"));
        hashMap.put("af_google_instance_id", AppsFlyerProperties.getInstance().getString("gcmInstanceId"));
        hashMap.put("launch_counter", Integer.toString(getCounter(context.getSharedPreferences("appsflyer-data", 0), "appsFlyerCount", false)));
        hashMap.put(ServerProtocol.DIALOG_PARAM_SDK_VERSION, Integer.toString(VERSION.SDK_INT));
        hashMap.put(AppsFlyerProperties.CHANNEL, getConfiguredChannel(new WeakReference(context)));
        try {
            long j = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            hashMap.put("install_date", new SimpleDateFormat("yyyy-MM-dd_HHmmssZ", Locale.US).format(new Date(j)));
        } catch (NameNotFoundException e) {
        } catch (NoSuchFieldError e2) {
        }
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_FINGER_PRINT, true)) {
            String uniquePsuedoID = getUniquePsuedoID();
            if (uniquePsuedoID != null) {
                hashMap.put("deviceFingerPrintId", uniquePsuedoID);
            }
        }
        try {
            c cVar = new c(context);
            cVar.bodyParameters = hashMap;
            cVar.execute(new String[]{REGISTER_URL + context.getPackageName()});
        } catch (Throwable th) {
        }
    }

    private static void broadcastBacktoTestApp(Context context, HashMap<String, String> params) {
        Intent intent = new Intent("com.appsflyer.testIntgrationBroadcast");
        intent.putExtra(NativeProtocol.WEB_DIALOG_PARAMS, params);
        context.sendBroadcast(intent);
    }

    public void setDeviceTrackingDisabled(boolean isDisabled) {
        AppsFlyerProperties.getInstance().set(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, isDisabled);
    }

    private Map<String, String> getConversionData(Context context) throws b {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        String referrer = AppsFlyerProperties.getInstance().getReferrer(context);
        if (referrer != null && referrer.length() > 0 && referrer.contains("af_tranid")) {
            return referrerStringToMap(context, referrer);
        }
        String string = sharedPreferences.getString("attributionId", null);
        if (string != null && string.length() > 0) {
            return attributionStringToMap(string);
        }
        throw new b();
    }

    public void registerConversionListener(Context context, AppsFlyerConversionListener conversionDataListener) {
        if (conversionDataListener != null) {
            conversionDataListener = conversionDataListener;
        }
    }

    public void unregisterConversionListener() {
        conversionDataListener = null;
    }

    public void registerValidatorListener(Context context, AppsFlyerInAppPurchaseValidatorListener validationListener) {
        a.afDebugLog("registerValidatorListener called");
        if (validationListener == null) {
            a.afDebugLog("registerValidatorListener null listener");
        } else {
            validatorListener = validationListener;
        }
    }

    protected void getConversionData(Context context, final ConversionDataListener conversionDataListener) {
        registerConversionListener(context, new AppsFlyerConversionListener() {
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                conversionDataListener.onConversionDataLoaded(conversionData);
            }

            public void onInstallConversionFailure(String errorMessage) {
                conversionDataListener.onConversionFailure(errorMessage);
            }

            public void onAppOpenAttribution(Map<String, String> map) {
            }

            public void onAttributionFailure(String errorMessage) {
            }
        });
    }

    private Map<String, String> referrerStringToMap(Context context, String referrer) {
        Map<String, String> linkedHashMap = new LinkedHashMap();
        String[] split = referrer.split("&");
        int length = split.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Object substring;
            String str = split[i];
            int indexOf = str.indexOf("=");
            if (indexOf > 0) {
                substring = str.substring(0, indexOf);
            } else {
                String str2 = str;
            }
            if (!linkedHashMap.containsKey(substring)) {
                if (substring.equals("c")) {
                    substring = "campaign";
                } else if (substring.equals("pid")) {
                    substring = "media_source";
                } else if (substring.equals("af_prt")) {
                    i2 = 1;
                    substring = "agency";
                }
                linkedHashMap.put(substring, new String());
            }
            int i3 = i2;
            Object obj = substring;
            substring = (indexOf <= 0 || str.length() <= indexOf + 1) ? null : str.substring(indexOf + 1);
            linkedHashMap.put(obj, substring);
            i++;
            i2 = i3;
        }
        try {
            if (!linkedHashMap.containsKey("install_time")) {
                linkedHashMap.put("install_time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime)));
            }
        } catch (Exception e) {
            a.afWarnLog("Could not fetch install time");
        }
        if (!linkedHashMap.containsKey("af_status")) {
            linkedHashMap.put("af_status", "Non-organic");
        }
        if (i2 != 0) {
            linkedHashMap.remove("media_source");
        }
        return linkedHashMap;
    }

    private Map<String, String> attributionStringToMap(String inputString) {
        Map<String, String> hashMap = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(inputString);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!IGNORABLE_KEYS.contains(str)) {
                    hashMap.put(str, jSONObject.getString(str));
                }
            }
            return hashMap;
        } catch (JSONException e) {
            a.afWarnLog(e.getMessage());
            return null;
        }
    }

    private void runInBackground(Context context, String appsFlyerKey, String eventName, String eventValue, String referrer, boolean isNewAPI) {
        Object newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        newSingleThreadScheduledExecutor.schedule(new d(new WeakReference(context), appsFlyerKey, eventName, eventValue, referrer, isNewAPI, newSingleThreadScheduledExecutor), 5, TimeUnit.MILLISECONDS);
    }

    private void sendTrackingWithEvent(Context context, String appsFlyerKey, String eventName, String eventValue, String referrer, boolean isUseNewAPI) {
        if (context != null) {
            String str;
            SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
            AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
            a.afLog("sendTrackingWithEvent from activity: " + context.getClass().getName().toString());
            boolean z = eventName == null;
            Map hashMap = new HashMap();
            addAdvertiserIDData(context, hashMap);
            hashMap.put("af_timestamp", Long.toString(new Date().getTime()));
            debugAction("collect data for server", "", context);
            StringBuilder append = new StringBuilder().append("******* sendTrackingWithEvent: ");
            if (z) {
                str = "Launch";
            } else {
                str = eventName;
            }
            a.afLog(append.append(str).toString());
            String str2 = "********* sendTrackingWithEvent: ";
            if (z) {
                str = "Launch";
            } else {
                str = eventName;
            }
            debugAction(str2, str, context);
            str2 = LOG_TAG;
            String str3 = "EVENT_CREATED_WITH_NAME";
            if (z) {
                str = "Launch";
            } else {
                str = eventName;
            }
            monitor(context, str2, str3, str);
            CacheManager.getInstance().init(context);
            try {
                List asList = Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions);
                if (!asList.contains("android.permission.INTERNET")) {
                    a.afWarnLog("Permission android.permission.INTERNET is missing in the AndroidManifest.xml");
                    monitor(context, null, "PERMISSION_INTERNET_MISSING", null);
                }
                if (!asList.contains("android.permission.ACCESS_NETWORK_STATE")) {
                    a.afWarnLog("Permission android.permission.ACCESS_NETWORK_STATE is missing in the AndroidManifest.xml");
                }
                if (!asList.contains("android.permission.ACCESS_WIFI_STATE")) {
                    a.afWarnLog("Permission android.permission.ACCESS_WIFI_STATE is missing in the AndroidManifest.xml");
                }
            } catch (Exception e) {
            }
            try {
                append = new StringBuilder();
                append.append(z ? APPS_TRACKING_URL : EVENTS_TRACKING_URL).append(context.getPackageName());
                if (isUseNewAPI) {
                    hashMap.put("af_events_api", "1");
                }
                hashMap.put("brand", Build.BRAND);
                hashMap.put("device", Build.DEVICE);
                hashMap.put("product", Build.PRODUCT);
                hashMap.put(ServerProtocol.DIALOG_PARAM_SDK_VERSION, Integer.toString(VERSION.SDK_INT));
                hashMap.put("model", Build.MODEL);
                hashMap.put("deviceType", Build.TYPE);
                if (!z) {
                    lastEventsProcessing(context, hashMap, eventName, eventValue);
                } else if (isAppsFlyerFirstLaunch(context)) {
                    if (!AppsFlyerProperties.getInstance().isOtherSdkStringDisabled()) {
                        hashMap.put("af_sdks", generateOtherSDKsString());
                        hashMap.put("batteryLevel", String.valueOf(getBatteryLevel(context)));
                    }
                    getReInstallData(context);
                }
                str = getProperty("KSAppsFlyerId");
                str3 = getProperty("KSAppsFlyerRICounter");
                if (!(str == null || str3 == null || Integer.valueOf(str3).intValue() <= 0)) {
                    hashMap.put("reinstallCounter", str3);
                    hashMap.put("originalAppsflyerId", str);
                }
                str = getProperty(AppsFlyerProperties.ADDITIONAL_CUSTOM_DATA);
                if (str != null) {
                    hashMap.put("customData", str);
                }
                try {
                    str = context.getPackageManager().getInstallerPackageName(context.getPackageName());
                    if (str != null) {
                        hashMap.put("installer_package", str);
                    }
                } catch (Exception e2) {
                }
                str = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.EXTENSION);
                if (str != null && str.length() > 0) {
                    hashMap.put(AppsFlyerProperties.EXTENSION, str);
                }
                str = getConfiguredChannel(new WeakReference(context));
                str3 = getCachedChannel(context, str);
                if (str3 != null) {
                    hashMap.put(AppsFlyerProperties.CHANNEL, str3);
                }
                if (!(str3 == null || str3.equals(str)) || (str3 == null && str != null)) {
                    hashMap.put("af_latestchannel", str);
                }
                str = getCachedStore(context);
                if (str != null) {
                    hashMap.put("af_installstore", str.toLowerCase());
                }
                str = getPreInstallName(context);
                if (str != null) {
                    hashMap.put("af_preinstall_name", str.toLowerCase());
                }
                str = getCurrentStore(context);
                if (str != null) {
                    hashMap.put("af_currentstore", str.toLowerCase());
                }
                if (appsFlyerKey == null || appsFlyerKey.length() < 0) {
                    str = getProperty(AppsFlyerProperties.AF_KEY);
                    if (str == null || str.length() < 0) {
                        a.afLog("AppsFlyer dev key is missing!!! Please use  AppsFlyerLib.getInstance().setAppsFlyerKey(...) to set it. ");
                        monitor(context, LOG_TAG, "DEV_KEY_MISSING", null);
                        a.afLog("AppsFlyer will not track this event.");
                        return;
                    }
                    hashMap.put("appsflyerKey", str);
                } else {
                    hashMap.put("appsflyerKey", appsFlyerKey);
                }
                str = getAppUserId();
                if (str != null) {
                    hashMap.put("appUserId", str);
                }
                str = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.USER_EMAILS);
                if (str != null) {
                    hashMap.put("user_emails", str);
                } else {
                    str = getProperty(AppsFlyerProperties.USER_EMAIL);
                    if (str != null) {
                        hashMap.put("sha1_el", f.toSHA1(str));
                    }
                }
                if (eventName != null) {
                    hashMap.put("eventName", eventName);
                    if (eventValue != null) {
                        hashMap.put("eventValue", eventValue);
                    }
                }
                if (getProperty(AppsFlyerProperties.APP_ID) != null) {
                    hashMap.put(AppsFlyerProperties.APP_ID, getProperty(AppsFlyerProperties.APP_ID));
                }
                str = getProperty(AppsFlyerProperties.CURRENCY_CODE);
                if (str != null) {
                    if (str.length() != 3) {
                        a.afWarnLog("WARNING: currency code should be 3 characters!!! '" + str + "' is not a legal value.");
                    }
                    hashMap.put("currency", str);
                }
                str = getProperty(AppsFlyerProperties.IS_UPDATE);
                if (str != null) {
                    hashMap.put("isUpdate", str);
                }
                hashMap.put("af_preinstalled", Boolean.toString(isPreInstalledApp(context)));
                if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_FACEBOOK_ATTR_ID, true)) {
                    str = getAttributionId(context.getContentResolver());
                    if (str != null) {
                        hashMap.put("fb", str);
                    }
                }
                addDeviceTracking(context, hashMap);
                str = g.id(new WeakReference(context));
                if (str != null) {
                    hashMap.put("uid", str);
                }
            } catch (Exception e3) {
                a.afLog("ERROR: " + "ERROR: " + "could not get uid " + e3.getMessage());
            } catch (Throwable th) {
                a.afLogE(th.getLocalizedMessage(), th);
                return;
            }
            try {
                hashMap.put("lang", Locale.getDefault().getDisplayLanguage());
            } catch (Exception e4) {
            }
            try {
                hashMap.put("lang_code", Locale.getDefault().getLanguage());
            } catch (Exception e5) {
            }
            try {
                hashMap.put("country", Locale.getDefault().getCountry());
            } catch (Exception e6) {
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                hashMap.put("operator", telephonyManager.getSimOperatorName());
                hashMap.put("carrier", telephonyManager.getNetworkOperatorName());
            } catch (Exception e7) {
            }
            try {
                hashMap.put("network", getNetwork(context));
            } catch (Throwable th2) {
                a.afLog("checking network error " + th2.getMessage());
            }
            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_FINGER_PRINT, true)) {
                str = getUniquePsuedoID();
                if (str != null) {
                    hashMap.put("deviceFingerPrintId", str);
                }
            }
            checkPlatform(context, hashMap);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssZ", Locale.US);
            if (VERSION.SDK_INT >= 9) {
                try {
                    hashMap.put("installDate", simpleDateFormat.format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime)));
                } catch (Exception e8) {
                }
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (packageInfo.versionCode > sharedPreferences.getInt("versionCode", 0)) {
                    saveIntegerToSharedPreferences(context, "appsflyerConversionDataRequestRetries", 0);
                    saveIntegerToSharedPreferences(context, "versionCode", packageInfo.versionCode);
                }
                hashMap.put("app_version_code", Integer.toString(packageInfo.versionCode));
                hashMap.put("app_version_name", packageInfo.versionName);
                if (VERSION.SDK_INT >= 9) {
                    long j = packageInfo.firstInstallTime;
                    long j2 = packageInfo.lastUpdateTime;
                    hashMap.put("date1", simpleDateFormat.format(new Date(j)));
                    hashMap.put("date2", simpleDateFormat.format(new Date(j2)));
                    hashMap.put("firstLaunchDate", getFirstInstallDate(simpleDateFormat, context));
                }
            } catch (NameNotFoundException e9) {
            } catch (NoSuchFieldError e10) {
            }
            if (referrer.length() > 0) {
                hashMap.put("referrer", referrer);
            }
            str = sharedPreferences.getString("attributionId", null);
            if (str != null && str.length() > 0) {
                hashMap.put("installAttribution", str);
            }
            str = sharedPreferences.getString("extraReferrers", null);
            if (str != null) {
                hashMap.put("extraReferrers", str);
            }
            str = AppsFlyerProperties.getInstance().getString("gcmInstanceId");
            if (str != null) {
                hashMap.put("af_google_instance_id", str);
            }
            if (z) {
                if (this.pushPayload != null) {
                    JSONObject jSONObject = new JSONObject(this.pushPayload);
                    jSONObject.put("isPush", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                    hashMap.put("af_deeplink", jSONObject.toString());
                }
                this.pushPayload = null;
            }
            if (z && (context instanceof Activity)) {
                Uri deepLinkUri = getDeepLinkUri(context);
                if (deepLinkUri != null) {
                    handleDeepLinkCallback(context, hashMap, deepLinkUri);
                } else if (this.latestDeepLink != null) {
                    handleDeepLinkCallback(context, hashMap, this.latestDeepLink);
                }
            }
            if (this.isRetargetingTestMode) {
                hashMap.put("testAppMode_retargeting", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                broadcastBacktoTestApp(context, (HashMap) hashMap);
                a.afLog("Sent retargeting params to test app");
            }
            if (isInTestMode(referrer)) {
                hashMap.put("testAppMode", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                broadcastBacktoTestApp(context, (HashMap) hashMap);
                a.afLog("Sent params to test app");
                endTestMode();
            }
            if (getProperty("advertiserId") == null) {
                addAdvertiserIDData(context, hashMap);
                if (getProperty("advertiserId") != null) {
                    hashMap.put("GAID_retry", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                } else {
                    hashMap.put("GAID_retry", "false");
                }
            }
            a.afLog("AppsFlyerLib.sendTrackingWithEvent");
            new f(append.toString(), hashMap, context.getApplicationContext(), z).run();
        }
    }

    private String getPushPayloadFromIntent(Context context) {
        if (context instanceof Activity) {
            Intent intent = ((Activity) context).getIntent();
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String string = extras.getString("af");
                    if (string == null) {
                        return string;
                    }
                    a.afLog("Push Notification received af payload = " + string);
                    extras.remove("af");
                    ((Activity) context).setIntent(intent.putExtras(extras));
                    return string;
                }
            }
        }
        return null;
    }

    private Uri getDeepLinkUri(Context context) {
        Intent intent = ((Activity) context).getIntent();
        if (intent == null || !"android.intent.action.VIEW".equals(intent.getAction())) {
            return null;
        }
        return intent.getData();
    }

    private void handleDeepLinkCallback(Context context, Map<String, String> params, Uri uri) {
        Map referrerStringToMap;
        params.put("af_deeplink", uri.toString());
        if (uri.getQueryParameter("af_deeplink") != null) {
            String queryParameter = uri.getQueryParameter("media_source");
            String queryParameter2 = uri.getQueryParameter("is_retargeting");
            if (queryParameter != null && queryParameter2 != null && queryParameter.equals("AppsFlyer_Test") && queryParameter2.equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                this.isRetargetingTestMode = true;
            }
            referrerStringToMap = referrerStringToMap(context, uri.getQuery().toString());
            if (uri.getPath() != null) {
                referrerStringToMap.put("path", uri.getPath());
            }
            if (uri.getScheme() != null) {
                referrerStringToMap.put("scheme", uri.getScheme());
            }
            if (uri.getHost() != null) {
                referrerStringToMap.put("host", uri.getHost());
            }
        } else {
            referrerStringToMap = new HashMap();
            referrerStringToMap.put("link", uri.toString());
        }
        saveDataToSharedPreferences(context, "deeplinkAttribution", new JSONObject(referrerStringToMap).toString());
        if (conversionDataListener != null) {
            conversionDataListener.onAppOpenAttribution(referrerStringToMap);
        }
    }

    private String generateOtherSDKsString() {
        return numricBooleanIsClassExist("com.tune.Tune") + numricBooleanIsClassExist("com.adjust.sdk.Adjust") + numricBooleanIsClassExist("com.kochava.android.tracker.Feature") + numricBooleanIsClassExist("io.branch.referral.Branch") + numricBooleanIsClassExist("com.apsalar.sdk.Apsalar") + numricBooleanIsClassExist("com.localytics.android.Localytics") + numricBooleanIsClassExist("com.tenjin.android.TenjinSDK") + numricBooleanIsClassExist("com.talkingdata.sdk.TalkingDataSDK") + numricBooleanIsClassExist("it.partytrack.sdk.Track") + numricBooleanIsClassExist("jp.appAdForce.android.LtvManager");
    }

    private int numricBooleanIsClassExist(String className) {
        try {
            Class.forName(className);
            return 1;
        } catch (Throwable th) {
            return 0;
        }
    }

    private void lastEventsProcessing(Context context, Map<String, String> params, String newEventName, String newEventValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        Editor edit = sharedPreferences.edit();
        try {
            String string = sharedPreferences.getString("prev_event_name", null);
            if (string != null) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("prev_event_timestamp", sharedPreferences.getLong("prev_event_timestamp", -1) + "");
                jSONObject.put("prev_event_value", sharedPreferences.getString("prev_event_value", null));
                jSONObject.put("prev_event_name", string);
                params.put("prev_event", jSONObject.toString());
            }
            edit.putString("prev_event_name", newEventName);
            edit.putString("prev_event_value", newEventValue);
            edit.putLong("prev_event_timestamp", System.currentTimeMillis());
            editorCommit(edit);
        } catch (Throwable e) {
            a.afLogE("Error while processing previous event.", e);
        }
    }

    private boolean isGooglePlayServicesAvailable(Context context) {
        try {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == 0) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            a.afLog("WARNING: Google play services is unavailable.");
            return false;
        }
    }

    private void addDeviceTracking(Context context, Map<String, String> params) {
        String str = null;
        if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, false)) {
            params.put(AppsFlyerProperties.DEVICE_TRACKING_DISABLED, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            return;
        }
        String str2;
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        boolean z = AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_IMEI, true);
        String string = sharedPreferences.getString("imeiCached", null);
        if (!z) {
            if (userCustomImei != null) {
                str2 = userCustomImei;
            }
            str2 = null;
        } else if (isIdCollectionAllowed(context)) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                str2 = (String) telephonyManager.getClass().getMethod("getDeviceId", new Class[0]).invoke(telephonyManager, new Object[0]);
                if (str2 == null) {
                    if (userCustomImei != null) {
                        str2 = userCustomImei;
                    } else {
                        if (string != null) {
                            str2 = string;
                        }
                        str2 = null;
                    }
                }
            } catch (Exception e) {
                a.afLog("WARNING: READ_PHONE_STATE is missing");
                str2 = null;
            }
        } else {
            if (userCustomImei != null) {
                str2 = userCustomImei;
            }
            str2 = null;
        }
        if (str2 != null) {
            saveDataToSharedPreferences(context, "imeiCached", str2);
            params.put("imei", str2);
        } else {
            a.afLog("IMEI was not collected.");
        }
        z = AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.COLLECT_ANDROID_ID, true);
        string = sharedPreferences.getString("androidIdCached", null);
        if (z) {
            if (isIdCollectionAllowed(context)) {
                try {
                    str2 = Secure.getString(context.getContentResolver(), "android_id");
                    if (str2 != null) {
                        str = str2;
                    } else if (userCustomAndroidId != null) {
                        str = userCustomAndroidId;
                    } else if (string != null) {
                        str = string;
                    }
                } catch (Exception e2) {
                }
            } else if (userCustomAndroidId != null) {
                str = userCustomAndroidId;
            }
        } else if (userCustomAndroidId != null) {
            str = userCustomAndroidId;
        }
        if (str != null) {
            saveDataToSharedPreferences(context, "androidIdCached", str);
            params.put("android_id", str);
            return;
        }
        a.afLog("Android ID was not collected.");
    }

    private boolean isIdCollectionAllowed(Context context) {
        return VERSION.SDK_INT < 19 || !isGooglePlayServicesAvailable(context);
    }

    private boolean isAppsFlyerFirstLaunch(Context context) {
        if (context.getSharedPreferences("appsflyer-data", 0).contains("appsFlyerCount")) {
            return false;
        }
        return true;
    }

    private String getCachedStore(Context context) {
        String str = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        if (sharedPreferences.contains("INSTALL_STORE")) {
            return sharedPreferences.getString("INSTALL_STORE", null);
        }
        if (isAppsFlyerFirstLaunch(context)) {
            str = getCurrentStore(context);
        }
        saveDataToSharedPreferences(context, "INSTALL_STORE", str);
        return str;
    }

    private String getCurrentStore(Context context) {
        return getManifestMetaData(new WeakReference(context), "AF_STORE");
    }

    public String getSystemProperty(String key) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{key});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getManifestMetaData(WeakReference<Context> context, String key) {
        if (context.get() == null) {
            return null;
        }
        return getManifestMetaData(key, ((Context) context.get()).getPackageManager(), ((Context) context.get()).getPackageName());
    }

    private String getManifestMetaData(String key, PackageManager packageManager, String packageName) {
        String str = null;
        try {
            Bundle bundle = packageManager.getApplicationInfo(packageName, 128).metaData;
            if (bundle != null) {
                Object obj = bundle.get(key);
                if (obj != null) {
                    str = obj.toString();
                }
            }
        } catch (Throwable th) {
            a.afLogE("Could not find " + key + " value in the manifest", th);
        }
        return str;
    }

    private String preInstallValueFromFile(Context context) {
        FileReader fileReader;
        Throwable th;
        String str = null;
        String systemProperty = getSystemProperty(PRE_INSTALL_SYSTEM_RO_PROP);
        if (systemProperty == null) {
            systemProperty = getManifestMetaData(new WeakReference(context), "AF_PRE_INSTALL_PATH");
        }
        if (systemProperty == null) {
            systemProperty = PRE_INSTALL_SYSTEM_DEFAULT;
        }
        try {
            String str2;
            if (new File(systemProperty).exists()) {
                str2 = systemProperty;
            } else {
                str2 = PRE_INSTALL_SYSTEM_DEFAULT_ETC;
            }
            Properties properties = new Properties();
            fileReader = new FileReader(str2);
            try {
                properties.load(fileReader);
                a.afLog("Found pre_install definition");
                str = properties.getProperty(context.getPackageName());
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable th2) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable th4) {
                    }
                }
                throw th;
            }
        } catch (Throwable th5) {
            Throwable th6 = th5;
            fileReader = str;
            th = th6;
            if (fileReader != null) {
                fileReader.close();
            }
            throw th;
        }
        return str;
    }

    private String getPreInstallName(Context context) {
        String str = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        if (sharedPreferences.contains("preInstallName")) {
            return sharedPreferences.getString("preInstallName", null);
        }
        if (isAppsFlyerFirstLaunch(context)) {
            str = preInstallValueFromFile(context);
            if (str == null) {
                str = getManifestMetaData(new WeakReference(context), "AF_PRE_INSTALL_NAME");
            }
        }
        if (str == null) {
            return str;
        }
        saveDataToSharedPreferences(context, "preInstallName", str);
        return str;
    }

    private void checkCache(Context context) {
        if (!isDuringCheckCache && System.currentTimeMillis() - lastCacheCheck >= 15000 && cacheScheduler == null) {
            cacheScheduler = Executors.newSingleThreadScheduledExecutor();
            cacheScheduler.schedule(new c(context), 1, TimeUnit.SECONDS);
        }
    }

    private String getConfiguredChannel(WeakReference<Context> context) {
        String string = AppsFlyerProperties.getInstance().getString(AppsFlyerProperties.CHANNEL);
        if (string == null) {
            return getManifestMetaData(context, "CHANNEL");
        }
        return string;
    }

    public boolean isPreInstalledApp(Context context) {
        try {
            if ((context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags & 1) != 0) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            a.afLogE("Could not check if app is pre installed", e);
            return false;
        }
    }

    private String getCachedChannel(Context context, String currentChannel) throws NameNotFoundException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
        if (sharedPreferences.contains("CACHED_CHANNEL")) {
            return sharedPreferences.getString("CACHED_CHANNEL", null);
        }
        saveDataToSharedPreferences(context, "CACHED_CHANNEL", currentChannel);
        return currentChannel;
    }

    private String getFirstInstallDate(SimpleDateFormat dateFormat, Context context) {
        String string = context.getSharedPreferences("appsflyer-data", 0).getString("appsFlyerFirstInstall", null);
        if (string == null) {
            if (isAppsFlyerFirstLaunch(context)) {
                a.afDebugLog("AppsFlyer: first launch detected");
                string = dateFormat.format(new Date());
            } else {
                string = "";
            }
            saveDataToSharedPreferences(context, "appsFlyerFirstInstall", string);
        }
        a.afLog("AppsFlyer: first launch date: " + string);
        return string;
    }

    private void addAdvertiserIDData(Context context, Map<String, String> params) {
        String bool;
        String str;
        boolean z;
        Object obj;
        String str2;
        AdInfo advertisingIdInfo;
        Object obj2;
        Throwable th;
        Object obj3;
        Throwable th2;
        int i = null;
        boolean z2 = true;
        a.afLog("Trying to fetch GAID..");
        String id;
        try {
            Throwable th3;
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            Info advertisingIdInfo2 = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if (advertisingIdInfo2 != null) {
                id = advertisingIdInfo2.getId();
                try {
                    bool = Boolean.toString(!advertisingIdInfo2.isLimitAdTrackingEnabled() ? z2 : false);
                    if (id != null) {
                        try {
                            if (id.length() != 0) {
                                str = bool;
                                bool = id;
                            }
                        } catch (Throwable th4) {
                            i = th4;
                            z = z2;
                            obj = id;
                            Object obj4 = bool;
                            bool = i;
                            try {
                                i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
                            } catch (Throwable th5) {
                                bool = bool + "/" + th5.getClass().getSimpleName();
                                debugAction("GAID", "\tgot error: " + th5.getMessage(), context);
                                String string = AppsFlyerProperties.getInstance().getString("advertiserId");
                                str = AppsFlyerProperties.getInstance().getString("advertiserIdEnabled");
                                if (th5.getLocalizedMessage() != null) {
                                    a.afLog(th5.getLocalizedMessage());
                                } else {
                                    a.afLog(th5.toString());
                                }
                                debugAction("Could not fetch advertiser id: ", th5.getLocalizedMessage(), context);
                                str2 = bool;
                                bool = string;
                            }
                            bool = bool.getClass().getSimpleName();
                            a.afLog("WARNING: Google Play Services is missing.");
                            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.ENABLE_GPS_FALLBACK, z2)) {
                                advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                                if (advertisingIdInfo != null) {
                                    id = advertisingIdInfo.getId();
                                    if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                                        z2 = false;
                                    }
                                    str = Boolean.toString(z2);
                                    if (id != null || id.length() == 0) {
                                        str2 = "emptyOrNull (bypass)";
                                        bool = id;
                                    } else {
                                        str2 = bool;
                                        bool = id;
                                    }
                                } else {
                                    str2 = "gpsAdInfo-null (bypass)";
                                    obj2 = th;
                                    obj3 = th2;
                                }
                            } else {
                                str2 = bool;
                                obj2 = th;
                                obj3 = th2;
                            }
                            if (str2 != null) {
                                params.put("gaidError", i + ": " + str2);
                            }
                            if (bool != null) {
                                return;
                            }
                        }
                    }
                    th3 = "emptyOrNull";
                    str = bool;
                    bool = id;
                } catch (Throwable th6) {
                    bool = th6;
                    z = false;
                    obj = id;
                    th = null;
                    i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
                    bool = bool.getClass().getSimpleName();
                    a.afLog("WARNING: Google Play Services is missing.");
                    if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.ENABLE_GPS_FALLBACK, z2)) {
                        advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                        if (advertisingIdInfo != null) {
                            id = advertisingIdInfo.getId();
                            if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                                z2 = false;
                            }
                            str = Boolean.toString(z2);
                            if (id != null) {
                            }
                            str2 = "emptyOrNull (bypass)";
                            bool = id;
                        } else {
                            str2 = "gpsAdInfo-null (bypass)";
                            obj2 = th;
                            obj3 = th2;
                        }
                    } else {
                        str2 = bool;
                        obj2 = th;
                        obj3 = th2;
                    }
                    if (str2 != null) {
                        params.put("gaidError", i + ": " + str2);
                    }
                    if (bool != null) {
                        return;
                    }
                }
            }
            bool = null;
            str = null;
            Object obj5 = "gpsAdInfo-null";
            z2 = false;
            z = z2;
            str2 = th3;
            i = -1;
        } catch (Throwable th7) {
            bool = th7;
            z = false;
            th = null;
            th2 = null;
            i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
            bool = bool.getClass().getSimpleName();
            a.afLog("WARNING: Google Play Services is missing.");
            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.ENABLE_GPS_FALLBACK, z2)) {
                str2 = bool;
                obj2 = th;
                obj3 = th2;
            } else {
                advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                if (advertisingIdInfo != null) {
                    str2 = "gpsAdInfo-null (bypass)";
                    obj2 = th;
                    obj3 = th2;
                } else {
                    id = advertisingIdInfo.getId();
                    if (advertisingIdInfo.isLimitAdTrackingEnabled()) {
                        z2 = false;
                    }
                    str = Boolean.toString(z2);
                    if (id != null) {
                    }
                    str2 = "emptyOrNull (bypass)";
                    bool = id;
                }
            }
            if (str2 != null) {
                params.put("gaidError", i + ": " + str2);
            }
            if (bool != null) {
            }
            return;
        }
        if (str2 != null) {
            params.put("gaidError", i + ": " + str2);
        }
        if (bool != null && str != null) {
            params.put("advertiserId", bool);
            params.put("advertiserIdEnabled", str);
            AppsFlyerProperties.getInstance().set("advertiserId", bool);
            AppsFlyerProperties.getInstance().set("advertiserIdEnabled", str);
            params.put("isGaidWithGps", String.valueOf(z));
        }
    }

    private void checkPlatform(Context context, Map<String, String> params) {
        try {
            Class.forName("com.unity3d.player.UnityPlayer");
            params.put("platformextension", "android_unity");
        } catch (ClassNotFoundException e) {
            params.put("platformextension", "android_native");
        } catch (Exception e2) {
        }
    }

    public String getAttributionId(ContentResolver contentResolver) {
        String str = null;
        String[] strArr = new String[]{ATTRIBUTION_ID_COLUMN_NAME};
        Cursor query = contentResolver.query(Uri.parse(ATTRIBUTION_ID_CONTENT_URI), strArr, str, str, str);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    str = query.getString(query.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME));
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Exception e) {
                        }
                    }
                    return str;
                }
            } catch (Exception e2) {
                a.afWarnLog("Could not collect cursor attribution" + e2);
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
        if (query != null) {
            try {
                query.close();
            } catch (Exception e5) {
            }
        }
        return str;
    }

    private int getCounter(SharedPreferences sharedPreferences, String parameterName, boolean isIncrease) {
        int i = sharedPreferences.getInt(parameterName, 0);
        if (!isIncrease) {
            return i;
        }
        i++;
        Editor edit = sharedPreferences.edit();
        edit.putInt(parameterName, i);
        editorCommit(edit);
        return i;
    }

    private long getTimePassedSinceLastLaunch(Context context, boolean shouldSave) {
        long j = context.getSharedPreferences("appsflyer-data", 0).getLong("AppsFlyerTimePassedSincePrevLaunch", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (j > 0) {
            j = currentTimeMillis - j;
        } else {
            j = -1;
        }
        if (shouldSave) {
            saveLongToSharedPreferences(context, "AppsFlyerTimePassedSincePrevLaunch", currentTimeMillis);
        }
        return j / 1000;
    }

    public String getUniquePsuedoID() {
        String str = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        try {
            return new UUID((long) str.hashCode(), (long) Build.class.getField("SERIAL").get(null).toString().hashCode()).toString();
        } catch (Exception e) {
            return new UUID((long) str.hashCode(), (long) "serial".hashCode()).toString();
        }
    }

    private String getNetwork(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1) {
                return "WIFI";
            }
            if (activeNetworkInfo.getType() == 0) {
                return "MOBILE";
            }
        }
        return "unknown";
    }

    public String getAppsFlyerUID(Context context) {
        return g.id(new WeakReference(context));
    }

    private void sendRequestToServer(String urlString, String postDataString, String afDevKey, WeakReference<Context> ctxReference, String cacheKey, boolean shouldRequestConversion) throws IOException {
        URL url = new URL(urlString);
        a.afLog("url: " + url.toString());
        debugAction("call server.", "\n" + url.toString() + "\nPOST:" + postDataString, (Context) ctxReference.get());
        h.logMessageMaskKey("data: " + postDataString);
        monitor((Context) ctxReference.get(), LOG_TAG, "EVENT_DATA", postDataString);
        try {
            callServer(url, postDataString, afDevKey, ctxReference, cacheKey, shouldRequestConversion);
        } catch (IOException e) {
            if (AppsFlyerProperties.getInstance().getBoolean(AppsFlyerProperties.USE_HTTP_FALLBACK, false)) {
                debugAction("https failed: " + e.getLocalizedMessage(), "", (Context) ctxReference.get());
                callServer(new URL(urlString.replace("https:", "http:")), postDataString, afDevKey, ctxReference, cacheKey, shouldRequestConversion);
                return;
            }
            a.afLog("failed to send requeset to server. " + e.getLocalizedMessage());
            monitor((Context) ctxReference.get(), LOG_TAG, "ERROR", e.getLocalizedMessage());
            throw e;
        }
    }

    private void callServer(URL url, String postData, String appsFlyerDevKey, WeakReference<Context> ctxReference, String cacheKey, boolean shouldRequestConversion) throws IOException {
        OutputStreamWriter outputStreamWriter;
        Throwable th;
        Context context = (Context) ctxReference.get();
        HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Length", postData.getBytes().length + "");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setDoOutput(true);
                try {
                    outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    try {
                        outputStreamWriter.write(postData);
                        if (outputStreamWriter != null) {
                            outputStreamWriter.close();
                        }
                        int responseCode = httpURLConnection.getResponseCode();
                        a.afLogM("response code: " + responseCode);
                        monitor(context, LOG_TAG, "SERVER_RESPONSE_CODE", Integer.toString(responseCode));
                        debugAction("response from server. status=", Integer.toString(responseCode), context);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("appsflyer-data", 0);
                        if (responseCode == 200) {
                            if (this.latestDeepLink != null) {
                                this.latestDeepLink = null;
                            }
                            if (cacheKey != null) {
                                CacheManager.getInstance().deleteRequest(cacheKey, context);
                            }
                            if (ctxReference.get() != null && cacheKey == null) {
                                saveDataToSharedPreferences(context, "sentSuccessfully", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                                checkCache(context);
                            }
                        }
                        responseCode = sharedPreferences.getInt("appsflyerConversionDataRequestRetries", 0);
                        long j = sharedPreferences.getLong("appsflyerConversionDataCacheExpiration", 0);
                        if (j != 0 && System.currentTimeMillis() - j > 5184000000L) {
                            saveDataToSharedPreferences(context, "attributionId", null);
                            saveLongToSharedPreferences(context, "appsflyerConversionDataCacheExpiration", 0);
                        }
                        if (sharedPreferences.getString("attributionId", null) == null && appsFlyerDevKey != null && shouldRequestConversion && conversionDataListener != null && responseCode <= 5) {
                            ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
                            newSingleThreadScheduledExecutor.schedule(new e(context.getApplicationContext(), appsFlyerDevKey, newSingleThreadScheduledExecutor), 10, TimeUnit.MILLISECONDS);
                        } else if (appsFlyerDevKey == null) {
                            a.afWarnLog("AppsFlyer dev key is missing.");
                        } else if (shouldRequestConversion && conversionDataListener != null && sharedPreferences.getString("attributionId", null) != null && getCounter(sharedPreferences, "appsFlyerCount", false) > 1) {
                            try {
                                Map conversionData = getConversionData(context);
                                if (conversionData != null) {
                                    conversionDataListener.onInstallConversionDataLoaded(conversionData);
                                }
                            } catch (b e) {
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (outputStreamWriter != null) {
                            outputStreamWriter.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStreamWriter = null;
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            httpURLConnection = null;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    public void validateAndTrackInAppPurchase(Context context, String publicKey, String signature, String purchaseData, String price, String currency, HashMap<String, String> additionalParameters) {
        a.afLog("Validate in app called with parameters: " + purchaseData + " " + price + " " + currency);
        if (publicKey != null && price != null && signature != null && currency != null && purchaseData != null) {
            ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
            newSingleThreadScheduledExecutor.schedule(new a(context.getApplicationContext(), getProperty(AppsFlyerProperties.AF_KEY), publicKey, signature, purchaseData, price, currency, additionalParameters, newSingleThreadScheduledExecutor), 10, TimeUnit.MILLISECONDS);
        } else if (validatorListener != null) {
            validatorListener.onValidateInAppFailure("Please provide purchase parameters");
        }
    }

    public String mapToString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : params.keySet()) {
            String str2 = (String) params.get(str);
            str2 = str2 == null ? "" : URLEncoder.encode(str2, AsyncHttpResponseHandler.DEFAULT_CHARSET);
            if (stringBuilder.length() > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(str).append('=').append(str2);
        }
        return stringBuilder.toString();
    }

    public float getBatteryLevel(Context context) {
        float f = 1.0f;
        try {
            Intent registerReceiver = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = registerReceiver.getIntExtra("level", -1);
            int intExtra2 = registerReceiver.getIntExtra("scale", -1);
            if (intExtra == -1 || intExtra2 == -1) {
                return 50.0f;
            }
            return (((float) intExtra) / ((float) intExtra2)) * 100.0f;
        } catch (Throwable th) {
            return f;
        }
    }
}
