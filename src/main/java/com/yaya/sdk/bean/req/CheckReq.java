package com.yaya.sdk.bean.req;

public class CheckReq {
    private String appId;
    private Integer osType;
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

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getOsType() {
        return this.osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
