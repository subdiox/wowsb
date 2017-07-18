package com.yaya.sdk.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.yaya.sdk.constants.Constants;
import com.yaya.sdk.http.AsyncHttpClient;
import com.yaya.sdk.listener.HttpListener;
import com.yaya.sdk.utils.Mylog;

public class CoreLib {
    private static String TAG = CoreLib.class.getSimpleName();

    private static boolean isN() {
        if (VERSION.SDK_INT >= 24) {
            return false;
        }
        return true;
    }

    public static void init(Context context, String appId) {
        Mylog.d(TAG, "init appId = " + appId);
        if (context == null || TextUtils.isEmpty(appId)) {
            Mylog.d(TAG, "init failed");
            return;
        }
        Constants.APP_ID = appId;
        Constants.GLOABLE_CONTEXT = context;
        AsyncHttpClient.uploadUserLoadInfo(context, new HttpListener() {
            public void onSuccess(String content, int statusCode) {
                Mylog.i(CoreLib.TAG, "uploadUserLoadInfo-onSuccess:statusCode--" + statusCode + ",contet--" + content);
            }

            public void onFailure(String content, int statusCode) {
                Mylog.i(CoreLib.TAG, "uploadUserLoadInfo-onFailure:statusCode--" + statusCode + ",contet--" + content);
            }
        });
        AsyncHttpClient.uploadAction(context, new HttpListener() {
            public void onSuccess(String content, int statusCode) {
                Mylog.i(CoreLib.TAG, "uploadAction-onSuccess:statusCode--" + statusCode + ",contet--" + content);
            }

            public void onFailure(String content, int statusCode) {
                Mylog.i(CoreLib.TAG, "uploadAction-onFailure:statusCode--" + statusCode + ",contet--" + content);
            }
        });
        new CoreLibb().startCore(context);
    }
}
