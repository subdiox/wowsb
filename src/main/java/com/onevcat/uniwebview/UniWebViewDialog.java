package com.onevcat.uniwebview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;
import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class UniWebViewDialog extends Dialog {
    public static final int ANIMATION_EDGE_BOTTOM = 3;
    public static final int ANIMATION_EDGE_LEFT = 2;
    public static final int ANIMATION_EDGE_NONE = 0;
    public static final int ANIMATION_EDGE_RIGHT = 4;
    public static final int ANIMATION_EDGE_TOP = 1;
    private AlertDialog _alertDialog;
    private boolean _animating = false;
    private boolean _backButtonEnable = true;
    private int _backgroundColor = -1;
    private int _bottom;
    private boolean _canGoBack;
    private boolean _canGoForward;
    private FrameLayout _content;
    private String _currentUrl = "";
    private String _currentUserAgent;
    private boolean _immersiveMode = true;
    private boolean _isLoading;
    private int _left;
    private DialogListener _listener;
    private boolean _loadingInterrupted;
    private boolean _manualHide;
    private boolean _openLinksInExternalBrowser;
    private int _right;
    private boolean _showSpinnerWhenLoading = true;
    private ProgressDialog _spinner;
    private String _spinnerText = "Loading...";
    private int _top;
    private UniWebView _uniWebView;
    private float alpha = 1.0f;
    private HashMap<String, String> headers;
    public ArrayList<String> schemes;
    private ArrayList<String> trustSites;

    public interface DialogListener {
        void onAnimationFinished(UniWebViewDialog uniWebViewDialog, String str);

        void onDialogClose(UniWebViewDialog uniWebViewDialog);

        void onDialogKeyDown(UniWebViewDialog uniWebViewDialog, int i);

        void onDialogShouldCloseByBackButton(UniWebViewDialog uniWebViewDialog);

        void onHideTransitionFinished(UniWebViewDialog uniWebViewDialog);

        void onJavaScriptFinished(UniWebViewDialog uniWebViewDialog, String str);

        void onPageFinished(UniWebViewDialog uniWebViewDialog, String str);

        void onPageStarted(UniWebViewDialog uniWebViewDialog, String str);

        void onReceivedError(UniWebViewDialog uniWebViewDialog, int i, String str, String str2);

        void onShowTransitionFinished(UniWebViewDialog uniWebViewDialog);

        boolean shouldOverrideUrlLoading(UniWebViewDialog uniWebViewDialog, String str);
    }

    public UniWebView getWebView() {
        return this._uniWebView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (!this._backButtonEnable || goBack()) {
            return true;
        }
        this._listener.onDialogShouldCloseByBackButton(this);
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("UniWebView", "onKeyDown " + event);
        if (event.getAction() == 0) {
            this._listener.onDialogKeyDown(this, event.getKeyCode());
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint({"NewApi"})
    public UniWebViewDialog(Context context, DialogListener listener) {
        super(context, 16973932);
        this._listener = listener;
        this.schemes = new ArrayList();
        this.schemes.add("uniwebview");
        this.trustSites = new ArrayList();
        this.headers = new HashMap();
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(32);
        window.setSoftInputMode(16);
        if (VERSION.SDK_INT < 16) {
            window.addFlags(1024);
        } else {
            HideSystemUI();
        }
        window.setFlags(1024, 1024);
        createContent();
        createWebView();
        createSpinner();
        addContentView(this._content, new LayoutParams(-1, -1));
        this._content.addView(this._uniWebView);
        Log.d("UniWebView", "Create a new UniWebView Dialog");
        this._content.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                UniWebViewDialog.this._content.getWindowVisibleDisplayFrame(r);
                if (Math.abs(r.height() - UniWebViewDialog.this._content.getHeight()) > 128) {
                    UniWebViewDialog.this.HideSystemUI();
                }
            }
        });
    }

    @SuppressLint({"NewApi"})
    public void HideSystemUI() {
        if (VERSION.SDK_INT >= 16) {
            int uiOptions;
            int updatedUIOptions;
            final View decorView = getWindow().getDecorView();
            if (VERSION.SDK_INT < 19 || !this._immersiveMode) {
                uiOptions = 4;
            } else {
                uiOptions = 3846;
            }
            decorView.setSystemUiVisibility(uiOptions);
            if (VERSION.SDK_INT < 19 || !this._immersiveMode) {
                updatedUIOptions = 4;
            } else {
                updatedUIOptions = 5894;
            }
            final int finalUiOptions = updatedUIOptions;
            decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
                public void onSystemUiVisibilityChange(int i) {
                    decorView.setOnSystemUiVisibilityChangeListener(null);
                    decorView.setSystemUiVisibility(finalUiOptions);
                }
            });
        }
    }

    public void changeInsets(int top, int left, int bottom, int right) {
        this._top = top;
        this._left = left;
        this._bottom = bottom;
        this._right = right;
        updateContentSize();
    }

    public void load(String url) {
        Log.d("UniWebView", url);
        this._uniWebView.loadUrl(url, this.headers);
    }

    public void postUrl(String url, byte[] postData) {
        this._uniWebView.postUrl(url, postData);
    }

    public void addJs(String js) {
        if (js == null) {
            Log.d("UniWebView", "Trying to add a null js. Abort.");
            return;
        }
        load(String.format("javascript:%s", new Object[]{js}));
    }

    public void loadJS(String js) {
        if (js == null) {
            Log.d("UniWebView", "Trying to eval a null js. Abort.");
            return;
        }
        String jsReformat = js.trim();
        while (jsReformat.endsWith(";") && jsReformat.length() != 0) {
            jsReformat = jsReformat.substring(0, jsReformat.length() - 1);
        }
        load(String.format("javascript:android.onData(%s)", new Object[]{jsReformat}));
    }

    public void loadHTMLString(String html, String baseURL) {
        this._uniWebView.loadDataWithBaseURL(baseURL, html, "text/html", AsyncHttpResponseHandler.DEFAULT_CHARSET, null);
    }

    public void cleanCache() {
        this._uniWebView.clearCache(true);
    }

    public boolean goBack() {
        if (!this._uniWebView.canGoBack()) {
            return false;
        }
        this._uniWebView.goBack();
        return true;
    }

    public boolean goForward() {
        if (!this._uniWebView.canGoForward()) {
            return false;
        }
        this._uniWebView.goForward();
        return true;
    }

    public void destroy() {
        this._uniWebView.loadUrl("about:blank");
        UniWebViewManager.Instance().removeShowingWebViewDialog(this);
        dismiss();
    }

    protected void onStop() {
        this._listener.onDialogClose(this);
    }

    private void showDialog() {
        if (VERSION.SDK_INT < 19 || !this._immersiveMode) {
            show();
            return;
        }
        getWindow().setFlags(8, 8);
        show();
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
        getWindow().clearFlags(8);
    }

    public void setShow(boolean show, boolean fade, int direction, float duration) {
        if (this._animating) {
            Log.d("UniWebView", "Trying to animate but another transition animation is not finished yet. Ignore this one.");
            return;
        }
        if (show) {
            showDialog();
            if (this._showSpinnerWhenLoading && this._isLoading) {
                showSpinner();
            }
            UniWebViewManager.Instance().addShowingWebViewDialog(this);
            this._manualHide = false;
        } else {
            ((InputMethodManager) UnityPlayer.currentActivity.getSystemService("input_method")).hideSoftInputFromWindow(this._uniWebView.getWindowToken(), 0);
            this._spinner.hide();
            this._manualHide = true;
        }
        if (fade || direction != 0) {
            Animation a;
            int xValue;
            int yValue;
            this._animating = true;
            final View v = ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
            AnimationSet set = new AnimationSet(false);
            int durationMills = (int) (1000.0f * duration);
            if (fade) {
                a = new AlphaAnimation(show ? 0.0f : 1.0f, show ? 1.0f : 0.0f);
                a.setFillAfter(true);
                a.setDuration((long) durationMills);
                set.addAnimation(a);
            }
            Point size = displaySize();
            if (direction == 1) {
                xValue = 0;
                yValue = -size.y;
            } else if (direction == 2) {
                xValue = -size.x;
                yValue = 0;
            } else if (direction == 3) {
                xValue = 0;
                yValue = size.y;
            } else if (direction == 4) {
                xValue = size.x;
                yValue = 0;
            } else if (direction == 0) {
                xValue = 0;
                yValue = 0;
            } else {
                Log.d("UniWebView", "Unknown direction. You should send 0~5");
                return;
            }
            if (direction != 0) {
                float f;
                float f2;
                float f3;
                float f4;
                if (show) {
                    f = (float) xValue;
                } else {
                    f = 0.0f;
                }
                if (show) {
                    f2 = 0.0f;
                } else {
                    f2 = (float) xValue;
                }
                if (show) {
                    f3 = (float) yValue;
                } else {
                    f3 = 0.0f;
                }
                if (show) {
                    f4 = 0.0f;
                } else {
                    f4 = (float) yValue;
                }
                a = new TranslateAnimation(f, f2, f3, f4);
                a.setFillAfter(true);
                a.setDuration((long) durationMills);
                set.addAnimation(a);
            }
            v.startAnimation(set);
            final boolean z = show;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    UniWebViewDialog.this._animating = false;
                    v.clearAnimation();
                    if (z) {
                        UniWebViewDialog.this._listener.onShowTransitionFinished(UniWebViewDialog.this);
                        return;
                    }
                    UniWebViewDialog.this.hide();
                    UniWebViewDialog.this._listener.onHideTransitionFinished(UniWebViewDialog.this);
                }
            }, (long) durationMills);
        } else if (show) {
            this._listener.onShowTransitionFinished(this);
        } else {
            hide();
            this._listener.onHideTransitionFinished(this);
        }
    }

    Point displaySize() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size;
        if (VERSION.SDK_INT >= 19 && this._immersiveMode) {
            size = new Point();
            display.getRealSize(size);
            return size;
        } else if (VERSION.SDK_INT < 13) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            size = new Point();
            display.getSize(size);
            return size;
        }
    }

    public void updateContentSize() {
        Window window = getWindow();
        Point size = displaySize();
        int width = Math.max(0, (size.x - this._left) - this._right);
        int height = Math.max(0, (size.y - this._top) - this._bottom);
        if (width == 0 || height == 0) {
            Log.d("UniWebView", "The inset is lager then screen size. Webview will not show. Please check your insets setting.");
            return;
        }
        window.setLayout(width, height);
        WindowManager.LayoutParams layoutParam = window.getAttributes();
        layoutParam.gravity = 51;
        layoutParam.x = this._left;
        layoutParam.y = this._top;
        window.setAttributes(layoutParam);
    }

    public void setSpinnerShowWhenLoading(boolean showSpinnerWhenLoading) {
        this._showSpinnerWhenLoading = showSpinnerWhenLoading;
    }

    public void setSpinnerText(String text) {
        if (text != null) {
            this._spinnerText = text;
        } else {
            this._spinnerText = "";
        }
        this._spinner.setMessage(text);
    }

    private void showSpinner() {
        if (VERSION.SDK_INT < 19 || !this._immersiveMode) {
            this._spinner.show();
            return;
        }
        this._spinner.getWindow().setFlags(8, 8);
        this._spinner.show();
        this._spinner.getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility());
        this._spinner.getWindow().clearFlags(8);
    }

    private void createContent() {
        this._content = new FrameLayout(getContext());
        this._content.setVisibility(0);
    }

    private void createSpinner() {
        this._spinner = new ProgressDialog(getContext());
        this._spinner.setCanceledOnTouchOutside(true);
        this._spinner.requestWindowFeature(1);
        this._spinner.setMessage(this._spinnerText);
    }

    private void createWebView() {
        this._uniWebView = new UniWebView(getContext());
        this._uniWebView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                UniWebViewDialog.this._canGoBack = view.canGoBack();
                UniWebViewDialog.this._canGoForward = view.canGoForward();
                Log.d("UniWebView", "Start Loading URL: " + url);
                super.onPageStarted(view, url, favicon);
                if (UniWebViewDialog.this._showSpinnerWhenLoading && UniWebViewDialog.this.isShowing()) {
                    UniWebViewDialog.this.showSpinner();
                }
                UniWebViewDialog.this._isLoading = true;
                UniWebViewDialog.this._listener.onPageStarted(UniWebViewDialog.this, url);
                UniWebViewDialog.this.HideSystemUI();
            }

            public void onPageFinished(WebView view, String url) {
                UniWebViewDialog.this._canGoBack = view.canGoBack();
                UniWebViewDialog.this._canGoForward = view.canGoForward();
                UniWebViewDialog.this._spinner.hide();
                UniWebViewDialog.this._currentUrl = url;
                UniWebViewDialog.this._currentUserAgent = UniWebViewDialog.this._uniWebView.getSettings().getUserAgentString();
                UniWebViewDialog.this._listener.onPageFinished(UniWebViewDialog.this, url);
                UniWebViewDialog.this._isLoading = false;
                UniWebViewDialog.this._uniWebView.setWebViewBackgroundColor(UniWebViewDialog.this._backgroundColor);
                UniWebViewDialog.this.HideSystemUI();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                UniWebViewDialog.this._canGoBack = view.canGoBack();
                UniWebViewDialog.this._canGoForward = view.canGoForward();
                UniWebViewDialog.this.HideSystemUI();
                UniWebViewDialog.this._spinner.hide();
                UniWebViewDialog.this._currentUrl = failingUrl;
                UniWebViewDialog.this._currentUserAgent = UniWebViewDialog.this._uniWebView.getSettings().getUserAgentString();
                UniWebViewDialog.this._listener.onReceivedError(UniWebViewDialog.this, errorCode, description, failingUrl);
                UniWebViewDialog.this._isLoading = false;
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("UniWebView", "shouldOverrideUrlLoading: " + url);
                return UniWebViewDialog.this._listener.shouldOverrideUrlLoading(UniWebViewDialog.this, url);
            }
        });
        FrameLayout customViewContainer = new FrameLayout(getContext());
        customViewContainer.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addContentView(customViewContainer, new LayoutParams(-1, -1));
        this._uniWebView.setWebChromeClient(new UniWebChromeClient(this._content, customViewContainer, null, this._uniWebView) {
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("UniWebView", "onPermissionRequest");
                AndroidPlugin.getUnityActivity_().runOnUiThread(new Runnable() {
                    @TargetApi(21)
                    public void run() {
                        String url = request.getOrigin().toString();
                        Log.d("UniWebView", "Request from url: " + url);
                        if (UniWebViewDialog.this.trustSites.contains(url)) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }

            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                UniWebViewDialog.this._alertDialog = new Builder(UniWebViewDialog.this.getContext()).setTitle(url).setMessage(message).setCancelable(false).setIcon(17301543).setPositiveButton(17039370, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        result.confirm();
                        UniWebViewDialog.this._alertDialog = null;
                    }
                }).create();
                if (VERSION.SDK_INT < 19 || !UniWebViewDialog.this._immersiveMode) {
                    UniWebViewDialog.this._alertDialog.show();
                } else {
                    UniWebViewDialog.this._alertDialog.getWindow().setFlags(8, 8);
                    UniWebViewDialog.this._alertDialog.show();
                    UniWebViewDialog.this._alertDialog.getWindow().getDecorView().setSystemUiVisibility(UniWebViewDialog.this.getWindow().getDecorView().getSystemUiVisibility());
                    UniWebViewDialog.this._alertDialog.getWindow().clearFlags(8);
                }
                return true;
            }

            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                UniWebViewDialog.this._alertDialog = new Builder(UniWebViewDialog.this.getContext()).setTitle(url).setMessage(message).setIcon(17301659).setCancelable(false).setPositiveButton(17039379, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        result.confirm();
                        UniWebViewDialog.this._alertDialog = null;
                    }
                }).setNegativeButton(17039369, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        result.cancel();
                        UniWebViewDialog.this._alertDialog = null;
                    }
                }).show();
                return true;
            }

            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                Builder alertDialogBuilder = new Builder(UniWebViewDialog.this.getContext());
                alertDialogBuilder.setTitle(url).setMessage(message).setIcon(17301659).setCancelable(false);
                final EditText input = new EditText(UniWebViewDialog.this.getContext());
                input.setSingleLine();
                alertDialogBuilder.setView(input);
                alertDialogBuilder.setPositiveButton(17039379, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable editable = input.getText();
                        String value = "";
                        if (editable != null) {
                            value = editable.toString();
                        }
                        dialog.dismiss();
                        result.confirm(value);
                        UniWebViewDialog.this._alertDialog = null;
                    }
                });
                alertDialogBuilder.setNegativeButton(17039369, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        result.cancel();
                        UniWebViewDialog.this._alertDialog = null;
                    }
                });
                UniWebViewDialog.this._alertDialog = alertDialogBuilder.show();
                return true;
            }
        });
        this._uniWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse(url));
                AndroidPlugin.getUnityActivity_().startActivity(i);
            }
        });
        this._uniWebView.setVisibility(0);
        this._uniWebView.addJavascriptInterface(this, "android");
        setBounces(false);
    }

    @JavascriptInterface
    public void onData(String value) {
        Log.d("UniWebView", "receive a call back from js: " + value);
        this._listener.onJavaScriptFinished(this, value);
    }

    public void goBackGround() {
        if (this._isLoading) {
            this._loadingInterrupted = true;
            this._uniWebView.stopLoading();
        }
        if (this._alertDialog != null) {
            this._alertDialog.hide();
        }
        hide();
        if (VERSION.SDK_INT >= 11) {
            this._uniWebView.onPause();
        }
    }

    public void goForeGround() {
        if (!this._manualHide) {
            if (this._loadingInterrupted) {
                this._uniWebView.reload();
                this._loadingInterrupted = false;
            }
            show();
            if (this._alertDialog != null) {
                this._alertDialog.show();
            }
        }
        if (VERSION.SDK_INT >= 11) {
            this._uniWebView.onResume();
        }
    }

    public void setTransparent(boolean transparent) {
        Log.d("UniWebView", "SetTransparentBackground is already deprecated and there is no guarantee it will work in later versions. You should use SetBackgroundColor instead.");
        if (transparent) {
            this._backgroundColor = Color.argb(0, 0, 0, 0);
        } else {
            this._backgroundColor = Color.argb(255, 255, 255, 255);
        }
        this._uniWebView.setWebViewBackgroundColor(this._backgroundColor);
    }

    public void setBackgroundColor(float r, float g, float b, float a) {
        this._backgroundColor = Color.argb((int) (a * 255.0f), (int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f));
        this._uniWebView.setWebViewBackgroundColor(this._backgroundColor);
    }

    public String getUrl() {
        return this._currentUrl;
    }

    public void setBackButtonEnable(boolean enable) {
        this._backButtonEnable = enable;
    }

    public void setBounces(boolean enable) {
        if (VERSION.SDK_INT <= 8) {
            Log.d("UniWebView", "WebView over scroll effect supports after API 9");
        } else if (enable) {
            this._uniWebView.setOverScrollMode(0);
        } else {
            this._uniWebView.setOverScrollMode(2);
        }
    }

    public void setZoomEnable(boolean enable) {
        this._uniWebView.getSettings().setBuiltInZoomControls(enable);
    }

    public void reload() {
        this._uniWebView.reload();
    }

    public void addUrlScheme(String scheme) {
        if (!this.schemes.contains(scheme)) {
            this.schemes.add(scheme);
        }
    }

    public void removeUrlScheme(String scheme) {
        if (this.schemes.contains(scheme)) {
            this.schemes.remove(scheme);
        }
    }

    public void stop() {
        this._uniWebView.stopLoading();
    }

    public void useWideViewPort(boolean use) {
        this._uniWebView.getSettings().setUseWideViewPort(use);
    }

    public void loadWithOverviewMode(boolean overview) {
        this._uniWebView.getSettings().setLoadWithOverviewMode(overview);
    }

    public String getUserAgent() {
        return this._currentUserAgent;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        if (VERSION.SDK_INT < 11) {
            AlphaAnimation animation = new AlphaAnimation(this.alpha, this.alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            this._uniWebView.startAnimation(animation);
            return;
        }
        this._uniWebView.setAlpha(this.alpha);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setImmersiveModeEnabled(boolean immersiveModeEnabled) {
        this._immersiveMode = immersiveModeEnabled;
    }

    public void AddPermissionRequestTrustSite(String site) {
        if (site != null && site.length() != 0) {
            this.trustSites.add(site);
        }
    }

    public boolean canGoBack() {
        return this._canGoBack;
    }

    public boolean canGoForward() {
        return this._canGoForward;
    }

    public void setPosition(int x, int y) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParam = window.getAttributes();
        layoutParam.x = x;
        layoutParam.y = y;
        window.setAttributes(layoutParam);
    }

    public void setSize(int width, int height) {
        if (width < 0 || height < 0) {
            Log.d("UniWebView", "The width or height of size is less than 0. Webview will not show. Please check your setting. Input width: " + width + ", input height: " + height);
        } else {
            getWindow().setLayout(width, height);
        }
    }

    public void animateTo(int deltaX, int deltaY, float duration, float delay, final String identifier) {
        View v = ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
        int durationInMills = (int) (duration * 1000.0f);
        int delayInMills = (int) (delay * 1000.0f);
        Animation a = new TranslateAnimation(0.0f, (float) deltaX, 0.0f, (float) deltaY);
        a.setFillAfter(true);
        a.setDuration((long) durationInMills);
        a.setStartOffset((long) delayInMills);
        v.startAnimation(a);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                UniWebViewDialog.this._listener.onAnimationFinished(UniWebViewDialog.this, identifier);
            }
        }, (long) (durationInMills + delayInMills));
    }

    public void setHeaderField(String key, String value) {
        if (value == null || value.length() == 0) {
            this.headers.remove(key);
        } else if (key != null && key.length() != 0) {
            this.headers.put(key, value);
        }
    }

    public void setVerticalScrollBarShow(boolean show) {
        this._uniWebView.setVerticalScrollBarEnabled(show);
    }

    public void setHorizontalScrollBarShow(boolean show) {
        this._uniWebView.setHorizontalScrollBarEnabled(show);
    }

    public boolean getOpenLinksInExternalBrowser() {
        return this._openLinksInExternalBrowser;
    }

    public void setOpenLinksInExternalBrowser(boolean value) {
        this._openLinksInExternalBrowser = value;
    }
}
