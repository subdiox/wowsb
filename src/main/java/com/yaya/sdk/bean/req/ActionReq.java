package com.yaya.sdk.bean.req;

public class ActionReq {
    public static final String ACTION_TYPE_RC_SDK = "RC-SDK";
    private String actionType;
    private String appId;
    private Integer osType = Integer.valueOf(1);
    private String sdkVersion;
    private String uuid;
    private String version;

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public Integer getOsType() {
        return this.osType;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getActionType() {
        return ACTION_TYPE_RC_SDK;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
