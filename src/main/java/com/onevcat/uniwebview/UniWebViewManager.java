package com.onevcat.uniwebview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class UniWebViewManager {
    private static UniWebViewManager _instance = null;
    private ArrayList<UniWebViewDialog> _showingWebViewDialogs = new ArrayList();
    private HashMap<String, UniWebViewDialog> _webViewDialogDic = new HashMap();

    public static UniWebViewManager Instance() {
        if (_instance == null) {
            _instance = new UniWebViewManager();
        }
        return _instance;
    }

    public UniWebViewDialog getUniWebViewDialog(String name) {
        if (name == null || name.length() == 0 || !this._webViewDialogDic.containsKey(name)) {
            return null;
        }
        return (UniWebViewDialog) this._webViewDialogDic.get(name);
    }

    public void removeUniWebView(String name) {
        if (this._webViewDialogDic.containsKey(name)) {
            this._webViewDialogDic.remove(name);
        }
    }

    public void setUniWebView(String name, UniWebViewDialog webViewDialog) {
        this._webViewDialogDic.put(name, webViewDialog);
    }

    public Collection<UniWebViewDialog> allDialogs() {
        return this._webViewDialogDic.values();
    }

    public void addShowingWebViewDialog(UniWebViewDialog webViewDialog) {
        if (!this._showingWebViewDialogs.contains(webViewDialog)) {
            this._showingWebViewDialogs.add(webViewDialog);
        }
    }

    public void removeShowingWebViewDialog(UniWebViewDialog webViewDialog) {
        this._showingWebViewDialogs.remove(webViewDialog);
    }

    public ArrayList<UniWebViewDialog> getShowingWebViewDialogs() {
        return this._showingWebViewDialogs;
    }
}
