package com.customsdk.wargaming;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import com.customsdk.wargaming.util.BaseGameUtils;
import com.customsdk.wargaming.util.IabHelper;
import com.customsdk.wargaming.util.IabHelper.OnConsumeFinishedListener;
import com.customsdk.wargaming.util.IabHelper.OnIabPurchaseFinishedListener;
import com.customsdk.wargaming.util.IabHelper.OnIabSetupFinishedListener;
import com.customsdk.wargaming.util.IabHelper.QueryInventoryFinishedListener;
import com.customsdk.wargaming.util.IabResult;
import com.customsdk.wargaming.util.Inventory;
import com.customsdk.wargaming.util.Purchase;
import com.customsdk.wargaming.util.SkuDetails;
import com.demo.yunva.MainActivity;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.unity3d.player.UnityPlayer;
import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WargamingMainActivity extends MainActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    static final int RC_REQUEST = 10001;
    private static int RC_SIGN_IN = 9001;
    private static final String TAG = "WARGAMING_SDK";
    private static final String UNITY_OBJECT_NAME = "ICustomSdk";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private List<Purchase> consumeList = new ArrayList();
    private String idToken;
    private boolean isSignInGoogle = false;
    private String mAndroidID = null;
    private boolean mAutoStartSignInflow = false;
    OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(WargamingMainActivity.TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (WargamingMainActivity.this.mHelper != null) {
                if (result.isSuccess()) {
                    Log.d(WargamingMainActivity.TAG, "queryPurchases consume count: " + WargamingMainActivity.this.queryPurchases.size());
                    WargamingMainActivity.this.queryPurchases.remove(purchase);
                    Log.d(WargamingMainActivity.TAG, "queryPurchases consume after count: " + WargamingMainActivity.this.queryPurchases.size());
                    if (WargamingMainActivity.this.consumeList.size() > 0) {
                        WargamingMainActivity.this.mHelper.consumeAsync((Purchase) WargamingMainActivity.this.consumeList.get(0), WargamingMainActivity.this.mConsumeFinishedListener);
                        WargamingMainActivity.this.consumeList.remove(0);
                        return;
                    }
                    return;
                }
                Log.d(WargamingMainActivity.TAG, "Error while consuming: " + result);
            }
        }
    };
    private Context mContext;
    private Map<String, SkuDetails> mFullSkuMap = new HashMap();
    private GoogleApiClient mGoogleApiClient = null;
    QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(WargamingMainActivity.TAG, "Query inventory finished.");
            if (WargamingMainActivity.this.mHelper != null) {
                if (result.isFailure()) {
                    Log.d(WargamingMainActivity.TAG, "Failed to query inventory: " + result);
                    return;
                }
                WargamingMainActivity.this.queryPurchases.clear();
                List<Purchase> purs = inventory.getAllPurchases();
                Log.d(WargamingMainActivity.TAG, "purchase size: " + purs.size());
                for (int i = 0; i < purs.size(); i++) {
                    WargamingMainActivity.this.queryPurchases.add((Purchase) purs.get(i));
                }
                Log.d(WargamingMainActivity.TAG, "query purchase size: " + WargamingMainActivity.this.queryPurchases.size());
                Log.d(WargamingMainActivity.TAG, "Query inventory was successful.");
            }
        }
    };
    IabHelper mHelper = null;
    QueryInventoryFinishedListener mOnGotSkuDetailsList = new QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(WargamingMainActivity.TAG, "Query sku details finished.");
            if (WargamingMainActivity.this.mHelper != null) {
                if (result.isFailure()) {
                    Log.d(WargamingMainActivity.TAG, "Failed to query sku details: " + result);
                    return;
                }
                WargamingMainActivity.this.mFullSkuMap = inventory.getAllSkus();
                Log.d(WargamingMainActivity.TAG, "query sku details successful: " + WargamingMainActivity.this.mFullSkuMap.size());
            }
        }
    };
    OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(WargamingMainActivity.TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (WargamingMainActivity.this.mHelper != null) {
                if (result.isFailure()) {
                    Log.d(WargamingMainActivity.TAG, "Error purchasing: " + result);
                } else if (WargamingMainActivity.this.verifyDeveloperPayload(purchase)) {
                    String purchase_data = new String(Base64.encode(purchase.getOriginalJson().getBytes(), 0));
                    Map<String, String> unitymsg = new HashMap();
                    unitymsg.put("token", purchase_data);
                    unitymsg.put("sign", purchase.getSignature());
                    unitymsg.put("sku", purchase.getSku());
                    unitymsg.put("orderId", purchase.getOrderId());
                    WargamingMainActivity.sendU3DMessage("OnPlatformSdk_GooglePayFinish", unitymsg);
                    WargamingMainActivity.this.queryPurchases.add(purchase);
                    Log.d(WargamingMainActivity.TAG, "Purchase successful.");
                } else {
                    Log.d(WargamingMainActivity.TAG, "Error purchasing. Authenticity verification failed.");
                }
            }
        }
    };
    private boolean mResolvingConnectionFailure = false;
    private List<Purchase> queryPurchases = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mAndroidID = Secure.getString(getContentResolver(), "android_id");
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi0NJiL2uHNhbHWnLE6H5z73lsBzz00un3YgcnYvA8U/Din93ohkSdia5SQGJNGapG9ULXDnvtpc3qDm73debqymdDFYUF2Aq55bI7B0Y4o9madeTaXlKTFbYgOVwqJ+V1ewIc90/jT4Ea+BsNfo4KYDEBBqtKR6fhIaCVhm8hAHJV3Kp/h0ueE9GkVHFtBNFIvigN9V/HYTpQgmRzC5X8CYJ74x6c1Js6dwQYMsvc/nz/jP1Z0u//xAY+vhTp0vqPEFvbeBpa2eh1BbvzsY9pP01mzaqf2K7qWeVPLbxP9udBUnd2zE0hjRCBIrASbdBSmhNQqi1H46dNa1OxNBKjwIDAQAB";
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == 0) {
            Log.d(TAG, "Creating IAB helper.");
            this.mHelper = new IabHelper(this, base64EncodedPublicKey);
            this.mHelper.enableDebugLogging(true);
            Log.d(TAG, "Starting setup.");
            this.mHelper.startSetup(new OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(WargamingMainActivity.TAG, "Setup finished.");
                    if (!result.isSuccess()) {
                        Log.e(WargamingMainActivity.TAG, "Problem setting up in-app billing: " + result);
                    } else if (WargamingMainActivity.this.mHelper != null) {
                        Log.d(WargamingMainActivity.TAG, "Setup successful. Querying inventory.");
                        WargamingMainActivity.this.mHelper.queryInventoryAsync(WargamingMainActivity.this.mGotInventoryListener);
                    }
                }
            });
        }
    }

    public void UpdateSkuDetails(String skuList) {
        if (skuList != null && !skuList.isEmpty()) {
            Log.d(TAG, "Querying sku details. " + skuList);
            List<String> l = new ArrayList();
            String[] skus = skuList.split("\\|");
            int i = 0;
            while (i < skus.length) {
                if (!(skus[i] == null || skus[i].isEmpty())) {
                    l.add(skus[i]);
                }
                i++;
            }
            this.mHelper.queryInventoryAsync(true, l, this.mOnGotSkuDetailsList);
        }
    }

    public String GetSkuCurrencyCode(String sku) {
        if (this.mFullSkuMap.containsKey(sku)) {
            String currencyCode = ((SkuDetails) this.mFullSkuMap.get(sku)).getCurrencyCode();
            if (!(currencyCode == null || currencyCode.isEmpty())) {
                return currencyCode;
            }
        }
        return "";
    }

    public String GetSkuCurrencyPrice(String sku) {
        if (this.mFullSkuMap.containsKey(sku)) {
            String price = ((SkuDetails) this.mFullSkuMap.get(sku)).getPrice();
            if (!(price == null || price.isEmpty())) {
                return price;
            }
        }
        return "";
    }

    public int GetSkuCurrencyPriceInMicors(String sku) {
        if (this.mFullSkuMap.containsKey(sku)) {
            return (int) (((SkuDetails) this.mFullSkuMap.get(sku)).getPriceInMicros() / 10000);
        }
        return 0;
    }

    public String GetAndroidAdvID() {
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(this.mContext).getId();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean IsGooglePlayAvailable() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == 0) {
            return true;
        }
        return false;
    }

    public void TryConnectGoolePlay() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.d(TAG, "try connect google play.");
        if (result == 0) {
            this.mGoogleApiClient = new Builder(this).addOnConnectionFailedListener(this).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("999178357348-bt1gro0m5cuih5mmt729vtvakedofgse.apps.googleusercontent.com").build()).build();
            Log.d(TAG, "api client successful.");
            ConnectGooglePlayAccount();
            return;
        }
        this.isSignInGoogle = false;
        this.mAutoStartSignInflow = false;
    }

    public void ConnectGooglePlayAccount() {
        this.mAutoStartSignInflow = true;
        Log.d(TAG, "start intent.");
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient), RC_SIGN_IN);
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Google play connection failed.");
        if (!this.mResolvingConnectionFailure && this.mAutoStartSignInflow) {
            this.mAutoStartSignInflow = false;
            this.mResolvingConnectionFailure = true;
            Log.d(TAG, "Google play auto signin failed.");
            if (!BaseGameUtils.resolveConnectionFailure(this, this.mGoogleApiClient, connectionResult, RC_SIGN_IN, "Google play connection failed")) {
                this.mResolvingConnectionFailure = false;
            }
        }
    }

    public void onConnected(@Nullable Bundle arg0) {
        Log.d(TAG, "Google play sign in success");
    }

    public void StartAutoSignIn() {
        Log.d(TAG, "Google play start auto sign in");
        if (!this.mAutoStartSignInflow && !this.isSignInGoogle && this.mGoogleApiClient != null) {
            Log.d(TAG, "Google play start auto sign in, connect");
            this.mGoogleApiClient.connect();
        }
    }

    public String GetLoginToken() {
        if (!this.isSignInGoogle || this.idToken == null) {
            return null;
        }
        return this.idToken;
    }

    public boolean IsSignInGoogle() {
        return this.isSignInGoogle;
    }

    public boolean IsGoogleAuthFinish() {
        return !this.mAutoStartSignInflow;
    }

    public void onConnectionSuspended(int arg0) {
        StartAutoSignIn();
    }

    public void googlePay(String productID) {
        Log.d(TAG, "GooglePay " + productID);
        String str = productID;
        this.mHelper.launchPurchaseFlow(this, str, RC_REQUEST, this.mPurchaseFinishedListener, "");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode=" + resultCode + ", intent=" + data);
            this.mAutoStartSignInflow = false;
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, " result " + result.getStatus());
            if (result.isSuccess()) {
                Log.d(TAG, "sign in success");
                this.idToken = result.getSignInAccount().getIdToken();
                this.isSignInGoogle = true;
            } else {
                Log.d(TAG, "sign in fail");
                this.mResolvingConnectionFailure = true;
                this.idToken = null;
                this.isSignInGoogle = false;
            }
            sendU3DMessage("GooglePlayLoginOver", null);
        } else if (this.mHelper == null) {
        } else {
            if (this.mHelper.handleActivityResult(requestCode, resultCode, data)) {
                Log.d(TAG, "onActivityResult handled by IABUtil.");
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    void ConsumePurchase(String signature) {
        Log.d(TAG, "try consume queryPurchases: " + this.queryPurchases.size());
        Log.d(TAG, "try consume signature: " + signature);
        for (int i = 0; i < this.queryPurchases.size(); i++) {
            Purchase purchase = (Purchase) this.queryPurchases.get(i);
            Log.d(TAG, "exist purchase token: " + purchase.getToken());
            Log.d(TAG, "exist purchase sig: " + purchase.getSignature());
            if (purchase.getSignature().equals(signature)) {
                Log.d(TAG, "consume sig: " + signature);
                if (this.mHelper.getAsyncInProgress().booleanValue()) {
                    this.consumeList.add(purchase);
                } else {
                    this.mHelper.consumeAsync(purchase, this.mConsumeFinishedListener);
                }
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    public void ConsumeAllQuery() {
        Log.d(TAG, "consume query purchase size: " + this.queryPurchases.size());
        for (int i = 0; i < this.queryPurchases.size(); i++) {
            Purchase pur = (Purchase) this.queryPurchases.get(i);
            Log.d(TAG, "consume query purchase sku: " + pur.getSku());
            if (verifyDeveloperPayload(pur)) {
                String purchase_data = new String(Base64.encode(pur.getOriginalJson().getBytes(), 0));
                Log.d(TAG, "Consumption successful. Provisioning.");
                Map<String, String> unitymsg = new HashMap();
                pur.getOrderId();
                unitymsg.put("token", purchase_data);
                unitymsg.put("sign", pur.getSignature());
                unitymsg.put("sku", pur.getSku());
                Log.d(TAG, "token: " + purchase_data);
                Log.d(TAG, "sign: " + pur.getSignature());
                sendU3DMessage("OnPlatformSdk_GooglePayFinish", unitymsg);
            }
        }
    }

    public void Login() {
        Log.w(TAG, "LOGIN START! ");
    }

    public void Logout() {
    }

    public void CopyToClipboard(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                ((ClipboardManager) WargamingMainActivity.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("transid", str));
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
        Process.killProcess(0);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private static void sendU3DMessage(String methodName, Map<String, String> hashMap) {
        String param = "";
        if (hashMap != null) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (param.length() == 0) {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("%s{%s}", new Object[]{key, val})).toString();
                } else {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("&%s{%s}", new Object[]{key, val})).toString();
                }
            }
        }
        UnityPlayer.UnitySendMessage(UNITY_OBJECT_NAME, methodName, param);
    }

    public String GetAndroidID() {
        if (this.mAndroidID != null) {
            return this.mAndroidID;
        }
        return "";
    }

    public String GetModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetVersion() {
        try {
            return VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetImei() {
        try {
            return ((TelephonyManager) getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String recupAdresseMAC() {
        try {
            WifiManager wifiMan = (WifiManager) getSystemService("wifi");
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            if (!wifiInf.getMacAddress().equals(marshmallowMacAddress)) {
                return wifiInf.getMacAddress();
            }
            try {
                String ret = getAdressMacByInterface();
                if (ret != null) {
                    return ret;
                }
                return getAddressMacByFile(wifiMan);
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC1");
                return marshmallowMacAddress;
            } catch (Exception e2) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC2 ");
                return marshmallowMacAddress;
            }
        } catch (Exception e3) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC 3");
            return marshmallowMacAddress;
        }
    }

    private static String getAdressMacByInterface() {
        try {
            for (NetworkInterface nif : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    int length = macBytes.length;
                    for (int i = 0; i < length; i++) {
                        res1.append(String.format("%02X:", new Object[]{Byte.valueOf(macBytes[i])}));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC4 ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        boolean enabled = true;
        int wifiState = wifiMan.getWifiState();
        wifiMan.setWifiEnabled(true);
        FileInputStream fin = new FileInputStream(new File(fileAddressMac));
        String ret = crunchifyGetStringFromStream(fin);
        fin.close();
        if (3 != wifiState) {
            enabled = false;
        }
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream == null) {
            return "No Contents";
        }
        Writer crunchifyWriter = new StringWriter();
        char[] crunchifyBuffer = new char[2048];
        try {
            Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, AsyncHttpResponseHandler.DEFAULT_CHARSET));
            while (true) {
                int counter = crunchifyReader.read(crunchifyBuffer);
                if (counter == -1) {
                    break;
                }
                crunchifyWriter.write(crunchifyBuffer, 0, counter);
            }
            return crunchifyWriter.toString();
        } finally {
            crunchifyStream.close();
        }
    }

    public String GetAndroidMac() {
        try {
            return recupAdresseMAC();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetLocalIp() {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public String GetCarrier() {
        try {
            return ((TelephonyManager) getSystemService("phone")).getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String GetNetworkType() {
        String strNetworkType = "";
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                return strNetworkType;
            }
            if (networkInfo.getType() == 1) {
                return "WIFI";
            }
            if (networkInfo.getType() != 0) {
                return strNetworkType;
            }
            String _strSubTypeName = networkInfo.getSubtypeName();
            Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
            switch (networkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return "2G";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3G";
                case 13:
                    return "4G";
                default:
                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                        return "3G";
                    }
                    return _strSubTypeName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return strNetworkType;
        }
    }
}
