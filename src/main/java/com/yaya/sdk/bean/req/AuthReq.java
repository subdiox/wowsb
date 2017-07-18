package com.yaya.sdk.bean.req;

public class AuthReq {
    private String androidId;
    private String appId;
    private String appVersion;
    private String channelId;
    private Integer cityId = Integer.valueOf(0);
    private Integer doubleCard;
    private String idfa;
    private String imei;
    private String imsi;
    private String ip;
    private Integer isp;
    private String mac;
    private String manufacturer;
    private String mobileType;
    private Integer networkType;
    private String osType;
    private String osVersion;
    private String phoneNum;
    private Integer providerId = Integer.valueOf(0);
    private Integer provinceId = Integer.valueOf(0);
    private Integer requestType;
    private String sdkVersion;
    private String secondPhoNum;
    private String softOwner;
    private String uuid;

    public String toString() {
        StringBuilder sb = new StringBuilder("AuthReq{");
        sb.append("requestType=").append(this.requestType);
        sb.append(", uuid='").append(this.uuid).append('\'');
        sb.append(", osType=").append(this.osType);
        sb.append(", osVersion=").append(this.osVersion);
        sb.append(", manufacturer='").append(this.manufacturer).append('\'');
        sb.append(", mobileType='").append(this.mobileType).append('\'');
        sb.append(", mac='").append(this.mac).append('\'');
        sb.append(", appId='").append(this.appId).append('\'');
        sb.append(", appVersion='").append(this.appVersion).append('\'');
        sb.append(", sdkVersion='").append(this.sdkVersion).append('\'');
        sb.append(", channelId='").append(this.channelId).append('\'');
        sb.append(", softOwner='").append(this.softOwner).append('\'');
        sb.append(", doubleCard=").append(this.doubleCard);
        sb.append(", phoneNum='").append(this.phoneNum).append('\'');
        sb.append(", secondPhoNum='").append(this.secondPhoNum).append('\'');
        sb.append(", imei='").append(this.imei).append('\'');
        sb.append(", imsi='").append(this.imsi).append('\'');
        sb.append(", isp=").append(this.isp);
        sb.append(", androidId='").append(this.androidId).append('\'');
        sb.append(", idfa='").append(this.idfa).append('\'');
        sb.append(", ip='").append(this.ip).append('\'');
        sb.append(", networkType=").append(this.networkType);
        sb.append('}');
        return sb.toString();
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public Integer getRequestType() {
        return this.requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOsType() {
        return this.osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMobileType() {
        return this.mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSoftOwner() {
        return this.softOwner;
    }

    public void setSoftOwner(String softOwner) {
        this.softOwner = softOwner;
    }

    public Integer getDoubleCard() {
        return this.doubleCard;
    }

    public void setDoubleCard(Integer doubleCard) {
        this.doubleCard = doubleCard;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSecondPhoNum() {
        return this.secondPhoNum;
    }

    public void setSecondPhoNum(String secondPhoNum) {
        this.secondPhoNum = secondPhoNum;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public Integer getIsp() {
        return this.isp;
    }

    public void setIsp(Integer isp) {
        this.isp = isp;
    }

    public String getAndroidId() {
        return this.androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getIdfa() {
        return this.idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public Integer getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(Integer networkType) {
        this.networkType = networkType;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getProviderId() {
        return this.providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
