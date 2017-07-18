package com.customsdk.tools;

import android.content.ClipData;
import android.content.ClipboardManager;
import com.unity3d.player.UnityPlayer;
import java.util.Locale;

public class Tools {
    public static final String GetLocalLanguages() {
        return Locale.getDefault().getLanguage();
    }

    public static final String GetLocalCountry() {
        return Locale.getDefault().getCountry();
    }

    public void CopyStringToClipboard(final String str) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                ((ClipboardManager) UnityPlayer.currentActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("FromUnitStr", str));
            }
        });
    }
}
