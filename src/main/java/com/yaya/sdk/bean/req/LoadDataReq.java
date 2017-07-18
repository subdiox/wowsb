package com.yaya.sdk.bean.req;

public class LoadDataReq {
    private String androidParam;
    private String requestId;
    private String userData;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserData() {
        return this.userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getAndroidParam() {
        return this.androidParam;
    }

    public void setAndroidParam(String androidParam) {
        this.androidParam = androidParam;
    }
}
