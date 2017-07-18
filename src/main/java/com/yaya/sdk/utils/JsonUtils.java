package com.yaya.sdk.utils;

import android.content.Context;
import android.os.Build.VERSION;
import com.yaya.sdk.bean.req.ActionReq;
import com.yaya.sdk.bean.req.AuthReq;
import com.yaya.sdk.bean.req.CheckReq;
import com.yaya.sdk.bean.req.ErrReportReq;
import com.yaya.sdk.bean.req.GetNextScpReq;
import com.yaya.sdk.bean.req.HelloInfo;
import com.yaya.sdk.bean.req.LoadDataReq;
import com.yaya.sdk.bean.resp.CheckResp;
import com.yaya.sdk.bean.resp.GetHelloResp;
import com.yaya.sdk.constants.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    private static String TAG = JSONObject.class.getSimpleName();

    public static String loadInfo2Json(AuthReq bean) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.AUTHREQ_RESQUEST_TYPE, bean.getRequestType());
            jsonObject.put(Constants.AUTHREQ_UUID, bean.getUuid());
            jsonObject.put(Constants.AUTHREQ_OSTYPE, bean.getOsType());
            jsonObject.put(Constants.AUTHREQ_MOBILE_TYPE, bean.getMobileType());
            jsonObject.put(Constants.AUTHREQ_MAC, bean.getMac());
            jsonObject.put(Constants.AUTHREQ_APP_ID, bean.getAppId());
            jsonObject.put(Constants.AUTHREQ_APP_VERSION, bean.getAppVersion());
            jsonObject.put(Constants.AUTHREQ_CHANNERL_ID, bean.getChannelId());
            jsonObject.put(Constants.AUTHREQ_SOFT_OWNER, bean.getSoftOwner());
            jsonObject.put(Constants.AUTHREQ_SDK_VERSION, bean.getSdkVersion());
            jsonObject.put(Constants.AUTHREQ_DOUBLECARD, bean.getDoubleCard());
            jsonObject.put(Constants.AUTHREQ_PHONENUM, bean.getPhoneNum());
            jsonObject.put(Constants.AUTHREQ_SECOND_PHONUM, bean.getSecondPhoNum());
            jsonObject.put(Constants.AUTHREQ_IMEI, bean.getImei());
            jsonObject.put(Constants.AUTHREQ_IMSI, bean.getImsi());
            jsonObject.put(Constants.AUTHREQ_ISP, bean.getIsp());
            jsonObject.put(Constants.AUTHREQ_ANDROID_ID, bean.getAndroidId());
            jsonObject.put(Constants.AUTHREQ_IDFA, bean.getIdfa());
            jsonObject.put(Constants.AUTHREQ_IP, bean.getIp());
            jsonObject.put(Constants.AUTHREQ_NETWORKTYPE, bean.getNetworkType());
            jsonObject.put(Constants.AUTHREQ_OSVERSION, bean.getOsVersion());
            json = jsonObject.toString();
        } catch (JSONException e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }

    public static String reportSucces2Json(LoadDataReq req) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestId", req.getRequestId());
            jsonObject.put(Constants.LOADDATAREQ_USERDATA, "");
            jsonObject.put(Constants.ERRREPORTREQ_PARMS, req.getAndroidParam());
            json = jsonObject.toString();
        } catch (Exception e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }

    public static String reportAction2Json(ActionReq req) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTIONREQ_ACTIONTYPE, req.getActionType());
            jsonObject.put(Constants.ACTIONREQ_APPID, req.getAppId());
            jsonObject.put(Constants.ACTIONREQ_OSTYPE, req.getOsType());
            jsonObject.put(Constants.ACTIONREQ_SDKVERSION, req.getSdkVersion());
            jsonObject.put(Constants.ACTIONREQ_UUID, req.getUuid());
            jsonObject.put(Constants.ACTIONREQ_VERSION, req.getVersion());
            json = jsonObject.toString();
        } catch (Exception e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }

    public static String loadInfo2Json(Context context) {
        AuthReq authReq = new AuthReq();
        authReq.setAndroidId(PhoneUtil.getAndroidId(context));
        authReq.setAppId(Constants.APP_ID);
        authReq.setAppVersion(PhoneUtil.getAppVersion(context));
        authReq.setChannelId("");
        authReq.setDoubleCard(Integer.valueOf(0));
        authReq.setIdfa("");
        authReq.setImei(PhoneUtil.getImei(context));
        authReq.setImsi(PhoneUtil.getImsi(context));
        authReq.setIp(PhoneUtil.getIp(context));
        authReq.setIsp(Integer.valueOf(PhoneUtil.getSimOperatorType(context)));
        authReq.setMac(PhoneUtil.getMac(context));
        authReq.setManufacturer(PhoneUtil.getManufacturer());
        authReq.setMobileType(PhoneUtil.getTelephonyModel());
        authReq.setNetworkType(Integer.valueOf(PhoneUtil.getNetWorkType(context)));
        authReq.setOsType(PhoneUtil.getOsType());
        authReq.setPhoneNum("");
        authReq.setOsVersion(VERSION.SDK_INT + "");
        int request_type = SPUtils.getInt(context, Constants.REGISTER_OR_LOAD, 1);
        authReq.setRequestType(Integer.valueOf(request_type));
        if (request_type == 1) {
            SPUtils.putInt(context, Constants.REGISTER_OR_LOAD, 2);
        }
        authReq.setSdkVersion("");
        authReq.setSecondPhoNum("");
        authReq.setSoftOwner("");
        authReq.setUuid(UUIDGenerator.getUUID(context));
        return loadInfo2Json(authReq);
    }

    public static String reportError2Json(ErrReportReq req) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ERRREPORTREQ_ERRMSG, req.getErrMsg());
            jsonObject.put("requestId", req.getRequestId());
            jsonObject.put(Constants.ERRREPORTREQ_PARMS, req.getAndroidParam());
            json = jsonObject.toString();
        } catch (Exception e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }

    public static String checkQeq2Json(CheckReq req) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHECKREQ_APPID, req.getAppId());
            jsonObject.put(Constants.CHECKREQ_OSTYPE, req.getOsType());
            jsonObject.put(Constants.CHECKREQ_SDKVERSION, req.getSdkVersion());
            jsonObject.put(Constants.CHECKREQ_UUID, req.getUuid());
            jsonObject.put(Constants.CHECKREQ_VERSION, req.getVersion());
            json = jsonObject.toString();
        } catch (Exception e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }

    public static CheckResp checkResp2Object(String respStr) {
        CheckResp resp = new CheckResp();
        try {
            JSONObject jsonObject = new JSONObject(respStr);
            String requestid = jsonObject.getString("requestId");
            Integer helloid = Integer.valueOf(jsonObject.getInt(Constants.CHECKRESP_HELLOID));
            resp.setRequestId(requestid);
            resp.setHelloId(helloid);
            return resp;
        } catch (JSONException e) {
            return null;
        }
    }

    public static GetHelloResp string2GetHelloResp(String content) {
        GetHelloResp getHelloResp = new GetHelloResp();
        HelloInfo helloInfo = null;
        try {
            JSONObject object = new JSONObject(content);
            String requestId = object.getString("requestId");
            if (!object.isNull(Constants.GETHELLORESP_INFO)) {
                HelloInfo helloInfo2 = new HelloInfo();
                try {
                    JSONObject infoObject = object.getJSONObject(Constants.GETHELLORESP_INFO);
                    if (!infoObject.isNull("id")) {
                        helloInfo2.setId(Integer.valueOf(infoObject.getInt("id")));
                    }
                    if (!infoObject.isNull(Constants.HELLOINFO_MD5VAL)) {
                        helloInfo2.setMd5Val(infoObject.getString(Constants.HELLOINFO_MD5VAL));
                    }
                    if (!infoObject.isNull("name")) {
                        helloInfo2.setName(infoObject.getString("name"));
                    }
                    if (!infoObject.isNull(Constants.HELLOINFO_OSTYPE)) {
                        helloInfo2.setOsType(Integer.valueOf(infoObject.getInt(Constants.HELLOINFO_OSTYPE)));
                    }
                    if (!infoObject.isNull(Constants.HELLOINFO_HELLO)) {
                        helloInfo2.setHello(infoObject.getString(Constants.HELLOINFO_HELLO));
                    }
                    helloInfo = helloInfo2;
                } catch (JSONException e) {
                    helloInfo = helloInfo2;
                }
            }
            getHelloResp.setInfo(helloInfo);
            getHelloResp.setRequestId(requestId);
        } catch (JSONException e2) {
        }
        return getHelloResp;
    }

    public static CheckResp string2CheckResp(String content) {
        CheckResp checkResp = new CheckResp();
        try {
            JSONObject object = new JSONObject(content);
            String requestId = object.getString("requestId");
            if (!object.isNull(Constants.CHECKRESP_HELLOID)) {
                checkResp.setHelloId(Integer.valueOf(object.getInt(Constants.CHECKRESP_HELLOID)));
            }
            checkResp.setRequestId(requestId);
        } catch (JSONException e) {
        }
        return checkResp;
    }

    public static String getNextScpReq2Json(GetNextScpReq req) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.GETNEXTSCPREQ_LASTID, req.getLastId());
            jsonObject.put(Constants.GETNEXTSCPREQ_NEXTID, req.getNextId());
            jsonObject.put(Constants.GETNEXTSCPREQ_REQUESTID, req.getRequestId());
            json = jsonObject.toString();
        } catch (Exception e) {
            Mylog.d(TAG, e.getMessage());
        }
        return json;
    }
}
