package com.yaya.sdk.bean.resp;

import com.yaya.sdk.bean.req.HelloInfo;

public class GetNextScpResp extends BaseResp {
    private HelloInfo info;
    private String requestId;

    public String getRequestId() {
        return this.requestId;
    }

    public HelloInfo getInfo() {
        return this.info;
    }
}
