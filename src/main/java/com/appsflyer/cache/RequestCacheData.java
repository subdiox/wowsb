package com.appsflyer.cache;

import java.util.Scanner;

public class RequestCacheData {
    private String cacheKey;
    private String postData;
    private String requestURL;
    private String version;

    public RequestCacheData(String urlString, String postData, String sdkBuildNumber) {
        this.requestURL = urlString;
        this.postData = postData;
        this.version = sdkBuildNumber;
    }

    public RequestCacheData(char[] chars) {
        Scanner scanner = new Scanner(new String(chars));
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (nextLine.startsWith("url=")) {
                this.requestURL = nextLine.substring("url=".length()).trim();
            } else if (nextLine.startsWith("version=")) {
                this.version = nextLine.substring("version=".length()).trim();
            } else if (nextLine.startsWith("data=")) {
                this.postData = nextLine.substring("data=".length()).trim();
            }
        }
        scanner.close();
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPostData() {
        return this.postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getRequestURL() {
        return this.requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }
}
