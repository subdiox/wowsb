package com.yaya.sdk.bean.resp;

public abstract class BaseResp {
    private String msg;
    private Long result = Long.valueOf(0);

    public Long getResult() {
        return this.result;
    }

    public String getMsg() {
        return this.msg;
    }
}
