package com.onevcat.uniwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import java.util.Map;

public class VideoEnabledWebView extends WebView {
    private boolean addedJavascriptInterface = false;
    private VideoEnabledWebChromeClient videoEnabledWebChromeClient;

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd() {
            Log.d("___", "GOT IT");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    if (VideoEnabledWebView.this.videoEnabledWebChromeClient != null) {
                        VideoEnabledWebView.this.videoEnabledWebChromeClient.onHideCustomView();
                    }
                }
            });
        }
    }

    public VideoEnabledWebView(Context context) {
        super(context);
    }

    public VideoEnabledWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoEnabledWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isVideoFullscreen() {
        return this.videoEnabledWebChromeClient != null && this.videoEnabledWebChromeClient.isVideoFullscreen();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void setWebChromeClient(WebChromeClient client) {
        getSettings().setJavaScriptEnabled(true);
        if (client instanceof VideoEnabledWebChromeClient) {
            this.videoEnabledWebChromeClient = (VideoEnabledWebChromeClient) client;
        }
        super.setWebChromeClient(client);
    }

    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void loadUrl(String url) {
        addJavascriptInterface();
        super.loadUrl(url);
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        addJavascriptInterface();
        super.loadUrl(url, additionalHttpHeaders);
    }

    public void postUrl(String url, byte[] postData) {
        addJavascriptInterface();
        super.postUrl(url, postData);
    }

    private void addJavascriptInterface() {
        if (!this.addedJavascriptInterface) {
            addJavascriptInterface(new JavascriptInterface(), "_VideoEnabledWebView");
            this.addedJavascriptInterface = true;
        }
    }
}
