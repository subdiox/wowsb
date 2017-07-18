package com.onevcat.uniwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.widget.FrameLayout.LayoutParams;

@SuppressLint({"SetJavaScriptEnabled"})
public class UniWebView extends VideoEnabledWebView {
    public static String customUserAgent;

    public UniWebView(Context context) {
        super(context);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setGeolocationEnabled(true);
        if (!(customUserAgent == null || customUserAgent.equals(""))) {
            webSettings.setUserAgentString(customUserAgent);
        }
        if (VERSION.SDK_INT >= 8) {
            webSettings.setPluginState(PluginState.ON);
        }
        if (VERSION.SDK_INT >= 11) {
            webSettings.setDisplayZoomControls(false);
        }
        if (VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        setScrollBarStyle(0);
        setVerticalScrollbarOverlay(true);
        setLayoutParams(new LayoutParams(-1, -1));
    }

    public void setAllowAutoPlay(boolean flag) {
        if (VERSION.SDK_INT >= 17) {
            getSettings().setMediaPlaybackRequiresUserGesture(!flag);
        }
    }

    public void updateTransparent(boolean transparent) {
        if (transparent) {
            setBackgroundColor(0);
        } else {
            setBackgroundColor(-1);
        }
    }

    public void setWebViewBackgroundColor(int color) {
        setBackgroundColor(color);
    }
}
