package com.yaya.sdk.async.http;

import java.util.concurrent.Future;

public class RequestHandle {
    private final Future<?> request;

    public RequestHandle(Future<?> request) {
        this.request = request;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.request != null && this.request.cancel(mayInterruptIfRunning);
    }

    public boolean isFinished() {
        return this.request == null || this.request.isDone();
    }

    public boolean isCancelled() {
        return this.request != null && this.request.isCancelled();
    }
}
