package com.yaya.sdk.http;

import android.content.Context;
import android.os.SystemClock;
import com.facebook.internal.NativeProtocol;
import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import com.yaya.sdk.bean.req.ActionReq;
import com.yaya.sdk.bean.req.CheckReq;
import com.yaya.sdk.bean.req.ErrReportReq;
import com.yaya.sdk.bean.req.GetNextScpReq;
import com.yaya.sdk.bean.req.LoadDataReq;
import com.yaya.sdk.bean.resp.CheckResp;
import com.yaya.sdk.constants.Constants;
import com.yaya.sdk.listener.HttpListener;
import com.yaya.sdk.utils.EncrypUtil;
import com.yaya.sdk.utils.JsonUtils;
import com.yaya.sdk.utils.Mylog;
import com.yaya.sdk.utils.PhoneUtil;
import com.yaya.sdk.utils.SPUtils;
import com.yaya.sdk.utils.UUIDGenerator;
import org.apache.http.entity.StringEntity;

public class AsyncHttpClient {
    private static String BASE_URL = "http://hs.yunva.com:9735/";
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_SUCC = 0;
    private static final String TAG = AsyncHttpClient.class.getSimpleName();
    private static com.yaya.sdk.async.http.AsyncHttpClient client = new com.yaya.sdk.async.http.AsyncHttpClient();

    private static void pull2Push(Context context, String json, String url, final HttpListener listener) {
        client.setMaxConnections(40);
        client.setMaxRetriesAndTimeout(5, 30000);
        try {
            Mylog.d(TAG, "json1:" + json);
            Context context2 = context;
            String str = url;
            client.post(context2, str, new StringEntity(EncrypUtil.encryptString(json), "utf-8"), "application/json", new AsyncHttpResponseHandler() {
                public void onSuccess(int statusCode, String content) {
                    super.onSuccess(statusCode, content);
                    Mylog.d(AsyncHttpClient.TAG, "onSuccess");
                    try {
                        if (listener != null) {
                            listener.onSuccess(EncrypUtil.decryptString(content), statusCode);
                        }
                    } catch (Exception e) {
                    }
                }

                public void onFailure(int statusCode, Throwable error, String content) {
                    super.onFailure(statusCode, error, content);
                    Mylog.e(AsyncHttpClient.TAG, error.toString());
                    try {
                        if (listener != null) {
                            listener.onFailure(content, statusCode);
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mylog.d(TAG, "post to url=" + url);
    }

    public static void uploadUserLoadInfo(Context context, HttpListener listener) {
        long last_time = SPUtils.getLong(context, Constants.UPLOAD_INFO_TIME, Long.valueOf(0));
        long current_time = SystemClock.currentThreadTimeMillis();
        if (last_time == 0 || last_time - current_time >= 1800000) {
            try {
                SPUtils.putLong(context, Constants.UPLOAD_INFO_TIME, current_time);
                pull2Push(context, JsonUtils.loadInfo2Json(context).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + "auth", listener);
            } catch (Exception e) {
            }
        }
    }

    public static synchronized void checkIdFremServer(Context context, HttpListener listener) {
        synchronized (AsyncHttpClient.class) {
            CheckReq req = new CheckReq();
            req.setUuid(UUIDGenerator.getUUID(context));
            req.setAppId(Constants.APP_ID);
            req.setOsType(Integer.valueOf(1));
            req.setSdkVersion(Constants.VERSION_NAME);
            req.setVersion(PhoneUtil.getAppVersion(context));
            try {
                pull2Push(context, JsonUtils.checkQeq2Json(req).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + "checkId", listener);
            } catch (Exception e) {
                Mylog.e(TAG, e.getMessage());
            }
        }
    }

    public static synchronized void reportSucces(Context context, LoadDataReq req, HttpListener listener) {
        synchronized (AsyncHttpClient.class) {
            if (req != null) {
                try {
                    pull2Push(context, JsonUtils.reportSucces2Json(req).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", "").replace("userData=", "userData=" + req.getUserData()), BASE_URL + "loaddata", listener);
                } catch (Exception e) {
                }
            }
        }
    }

    public static void reportError(Context context, ErrReportReq req, HttpListener listener) {
        if (req != null) {
            try {
                pull2Push(context, JsonUtils.reportError2Json(req).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + "errReport", listener);
            } catch (Exception e) {
            }
        }
    }

    public static void pullNextFremServer(Context context, HttpListener listener, GetNextScpReq req) {
        try {
            pull2Push(context, JsonUtils.getNextScpReq2Json(req).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + "next", listener);
        } catch (Exception e) {
        }
    }

    public static void uploadAction(Context context, HttpListener listener) {
        ActionReq req = new ActionReq();
        req.setUuid(UUIDGenerator.getUUID(context));
        req.setAppId(Constants.APP_ID);
        req.setOsType(Integer.valueOf(1));
        req.setSdkVersion(Constants.VERSION_NAME);
        req.setVersion(PhoneUtil.getAppVersion(context));
        req.setActionType(ActionReq.ACTION_TYPE_RC_SDK);
        try {
            pull2Push(context, JsonUtils.reportAction2Json(req).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + NativeProtocol.WEB_DIALOG_ACTION, listener);
        } catch (Exception e) {
        }
    }

    public static void pullCodeFremServer(Context context, CheckResp resp, HttpListener listener) {
        try {
            pull2Push(context, ("requestId=" + resp.getRequestId()).replace(":", "=").replace("{", "").replace("}", "").replace(",", "&&").replace("\"", ""), BASE_URL + "hello", listener);
        } catch (Exception e) {
        }
    }
}
