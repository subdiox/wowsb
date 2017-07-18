package com.onevcat.uniwebview;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.onevcat.uniwebview.UniWebViewDialog.DialogListener;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import java.util.Iterator;

public class AndroidPlugin extends UnityPlayerActivity {
    public static final int FILECHOOSER_RESULTCODE = 19238467;
    protected static final String LOG_TAG = "UniWebView";
    protected static String _cameraPhotoPath;
    protected static ValueCallback<Uri[]> _uploadCallback;
    protected static ValueCallback<Uri> _uploadMessages;

    public static Activity getUnityActivity_() {
        return UnityPlayer.currentActivity;
    }

    public static void setUploadMessage(ValueCallback<Uri> message) {
        _uploadMessages = message;
    }

    public static void setUploadCallback(ValueCallback<Uri[]> message) {
        _uploadCallback = message;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(getUnityActivity_());
    }

    public void onPause() {
        super.onPause();
        ShowAllWebViewDialogs(false);
        if (VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().flush();
            return;
        }
        CookieSyncManager manager = CookieSyncManager.getInstance();
        if (manager != null) {
            manager.stopSync();
        }
    }

    public void onResume() {
        super.onResume();
        ShowAllWebViewDialogs(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                AndroidPlugin.this.ShowAllWebViewDialogs(true);
            }
        }, 200);
        CookieSyncManager manager = CookieSyncManager.getInstance();
        if (manager != null) {
            manager.startSync();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(LOG_TAG, "Rotation: " + newConfig.orientation);
        for (UniWebViewDialog dialog : UniWebViewManager.Instance().allDialogs()) {
            dialog.updateContentSize();
            dialog.HideSystemUI();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || (_uploadMessages == null && _uploadCallback == null)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri[] results = null;
        Uri result = null;
        if (resultCode == -1) {
            if (data != null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    result = Uri.parse(dataString);
                    results = new Uri[]{result};
                }
            } else if (_cameraPhotoPath != null) {
                result = Uri.parse(_cameraPhotoPath);
                results = new Uri[]{result};
            }
        }
        if (_uploadCallback != null) {
            _uploadCallback.onReceiveValue(results);
            _uploadCallback = null;
        }
        if (_uploadMessages != null) {
            _uploadMessages.onReceiveValue(result);
            _uploadMessages = null;
        }
        _cameraPhotoPath = null;
    }

    public static void _UniWebViewInit(String name, int top, int left, int bottom, int right) {
        final String str = name;
        final int i = top;
        final int i2 = left;
        final int i3 = bottom;
        final int i4 = right;
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewInit");
                UniWebViewDialog dialog = new UniWebViewDialog(AndroidPlugin.getUnityActivity_(), new DialogListener() {
                    public void onPageFinished(UniWebViewDialog dialog, String url) {
                        Log.d(AndroidPlugin.LOG_TAG, "page load finished: " + url);
                        UnityPlayer.UnitySendMessage(str, "LoadComplete", "");
                    }

                    public void onPageStarted(UniWebViewDialog dialog, String url) {
                        Log.d(AndroidPlugin.LOG_TAG, "page load started: " + url);
                        UnityPlayer.UnitySendMessage(str, "LoadBegin", url);
                    }

                    public void onReceivedError(UniWebViewDialog dialog, int errorCode, String description, String failingUrl) {
                        Log.d(AndroidPlugin.LOG_TAG, "page load error: " + failingUrl + " Error: " + description);
                        UnityPlayer.UnitySendMessage(str, "LoadComplete", description);
                    }

                    public boolean shouldOverrideUrlLoading(UniWebViewDialog dialog, String url) {
                        if (url.startsWith("mailto:")) {
                            AndroidPlugin.getUnityActivity_().startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(url)));
                            return true;
                        } else if (url.startsWith("tel:")) {
                            AndroidPlugin.getUnityActivity_().startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
                            return true;
                        } else if (url.startsWith("sms:")) {
                            try {
                                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.addCategory("android.intent.category.BROWSABLE");
                                AndroidPlugin.getUnityActivity_().startActivity(intent);
                                return true;
                            } catch (Exception e) {
                                Log.d(AndroidPlugin.LOG_TAG, e.getMessage());
                                return false;
                            }
                        } else {
                            boolean canResponseScheme = false;
                            Iterator it = dialog.schemes.iterator();
                            while (it.hasNext()) {
                                if (url.startsWith(((String) it.next()) + "://")) {
                                    canResponseScheme = true;
                                    break;
                                }
                            }
                            if (canResponseScheme) {
                                UnityPlayer.UnitySendMessage(str, "ReceivedMessage", url);
                                return true;
                            } else if (dialog == null || dialog.getWebView() == null || dialog.getWebView().getHitTestResult() == null) {
                                Log.d(AndroidPlugin.LOG_TAG, "dialog null!");
                                return false;
                            } else if (dialog.getWebView().getHitTestResult().getType() <= 0 || !dialog.getOpenLinksInExternalBrowser()) {
                                return false;
                            } else {
                                Intent i = new Intent("android.intent.action.VIEW");
                                i.setData(Uri.parse(url));
                                AndroidPlugin.getUnityActivity_().startActivity(i);
                                return true;
                            }
                        }
                    }

                    public void onDialogShouldCloseByBackButton(UniWebViewDialog dialog) {
                        Log.d(AndroidPlugin.LOG_TAG, "dialog should be closed");
                        UnityPlayer.UnitySendMessage(str, "WebViewDone", "");
                    }

                    public void onDialogKeyDown(UniWebViewDialog dialog, int keyCode) {
                        UnityPlayer.UnitySendMessage(str, "WebViewKeyDown", Integer.toString(keyCode));
                    }

                    public void onDialogClose(UniWebViewDialog dialog) {
                        UniWebViewManager.Instance().removeUniWebView(str);
                    }

                    public void onJavaScriptFinished(UniWebViewDialog dialog, String result) {
                        UnityPlayer.UnitySendMessage(str, "EvalJavaScriptFinished", result);
                    }

                    public void onAnimationFinished(UniWebViewDialog dialog, String identifier) {
                        UnityPlayer.UnitySendMessage(str, "AnimationFinished", identifier);
                    }

                    public void onShowTransitionFinished(UniWebViewDialog dialog) {
                        UnityPlayer.UnitySendMessage(str, "ShowTransitionFinished", "");
                    }

                    public void onHideTransitionFinished(UniWebViewDialog dialog) {
                        UnityPlayer.UnitySendMessage(str, "HideTransitionFinished", "");
                    }
                });
                dialog.changeInsets(i, i2, i3, i4);
                UniWebViewManager.Instance().setUniWebView(str, dialog);
            }
        });
    }

    public static void _UniWebViewLoad(final String name, final String url) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewLoad");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.load(url);
                }
            }
        });
    }

    public static void _UniWebViewPostUrl(final String name, final String url, final byte[] postData) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewPostUrl");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.postUrl(url, postData);
                }
            }
        });
    }

    public static void _UniWebViewReload(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewReload");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.reload();
                }
            }
        });
    }

    public static void _UniWebViewStop(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewStop");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.stop();
                }
            }
        });
    }

    public static void _UniWebViewChangeInsets(String name, int top, int left, int bottom, int right) {
        final String str = name;
        final int i = top;
        final int i2 = left;
        final int i3 = bottom;
        final int i4 = right;
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewChangeSize");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(str);
                if (dialog != null) {
                    dialog.changeInsets(i, i2, i3, i4);
                }
            }
        });
    }

    public static void _UniWebViewShow(final String name, final boolean fade, final int direction, final float duration) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewShow");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setShow(true, fade, direction, duration);
                }
            }
        });
    }

    public static void _UniWebViewHide(final String name, final boolean fade, final int direction, final float duration) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewHide");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setShow(false, fade, direction, duration);
                }
            }
        });
    }

    public static void _UniWebViewEvaluatingJavaScript(final String name, final String js) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewEvaluatingJavaScript");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.loadJS(js);
                }
            }
        });
    }

    public static void _UniWebViewAddJavaScript(final String name, final String js) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewAddJavaScript");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.addJs(js);
                }
            }
        });
    }

    public static void _UniWebViewCleanCache(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewCleanCache");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.cleanCache();
                }
            }
        });
    }

    public static void _UniWebViewCleanCookie(String name, final String key) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewCleanCookie");
                CookieManager cm = CookieManager.getInstance();
                if (key == null || key.length() == 0) {
                    Log.d(AndroidPlugin.LOG_TAG, "Cleaning all cookies");
                    cm.removeAllCookie();
                } else {
                    Log.d(AndroidPlugin.LOG_TAG, "Setting an empty cookie for: " + key);
                    cm.setCookie(key, "");
                }
                if (VERSION.SDK_INT >= 21) {
                    CookieManager.getInstance().flush();
                    return;
                }
                CookieSyncManager manager = CookieSyncManager.getInstance();
                if (manager != null) {
                    manager.sync();
                }
            }
        });
    }

    public static void _UniWebViewSetCookie(String url, String cookie) {
        setCookie(url, cookie);
    }

    public static String _UniWebViewGetCookie(String url, String key) {
        return getCookie(url, key);
    }

    static void setCookie(String url, String cookie) {
        CookieManager.getInstance().setCookie(url, cookie);
    }

    static String getCookie(String url, String key) {
        String value = "";
        String cookies = CookieManager.getInstance().getCookie(url);
        if (cookies == null) {
            return "";
        }
        for (String kvPair : cookies.split(";")) {
            if (kvPair.contains(key)) {
                String[] pair = kvPair.split("=", 2);
                if (pair.length >= 2) {
                    value = pair[1];
                }
            }
        }
        return value;
    }

    public static void _UniWebViewDestroy(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewDestroy");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.destroy();
                }
            }
        });
    }

    public static void _UniWebViewTransparentBackground(final String name, final boolean transparent) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewTransparentBackground");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setTransparent(transparent);
                }
            }
        });
    }

    public static void _UniWebViewSetBackgroundColor(String name, float r, float g, float b, float a) {
        final String str = name;
        final float f = r;
        final float f2 = g;
        final float f3 = b;
        final float f4 = a;
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetBackgroundColor");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(str);
                if (dialog != null) {
                    dialog.setBackgroundColor(f, f2, f3, f4);
                }
            }
        });
    }

    public static void _UniWebViewSetSpinnerShowWhenLoading(final String name, final boolean show) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetSpinnerShowWhenLoading: " + show);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setSpinnerShowWhenLoading(show);
                }
            }
        });
    }

    public static void _UniWebViewSetSpinnerText(final String name, final String text) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetSpinnerText: " + text);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setSpinnerText(text);
                }
            }
        });
    }

    public static boolean _UniWebViewCanGoBack(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            return dialog.canGoBack();
        }
        return false;
    }

    public static boolean _UniWebViewCanGoForward(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            return dialog.canGoForward();
        }
        return false;
    }

    public static void _UniWebViewGoBack(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewGoBack");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.goBack();
                }
            }
        });
    }

    public static void _UniWebViewGoForward(final String name) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewGoForward");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.goForward();
                }
            }
        });
    }

    public static void _UniWebViewLoadHTMLString(final String name, final String htmlString, final String baseURL) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewLoadHTMLString");
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.loadHTMLString(htmlString, baseURL);
                }
            }
        });
    }

    public static String _UniWebViewGetCurrentUrl(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            return dialog.getUrl();
        }
        return "";
    }

    public static void _UniWebViewSetBackButtonEnable(final String name, final boolean enable) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetBackButtonEnable:" + enable);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setBackButtonEnable(enable);
                }
            }
        });
    }

    public static void _UniWebViewSetBounces(final String name, final boolean enable) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetBounces:" + enable);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setBounces(enable);
                }
            }
        });
    }

    public static void _UniWebViewSetZoomEnable(final String name, final boolean enable) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetZoomEnable:" + enable);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setZoomEnable(enable);
                }
            }
        });
    }

    public static void _UniWebViewAddUrlScheme(final String name, final String scheme) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewAddUrlScheme:" + scheme);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.addUrlScheme(scheme);
                }
            }
        });
    }

    public static void _UniWebViewRemoveUrlScheme(final String name, final String scheme) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewAddUrlScheme:" + scheme);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.removeUrlScheme(scheme);
                }
            }
        });
    }

    public static void _UniWebViewUseWideViewPort(final String name, final boolean use) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewUseWideViewPort:" + use);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.useWideViewPort(use);
                }
            }
        });
    }

    public static void _UniWebViewLoadWithOverviewMode(final String name, final boolean overview) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewLoadWithOverviewMode:" + overview);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.loadWithOverviewMode(overview);
                }
            }
        });
    }

    public static void _UniWebViewSetUserAgent(String userAgent) {
        UniWebView.customUserAgent = userAgent;
    }

    public static String _UniWebViewGetUserAgent(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            return dialog.getUserAgent();
        }
        return "";
    }

    public static void _UniWebViewSetAlpha(final String name, final float alpha) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                Log.d(AndroidPlugin.LOG_TAG, "_UniWebViewSetAlpha: " + alpha);
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setAlpha(alpha);
                }
            }
        });
    }

    public static float _UniWebViewGetAlpha(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            return dialog.getAlpha();
        }
        return 0.0f;
    }

    public static void _UniWebViewSetImmersiveModeEnabled(final String name, final boolean enabled) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setImmersiveModeEnabled(enabled);
                }
            }
        });
    }

    public static void _UniWebViewAddPermissionRequestTrustSite(String name, String site) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        if (dialog != null) {
            dialog.AddPermissionRequestTrustSite(site);
        }
    }

    public static void _UniWebViewSetPosition(final String name, final int x, final int y) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(name).setPosition(x, y);
            }
        });
    }

    public static void _UniWebViewSetSize(final String name, final int width, final int height) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(name).setSize(width, height);
            }
        });
    }

    public static void _UniWebViewAnimateTo(String name, int x, int y, float duration, float delay, String identifier) {
        final String str = name;
        final int i = x;
        final int i2 = y;
        final float f = duration;
        final float f2 = delay;
        final String str2 = identifier;
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(str).animateTo(i, i2, f, f2, str2);
            }
        });
    }

    public static void _UniWebViewSetHeaderField(final String name, final String key, final String value) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(name).setHeaderField(key, value);
            }
        });
    }

    public static void _UniWebViewSetVerticalScrollBarShow(final String name, final boolean show) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(name).setVerticalScrollBarShow(show);
            }
        });
    }

    public static void _UniWebViewSetHorizontalScrollBarShow(final String name, final boolean show) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewManager.Instance().getUniWebViewDialog(name).setHorizontalScrollBarShow(show);
            }
        });
    }

    public static boolean _UniWebViewGetOpenLinksInExternalBrowser(String name) {
        UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
        return dialog != null && dialog.getOpenLinksInExternalBrowser();
    }

    public static void _UniWebViewSetOpenLinksInExternalBrowser(final String name, final boolean value) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.setOpenLinksInExternalBrowser(value);
                }
            }
        });
    }

    public static void _UniWebViewSetWebContentsDebuggingEnabled(final boolean enabled) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                if (VERSION.SDK_INT >= 19) {
                    WebView.setWebContentsDebuggingEnabled(enabled);
                }
            }
        });
    }

    public static void _UniWebViewSetAllowAutoPlay(final String name, final boolean allowed) {
        runSafelyOnUiThread(new Runnable() {
            public void run() {
                UniWebViewDialog dialog = UniWebViewManager.Instance().getUniWebViewDialog(name);
                if (dialog != null) {
                    dialog.getWebView().setAllowAutoPlay(allowed);
                }
            }
        });
    }

    protected static void runSafelyOnUiThread(final Runnable r) {
        getUnityActivity_().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    r.run();
                } catch (Exception e) {
                    Log.d(AndroidPlugin.LOG_TAG, "UniWebView should run on UI thread: " + e.getMessage());
                }
            }
        });
    }

    protected void ShowAllWebViewDialogs(boolean show) {
        Iterator it = UniWebViewManager.Instance().getShowingWebViewDialogs().iterator();
        while (it.hasNext()) {
            UniWebViewDialog webViewDialog = (UniWebViewDialog) it.next();
            if (show) {
                Log.d(LOG_TAG, webViewDialog + "goForeGround");
                webViewDialog.goForeGround();
                webViewDialog.HideSystemUI();
            } else {
                Log.d(LOG_TAG, webViewDialog + "goBackGround");
                webViewDialog.goBackGround();
                webViewDialog.HideSystemUI();
            }
        }
    }
}
