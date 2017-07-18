package com.lthz.update;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import com.lthz.update.DownloadService.DownloadBinder;
import com.unity3d.player.UnityPlayer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NotificationApkUpdate {
    private static final String FINISH_CALLBACK_MESSAGE = "OnApkDownloadFinished";
    private static final String UNITY_OBJECT_NAME = "ICustomSdk";
    private static final String UPDATE_CALLBACK_MESSAGE = "OnApkDownloadUpdated";
    private static NotificationApkUpdate sInstance;
    private ICallbackResult callback = new ICallbackResult() {
        public void OnBackResult(Object result) {
            if ("finish".equals(result)) {
                NotificationApkUpdate.this.StopDownload();
                Map<String, String> msg = new HashMap();
                msg.put("code", String.valueOf(0));
                NotificationApkUpdate.sendU3DMessage(NotificationApkUpdate.FINISH_CALLBACK_MESSAGE, msg);
                NotificationApkUpdate.this.InstallApk();
                return;
            }
            UnityPlayer.UnitySendMessage(NotificationApkUpdate.UNITY_OBJECT_NAME, NotificationApkUpdate.UPDATE_CALLBACK_MESSAGE, String.valueOf(((Integer) result).intValue()));
        }
    };
    ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            NotificationApkUpdate.this.mIsBinded = false;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            NotificationApkUpdate.this.mBinder = (DownloadBinder) service;
            NotificationApkUpdate.this.mIsBinded = true;
            NotificationApkUpdate.this.mBinder.addCallback(NotificationApkUpdate.this.callback);
            NotificationApkUpdate.this.mBinder.start(NotificationApkUpdate.this.mDownloadURL, NotificationApkUpdate.this.mNotificationName, NotificationApkUpdate.this.mApkName, NotificationApkUpdate.this.mSavePath);
        }
    };
    private String mApkName;
    private DownloadBinder mBinder;
    private String mDownloadURL;
    private boolean mIsBinded = false;
    private String mNotificationName;
    private String mSavePath;

    public interface ICallbackResult {
        void OnBackResult(Object obj);
    }

    public static NotificationApkUpdate GetInstance() {
        if (sInstance == null) {
            sInstance = new NotificationApkUpdate();
        }
        return sInstance;
    }

    public int InstallApk() {
        File apkfile = new File(this.mSavePath + this.mApkName);
        if (!apkfile.exists()) {
            return -1;
        }
        Intent i = new Intent("android.intent.action.VIEW");
        i.setFlags(268435456);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        UnityPlayer.currentActivity.startActivity(i);
        return 0;
    }

    public void StartDownload(String downloadURL, String notification, String apkName) {
        this.mDownloadURL = downloadURL;
        this.mNotificationName = notification;
        this.mApkName = apkName;
        this.mSavePath = new StringBuilder(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath())).append("/").toString();
        Intent it = new Intent(UnityPlayer.currentActivity, DownloadService.class);
        UnityPlayer.currentActivity.startService(it);
        UnityPlayer.currentActivity.bindService(it, this.conn, 1);
    }

    public void StopDownload() {
        this.mBinder.cancel();
        this.mBinder.cancelNotification();
        if (this.mIsBinded) {
            UnityPlayer.currentActivity.unbindService(this.conn);
        }
        UnityPlayer.currentActivity.stopService(new Intent(UnityPlayer.currentActivity, DownloadService.class));
    }

    private static void sendU3DMessage(String methodName, Map<String, String> hashMap) {
        String param = "";
        if (hashMap != null) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (param.length() == 0) {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("%s{%s}", new Object[]{key, val})).toString();
                } else {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("&%s{%s}", new Object[]{key, val})).toString();
                }
            }
        }
        UnityPlayer.UnitySendMessage(UNITY_OBJECT_NAME, methodName, param);
    }
}
