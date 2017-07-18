package com.example.customtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.unity3d.player.UnityPlayer;

public class DummyActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        UnityPlayer.UnitySendMessage("Canvas", "LoadMainLevel", "");
        finish();
    }

    public static void static__reload() {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                UnityPlayer.currentActivity.startActivity(new Intent(UnityPlayer.currentActivity.getApplicationContext(), DummyActivity.class));
            }
        });
    }
}
