package com.yaya.sdk.async.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;

public class AsyncHttpResponseHandler implements ResponseHandlerInterface {
    protected static final int BUFFER_SIZE = 4096;
    public static final String DEFAULT_CHARSET = "UTF-8";
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int FINISH_MESSAGE = 3;
    private static final String LOG_TAG = "AsyncHttpResponseHandler";
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int START_MESSAGE = 2;
    protected static final int SUCCESS_MESSAGE = 0;
    private Handler handler;
    private Header[] requestHeaders = null;
    private URI requestURI = null;
    private String responseCharset = DEFAULT_CHARSET;
    private Boolean useSynchronousMode = Boolean.valueOf(false);

    static class ResponderHandler extends Handler {
        private final WeakReference<AsyncHttpResponseHandler> mResponder;

        ResponderHandler(AsyncHttpResponseHandler service) {
            this.mResponder = new WeakReference(service);
        }

        public void handleMessage(Message msg) {
            AsyncHttpResponseHandler service = (AsyncHttpResponseHandler) this.mResponder.get();
            if (service != null) {
                service.handleMessage(msg);
            }
        }
    }

    public URI getRequestURI() {
        return this.requestURI;
    }

    public Header[] getRequestHeaders() {
        return this.requestHeaders;
    }

    public void setRequestURI(URI requestURI) {
        this.requestURI = requestURI;
    }

    public void setRequestHeaders(Header[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public boolean getUseSynchronousMode() {
        return this.useSynchronousMode.booleanValue();
    }

    public void setUseSynchronousMode(boolean value) {
        this.useSynchronousMode = Boolean.valueOf(value);
    }

    public void setCharset(String charset) {
        this.responseCharset = charset;
    }

    public String getCharset() {
        return this.responseCharset == null ? DEFAULT_CHARSET : this.responseCharset;
    }

    public AsyncHttpResponseHandler() {
        if (Looper.myLooper() != null) {
            this.handler = new ResponderHandler(this);
        }
    }

    public void onProgress(int bytesWritten, int totalSize) {
    }

    public void onStart() {
    }

    public void onFinish() {
    }

    @Deprecated
    public void onSuccess(String content) {
    }

    @Deprecated
    public void onSuccess(int statusCode, Header[] headers, String content) {
        onSuccess(statusCode, content);
    }

    @Deprecated
    public void onSuccess(int statusCode, String content) {
        onSuccess(content);
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            onSuccess(statusCode, headers, responseBody == null ? null : new String(responseBody, getCharset()));
        } catch (Throwable e) {
            if (AsyncHttpClient.isDebug) {
                onFailure(statusCode, headers, e, null);
            } else {
                onFailure(statusCode, headers, e, null);
            }
        }
    }

    @Deprecated
    public void onFailure(Throwable error) {
    }

    @Deprecated
    public void onFailure(Throwable error, String content) {
        onFailure(error);
    }

    @Deprecated
    public void onFailure(int statusCode, Throwable error, String content) {
        onFailure(error, content);
    }

    @Deprecated
    public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
        onFailure(statusCode, error, content);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        try {
            onFailure(statusCode, headers, error, responseBody == null ? null : new String(responseBody, getCharset()));
        } catch (Throwable e) {
            if (AsyncHttpClient.isDebug) {
                onFailure(statusCode, headers, e, null);
            } else {
                onFailure(statusCode, headers, e, null);
            }
        }
    }

    public void onRetry() {
    }

    public final void sendProgressMessage(int bytesWritten, int bytesTotal) {
        sendMessage(obtainMessage(4, new Object[]{Integer.valueOf(bytesWritten), Integer.valueOf(bytesTotal)}));
    }

    public final void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {
        sendMessage(obtainMessage(0, new Object[]{Integer.valueOf(statusCode), headers, responseBody}));
    }

    public final void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        sendMessage(obtainMessage(1, new Object[]{Integer.valueOf(statusCode), headers, responseBody, error}));
    }

    public final void sendStartMessage() {
        sendMessage(obtainMessage(2, null));
    }

    public final void sendFinishMessage() {
        sendMessage(obtainMessage(3, null));
    }

    public final void sendRetryMessage() {
        sendMessage(obtainMessage(5, null));
    }

    protected void handleMessage(Message msg) {
        Object[] response;
        switch (msg.what) {
            case 0:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 3) {
                    onSuccess(((Integer) response[0]).intValue(), (Header[]) response[1], (byte[]) response[2]);
                    return;
                }
                return;
            case 1:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 4) {
                    onFailure(((Integer) response[0]).intValue(), (Header[]) response[1], (byte[]) response[2], (Throwable) response[3]);
                    return;
                }
                return;
            case 2:
                onStart();
                return;
            case 3:
                onFinish();
                return;
            case 4:
                response = (Object[]) msg.obj;
                if (response != null && response.length >= 2) {
                    try {
                        onProgress(((Integer) response[0]).intValue(), ((Integer) response[1]).intValue());
                        return;
                    } catch (Throwable th) {
                        if (!AsyncHttpClient.isDebug) {
                            return;
                        }
                        return;
                    }
                } else if (!AsyncHttpClient.isDebug) {
                    return;
                } else {
                    return;
                }
            case 5:
                onRetry();
                return;
            default:
                return;
        }
    }

    protected void sendMessage(Message msg) {
        if (getUseSynchronousMode() || this.handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) {
            this.handler.sendMessage(msg);
        }
    }

    protected void postRunnable(Runnable r) {
        if (r != null) {
            this.handler.post(r);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        if (this.handler != null) {
            return this.handler.obtainMessage(responseMessage, response);
        }
        Message msg = Message.obtain();
        if (msg == null) {
            return msg;
        }
        msg.what = responseMessage;
        msg.obj = response;
        return msg;
    }

    public void sendResponseMessage(HttpResponse response) throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            byte[] responseBody = getResponseData(response.getEntity());
            if (!Thread.currentThread().isInterrupted()) {
                if (status.getStatusCode() >= 300) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
                } else {
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
                }
            }
        }
    }

    byte[] getResponseData(HttpEntity entity) throws IOException {
        byte[] responseBody = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                long contentLength = entity.getContentLength();
                if (contentLength > 2147483647L) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                if (contentLength < 0) {
                    contentLength = PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
                }
                try {
                    ByteArrayBuffer buffer = new ByteArrayBuffer((int) contentLength);
                    byte[] tmp = new byte[4096];
                    int count = 0;
                    while (true) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        count += l;
                        buffer.append(tmp, 0, l);
                        sendProgressMessage(count, (int) contentLength);
                    }
                    instream.close();
                    responseBody = buffer.toByteArray();
                } catch (OutOfMemoryError e) {
                    System.gc();
                    throw new IOException("File too large to fit into available memory");
                } catch (Throwable th) {
                    instream.close();
                }
            }
        }
        return responseBody;
    }
}
