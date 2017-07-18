package com.yaya.sdk.bean.req;

public class GetNextScpReq {
    private Integer lastId;
    private Integer nextId;
    private String requestId;

    public Integer getLastId() {
        return this.lastId;
    }

    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    public Integer getNextId() {
        return this.nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
