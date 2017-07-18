package com.appsflyer;

class e {
    private String instanceId;
    private String token;
    private long tokenTimestamp;

    public e(long j, String str, String str2) {
        this.tokenTimestamp = j;
        this.token = str;
        this.instanceId = str2;
    }

    public e(String str, String str2, String str3) {
        if (str == null) {
            this.tokenTimestamp = 0;
        } else {
            this.tokenTimestamp = Long.valueOf(str).longValue();
        }
        this.token = str2;
        this.instanceId = str3;
    }

    public boolean update(e token) {
        return update(token.getTokenTimestamp(), token.getToken(), token.getInstanceId());
    }

    public boolean update(long tokenTimestamp, String token, String instanceId) {
        if (token.equals(this.token) || tokenTimestamp - this.tokenTimestamp <= 2000) {
            return false;
        }
        this.tokenTimestamp = tokenTimestamp;
        this.token = token;
        this.instanceId = instanceId;
        return true;
    }

    public String toString() {
        return this.tokenTimestamp + "," + this.token + "," + this.instanceId;
    }

    public long getTokenTimestamp() {
        return this.tokenTimestamp;
    }

    public String getToken() {
        return this.token;
    }

    public String getInstanceId() {
        return this.instanceId;
    }
}
