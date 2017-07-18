package com.lthz.update;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import com.lthz.update.NotificationApkUpdate.ICallbackResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.wargaming.wows.blitz.R;

public class DownloadService extends Service {
    private static final int NOTIFY_ID = 0;
    private static String mSaveFileName;
    private DownloadBinder binder;
    private ICallbackResult callback;
    private Thread downLoadThread;
    private int lastRate = 0;
    private String mApkURL;
    private Builder mBuilder;
    private boolean mCanceled;
    private Context mContext = this;
    private Handler mHandler = new Handler() {
        @SuppressLint({"NewApi"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    DownloadService.this.mNotificationManager.cancel(0);
                    DownloadService.this.installApk();
                    return;
                case 1:
                    if (msg.arg1 < 100) {
                        DownloadService.this.mNotification = DownloadService.this.mBuilder.setContentText(" 正在下载更新 : " + DownloadService.this.progress + "%").setProgress(100, DownloadService.this.progress, false).build();
                    } else {
                        Intent intent = new Intent(DownloadService.this.mContext, NotificationApkUpdate.class);
                        intent.putExtra("completed", "yes");
                        PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this.mContext, 0, intent, 134217728);
                        DownloadService.this.mNotification = DownloadService.this.mBuilder.setContentTitle("下载完成").setContentText("下载完成").setOngoing(false).build();
                        DownloadService.this.mNotification.contentIntent = contentIntent;
                        DownloadService.this.serviceIsDestroy = true;
                        DownloadService.this.stopSelf();
                    }
                    DownloadService.this.mNotificationManager.notify(0, DownloadService.this.mNotification);
                    return;
                case 2:
                    DownloadService.this.mNotificationManager.cancel(0);
                    return;
                default:
                    return;
            }
        }
    };
    Notification mNotification;
    private NotificationManager mNotificationManager;
    private String mSavePath;
    private Runnable mdownApkRunnable = new Runnable() {
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(DownloadService.this.mApkURL).openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(DownloadService.this.mSavePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(DownloadService.mSaveFileName));
                int count = 0;
                byte[] buf = new byte[1048576];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    DownloadService.this.progress = (int) ((((float) count) / ((float) length)) * 100.0f);
                    Message msg = DownloadService.this.mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = DownloadService.this.progress;
                    if (DownloadService.this.progress >= DownloadService.this.lastRate + 1) {
                        DownloadService.this.mHandler.sendMessage(msg);
                        DownloadService.this.lastRate = DownloadService.this.progress;
                        if (DownloadService.this.callback != null) {
                            DownloadService.this.callback.OnBackResult(Integer.valueOf(DownloadService.this.progress));
                        }
                    }
                    if (numread <= 0) {
                        DownloadService.this.mHandler.sendEmptyMessage(0);
                        DownloadService.this.mCanceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!DownloadService.this.mCanceled);
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    };
    private int progress;
    private boolean serviceIsDestroy = false;

    public class DownloadBinder extends Binder {
        public void start(String downloadURL, String notification, String apkFileName, String savePath) {
            if (DownloadService.this.downLoadThread == null || !DownloadService.this.downLoadThread.isAlive()) {
                DownloadService.this.mSavePath = savePath;
                DownloadService.mSaveFileName = new StringBuilder(String.valueOf(DownloadService.this.mSavePath)).append(apkFileName).toString();
                DownloadService.this.mApkURL = downloadURL;
                DownloadService.this.progress = 0;
                DownloadService.this.setUpNotification(notification);
                new Thread() {
                    public void run() {
                        DownloadService.this.startDownload();
                    }
                }.start();
            }
        }

        public void cancel() {
            DownloadService.this.mCanceled = true;
        }

        public int getProgress() {
            return DownloadService.this.progress;
        }

        public boolean isCanceled() {
            return DownloadService.this.mCanceled;
        }

        public boolean serviceIsDestroy() {
            return DownloadService.this.serviceIsDestroy;
        }

        public void cancelNotification() {
            DownloadService.this.mHandler.sendEmptyMessage(2);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadService.this.callback = callback;
        }
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public void onCreate() {
        super.onCreate();
        this.binder = new DownloadBinder();
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        stopForeground(true);
    }

    private void startDownload() {
        this.mCanceled = false;
        downloadApk();
    }

    @SuppressLint({"NewApi"})
    private void setUpNotification(String notification) {
        this.mBuilder = new Builder(this.mContext).setSmallIcon(R.drawable.abc_ab_share_pack_mtrl_alpha).setContentText("").setContentTitle(notification).setOngoing(true).setProgress(100, 0, false);
        this.mNotification = this.mBuilder.build();
        this.mNotification.contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationApkUpdate.class), 134217728);
        this.mNotificationManager.notify(0, this.mNotification);
    }

    private void downloadApk() {
        this.downLoadThread = new Thread(this.mdownApkRunnable);
        this.downLoadThread.start();
    }

    private void installApk() {
        this.callback.OnBackResult("finish");
    }
}
