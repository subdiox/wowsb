package com.yaya.sdk.bean.resp;

public class CheckResp extends BaseResp {
    private Integer helloId;
    private String requestId;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getHelloId() {
        return this.helloId;
    }

    public void setHelloId(Integer helloid2) {
        this.helloId = helloid2;
    }
}
