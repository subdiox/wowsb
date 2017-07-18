package com.appsflyer;

import android.content.Context;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import java.util.Map;
import org.json.JSONObject;

public class AppsFlyerUnityHelper {
    private static String TAG = "AppsFlyerUnityHelper";

    public static void createConversionDataListener(Context context, String callbackObject) {
        final String callbackObjectName = callbackObject;
        AppsFlyerLib.getInstance().registerConversionListener(context, new AppsFlyerConversionListener() {
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                UnityPlayer.UnitySendMessage(callbackObjectName, "didReceiveConversionData", new JSONObject(map).toString());
            }

            public void onInstallConversionFailure(String s) {
                UnityPlayer.UnitySendMessage(callbackObjectName, "didReceiveConversionDataWithError", s);
            }

            public void onAppOpenAttribution(Map<String, String> map) {
                UnityPlayer.UnitySendMessage(callbackObjectName, "onAppOpenAttribution", new JSONObject(map).toString());
            }

            public void onAttributionFailure(String s) {
                UnityPlayer.UnitySendMessage(callbackObjectName, "onAppOpenAttributionFailure", s);
            }
        });
    }

    public static void createValidateInAppListener(Context context, String callbackObject, String callbackMethod, String callbackFailedMethod) {
        final String callbackMethodName = callbackMethod;
        final String callbackMethodFailedName = callbackFailedMethod;
        final String callbackObjectName = callbackObject;
        AppsFlyerLib.getInstance().registerValidatorListener(context, new AppsFlyerInAppPurchaseValidatorListener() {
            public void onValidateInApp() {
                Log.i("AppsFlyerLibUnityhelper", "onValidateInApp called.");
                UnityPlayer.UnitySendMessage(callbackObjectName, callbackMethodName, "Validate success");
            }

            public void onValidateInAppFailure(String errorMessage) {
                Log.i("AppsFlyerLibUnityhelper", "onValidateInAppFailure called.");
                UnityPlayer.UnitySendMessage(callbackObjectName, callbackMethodFailedName, errorMessage);
            }
        });
    }
}
