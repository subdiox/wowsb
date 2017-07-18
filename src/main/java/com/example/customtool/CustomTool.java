package com.example.customtool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Process;
import com.unity3d.player.UnityPlayer;

public class CustomTool {
    public static void restart(float delayTime) {
        if (delayTime < 200.0f) {
            delayTime = 200.0f;
        }
        ((AlarmManager) UnityPlayer.currentActivity.getSystemService("alarm")).set(1, (long) (((float) System.currentTimeMillis()) + delayTime), PendingIntent.getActivity(UnityPlayer.currentActivity, 0, UnityPlayer.currentActivity.getPackageManager().getLaunchIntentForPackage(UnityPlayer.currentActivity.getPackageName()), 67108864));
        UnityPlayer.currentActivity.finish();
        Process.killProcess(Process.myPid());
    }
}
