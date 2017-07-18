package com.yaya.sdk.listener;

public interface HttpListener {
    void onFailure(String str, int i);

    void onSuccess(String str, int i);
}
