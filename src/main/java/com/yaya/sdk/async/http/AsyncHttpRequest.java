package com.yaya.sdk.async.http;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private final HttpUriRequest request;
    private final ResponseHandlerInterface responseHandler;

    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, ResponseHandlerInterface responseHandler) {
        this.client = client;
        this.context = context;
        this.request = request;
        this.responseHandler = responseHandler;
    }

    public void run() {
        if (this.responseHandler != null) {
            this.responseHandler.sendStartMessage();
        }
        try {
            makeRequestWithRetries();
        } catch (IOException e) {
            if (this.responseHandler != null) {
                this.responseHandler.sendFailureMessage(0, null, null, e);
            }
        }
        if (this.responseHandler != null) {
            this.responseHandler.sendFinishMessage();
        }
    }

    private void makeRequest() throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            if (this.request.getURI().getScheme() == null) {
                throw new MalformedURLException("No valid URI scheme was provided");
            }
            HttpResponse response = this.client.execute(this.request, this.context);
            if (!Thread.currentThread().isInterrupted() && this.responseHandler != null) {
                this.responseHandler.sendResponseMessage(response);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void makeRequestWithRetries() throws java.io.IOException {
        /*
        r7 = this;
        r3 = 1;
        r0 = 0;
        r5 = r7.client;
        r4 = r5.getHttpRequestRetryHandler();
        r1 = r0;
    L_0x0009:
        if (r3 == 0) goto L_0x0087;
    L_0x000b:
        r7.makeRequest();	 Catch:{ UnknownHostException -> 0x000f, NullPointerException -> 0x004e, IOException -> 0x0078 }
        return;
    L_0x000f:
        r2 = move-exception;
        r0 = new java.io.IOException;	 Catch:{ Exception -> 0x0089 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089 }
        r5.<init>();	 Catch:{ Exception -> 0x0089 }
        r6 = "UnknownHostException exception: ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089 }
        r6 = r2.getMessage();	 Catch:{ Exception -> 0x0089 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0089 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0089 }
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00ac }
        if (r5 <= 0) goto L_0x004c;
    L_0x0030:
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00ac }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00ac }
        r6 = r7.context;	 Catch:{ Exception -> 0x00ac }
        r5 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00ac }
        if (r5 == 0) goto L_0x004c;
    L_0x003e:
        r3 = 1;
    L_0x003f:
        if (r3 == 0) goto L_0x00ae;
    L_0x0041:
        r5 = r7.responseHandler;	 Catch:{ Exception -> 0x00ac }
        if (r5 == 0) goto L_0x00ae;
    L_0x0045:
        r5 = r7.responseHandler;	 Catch:{ Exception -> 0x00ac }
        r5.sendRetryMessage();	 Catch:{ Exception -> 0x00ac }
        r1 = r0;
        goto L_0x0009;
    L_0x004c:
        r3 = 0;
        goto L_0x003f;
    L_0x004e:
        r2 = move-exception;
        r0 = new java.io.IOException;	 Catch:{ Exception -> 0x0089 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089 }
        r5.<init>();	 Catch:{ Exception -> 0x0089 }
        r6 = "NPE in HttpClient: ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089 }
        r6 = r2.getMessage();	 Catch:{ Exception -> 0x0089 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0089 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0089 }
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00ac }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00ac }
        r6 = r7.context;	 Catch:{ Exception -> 0x00ac }
        r3 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00ac }
        goto L_0x003f;
    L_0x0078:
        r2 = move-exception;
        r0 = r2;
        r5 = r7.executionCount;	 Catch:{ Exception -> 0x00ac }
        r5 = r5 + 1;
        r7.executionCount = r5;	 Catch:{ Exception -> 0x00ac }
        r6 = r7.context;	 Catch:{ Exception -> 0x00ac }
        r3 = r4.retryRequest(r0, r5, r6);	 Catch:{ Exception -> 0x00ac }
        goto L_0x003f;
    L_0x0087:
        r0 = r1;
    L_0x0088:
        throw r0;
    L_0x0089:
        r2 = move-exception;
        r0 = r1;
    L_0x008b:
        r5 = com.yaya.sdk.async.http.AsyncHttpClient.isDebug;
        if (r5 == 0) goto L_0x008f;
    L_0x008f:
        r0 = new java.io.IOException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unhandled exception: ";
        r5 = r5.append(r6);
        r6 = r2.getMessage();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r0.<init>(r5);
        goto L_0x0088;
    L_0x00ac:
        r2 = move-exception;
        goto L_0x008b;
    L_0x00ae:
        r1 = r0;
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yaya.sdk.async.http.AsyncHttpRequest.makeRequestWithRetries():void");
    }
}
