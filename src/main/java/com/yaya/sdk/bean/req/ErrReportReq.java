package com.yaya.sdk.bean.req;

public class ErrReportReq {
    private String androidParam;
    private String errMsg;
    private String requestId;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getAndroidParam() {
        return this.androidParam;
    }

    public void setAndroidParam(String androidParam) {
        this.androidParam = androidParam;
    }
}
