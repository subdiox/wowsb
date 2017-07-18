package com.yaya.sdk.bean.resp;

import com.yaya.sdk.bean.req.HelloInfo;

public class GetHelloResp extends BaseResp {
    private HelloInfo info;
    private String requestId;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public HelloInfo getInfo() {
        return this.info;
    }

    public void setInfo(HelloInfo info) {
        this.info = info;
    }
}
