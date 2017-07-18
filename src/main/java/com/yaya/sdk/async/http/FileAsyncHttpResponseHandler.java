package com.yaya.sdk.async.http;

import android.content.Context;
import android.util.Log;
import com.yaya.sdk.utils.Mylog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled = (!FileAsyncHttpResponseHandler.class.desiredAssertionStatus());
    private static final String LOG_TAG = "FileAsyncHttpResponseHandler";
    private File mFile;

    public FileAsyncHttpResponseHandler(File file) {
        if ($assertionsDisabled || file != null) {
            this.mFile = file;
            return;
        }
        throw new AssertionError();
    }

    public FileAsyncHttpResponseHandler(Context c) {
        if ($assertionsDisabled || c != null) {
            this.mFile = getTemporaryFile(c);
            return;
        }
        throw new AssertionError();
    }

    protected File getTemporaryFile(Context c) {
        try {
            return File.createTempFile("temp_", "_handled", c.getCacheDir());
        } catch (Throwable t) {
            if (AsyncHttpClient.isDebug) {
                Log.e(LOG_TAG, "Cannot create temporary file", t);
            }
            return null;
        }
    }

    protected File getTargetFile() {
        if ($assertionsDisabled || this.mFile != null) {
            return this.mFile;
        }
        throw new AssertionError();
    }

    public void onSuccess(File file) {
    }

    public void onSuccess(int statusCode, File file) {
        onSuccess(file);
    }

    public void onSuccess(int statusCode, Header[] headers, File file) {
        onSuccess(statusCode, file);
    }

    public void onFailure(Throwable e, File response) {
        onFailure(e);
    }

    public void onFailure(int statusCode, Throwable e, File response) {
        onFailure(e, response);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable e, File response) {
        onFailure(statusCode, e, response);
    }

    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFailure(statusCode, headers, error, getTargetFile());
    }

    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        onSuccess(statusCode, headers, getTargetFile());
    }

    byte[] getResponseData(HttpEntity entity) throws IOException {
        Mylog.d(LOG_TAG, "entry: " + entity);
        if (entity != null) {
            InputStream instream = entity.getContent();
            long contentLength = entity.getContentLength();
            FileOutputStream buffer = new FileOutputStream(getTargetFile());
            if (instream != null) {
                try {
                    byte[] tmp = new byte[4096];
                    int count = 0;
                    while (true) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            instream.close();
                            buffer.flush();
                            buffer.close();
                        } else {
                            count += l;
                            buffer.write(tmp, 0, l);
                            sendProgressMessage(count, (int) contentLength);
                        }
                    }
                    instream.close();
                    buffer.flush();
                    buffer.close();
                } catch (Throwable th) {
                    instream.close();
                    buffer.flush();
                    buffer.close();
                }
            }
        }
        return null;
    }
}
