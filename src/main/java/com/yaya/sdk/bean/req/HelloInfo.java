package com.yaya.sdk.bean.req;

public class HelloInfo {
    private String hello;
    private Integer id;
    private String md5Val;
    private String name;
    private Integer osType;

    public String getMd5Val() {
        return this.md5Val;
    }

    public void setMd5Val(String md5Val) {
        this.md5Val = md5Val;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOsType() {
        return this.osType;
    }

    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    public String getHello() {
        return this.hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
