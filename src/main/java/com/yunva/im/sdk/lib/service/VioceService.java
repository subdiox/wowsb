package com.yunva.im.sdk.lib.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import com.yunva.im.sdk.lib.YvLoginInit;
import com.yunva.im.sdk.lib.config.SystemConfigFactory;
import com.yunva.im.sdk.lib.dynamicload.utils.JarUtils;
import com.yunva.im.sdk.lib.utils.MyLog;
import com.yunva.im.sdk.lib.utils.VersionUtil;
import java.lang.reflect.Field;

public class VioceService extends Service {
    public static boolean isStarted = false;
    private static final byte[] syncKey = new byte[0];
    private final String TAG = "LibVioceService";
    private String mApkPackage;
    private Resources mBundleResource;
    private Object service;

    public IBinder onBind(Intent intent) {
        MyLog.d("LibVioceService", "onBind...");
        try {
            Object serviceImpl = getService();
            return (IBinder) serviceImpl.getClass().getDeclaredMethod("onBind", new Class[]{Service.class, Intent.class}).invoke(serviceImpl, new Object[]{this, intent});
        } catch (Exception e) {
            MyLog.e("LibVioceService", "dynamic load onBind failure.exception:" + e.getMessage());
            return null;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.d("LibVioceService", "onStartCommand...");
        isStarted = true;
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (YvLoginInit.context == null) {
            YvLoginInit.context = getApplicationContext();
        }
        Object serviceImpl = getService();
        try {
            MyLog.i("LibVioceService", "call onStartCommand");
            serviceImpl.getClass().getDeclaredMethod("onStartCommand", new Class[]{Service.class, Intent.class, Integer.TYPE, Integer.TYPE}).invoke(serviceImpl, new Object[]{this, intent, Integer.valueOf(flags), Integer.valueOf(startId)});
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e("LibVioceService", "dynamic load onStartCommand failure.exception:" + e.getMessage());
        }
        long time = intent.getLongExtra("time", 1800000);
        try {
            Intent bintent = new Intent(this, VioceService.class);
            bintent.putExtra("isAlarm", true);
            bintent.putExtra("time", time);
            ((AlarmManager) getSystemService("alarm")).set(0, System.currentTimeMillis() + time, PendingIntent.getService(this, 0, bintent, 0));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        MyLog.d("LibVioceService", "onCreate...");
        isStarted = false;
    }

    public void onDestroy() {
        super.onDestroy();
        MyLog.d("LibVioceService", "onDestroy...");
        try {
            Object serviceImpl = getService();
            serviceImpl.getClass().getDeclaredMethod("onDestroy", new Class[]{Service.class}).invoke(serviceImpl, new Object[]{this});
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e("LibVioceService", "dynamic load onDestroy failure.exception:" + e.getMessage());
        }
    }

    @SuppressLint({"NewApi"})
    public Object getService() {
        synchronized (syncKey) {
            if (this.service == null) {
                MyLog.d("LibVioceService", "service is null");
                if (SystemConfigFactory.getInstance().getSystemConfig() == null) {
                    SystemConfigFactory.getInstance().buildConfig(false);
                }
                if (SystemConfigFactory.getInstance().getSystemConfig() != null) {
                    try {
                        this.service = JarUtils.getDexClassLoader(getApplicationContext(), VersionUtil.DefaultVersionName, SystemConfigFactory.getInstance().getJarPath()).loadClass("com.yunva.atp.service.VioceService").newInstance();
                        MyLog.i("LibVioceService", "getService ok:" + this.service.getClass().getName());
                        this.mBundleResource = JarUtils.getBundleResource(this);
                        this.mApkPackage = JarUtils.getApkPackage(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyLog.e("LibVioceService", " getService   failure.exception:" + e.getMessage());
                    }
                }
            }
        }
        return this.service;
    }

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        getService();
        if (this.mBundleResource != null) {
            replaceContextResources(context);
        }
    }

    public void replaceContextResources(Context context) {
        try {
            Field field = context.getClass().getDeclaredField("mResources");
            field.setAccessible(true);
            field.set(context, this.mBundleResource);
            System.out.println("debug:repalceResources succ");
        } catch (Exception e) {
            System.out.println("debug:repalceResources error");
            e.printStackTrace();
        }
    }
}
