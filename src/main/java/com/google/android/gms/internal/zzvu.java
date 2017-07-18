package com.google.android.gms.internal;

import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.Status;

class zzvu implements ProxyResult {
    private Status zzair;
    private ProxyResponse zzajY;

    public zzvu(ProxyResponse proxyResponse) {
        this.zzajY = proxyResponse;
        this.zzair = Status.zzazx;
    }

    public zzvu(Status status) {
        this.zzair = status;
    }

    public ProxyResponse getResponse() {
        return this.zzajY;
    }

    public Status getStatus() {
        return this.zzair;
    }
}
