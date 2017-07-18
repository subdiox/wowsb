package com.yunva.im.sdk.lib;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.unity3d.player.UnityPlayer;
import com.yunva.im.sdk.lib.config.SystemConfigFactory;
import com.yunva.im.sdk.lib.location.LBSUtil;
import com.yunva.im.sdk.lib.location.LbsInfoReturnListener;
import com.yunva.im.sdk.lib.service.VioceService;
import com.yunva.im.sdk.lib.utils.MyLog;
import org.cocos2dx.lib.Cocos2dxActivity;

public class YvLoginInit {
    private static final String TAG = "YvLoginInit";
    public static Context context;
    private static String mAppId;
    private static Application mApplication;
    private LBSUtil lbsUtil;
    private GetLbsInfoReturnListener mGetLbsInfoReturnListener = new GetLbsInfoReturnListener();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(YvLoginInit.context, "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.", 0).show();
                    return;
                case 2:
                    YvLoginInit.YvImDoCallBack();
                    return;
                case 3:
                    YvLoginInit.this.lbsUtil.getLocationByGPS();
                    return;
                case 4:
                    YvLoginInit.this.lbsUtil.getLocationByWiFi();
                    return;
                case 5:
                    YvLoginInit.this.lbsUtil.getLocationByCell();
                    return;
                case 8:
                    YvLoginInit.this.lbsUtil.getLocationByIP();
                    return;
                case 16:
                    YvLoginInit.this.lbsUtil.getLocationByBlueTooth();
                    return;
                case 32:
                    YvLoginInit.this.lbsUtil.getLocationByWifiCell();
                    return;
                case 64:
                    YvLoginInit.this.lbsUtil.getLocationByAGPS();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean orTest = false;

    class GetLbsInfoReturnListener implements LbsInfoReturnListener {
        GetLbsInfoReturnListener() {
        }

        public void getLbsInfo(int type, String lbsInfo) {
            YvLoginInit.this.YvImUpdateGps(0, type, lbsInfo);
        }

        public void returnError(int code, int type) {
            YvLoginInit.this.YvImUpdateGps(code, type, "");
        }
    }

    public static native void YvImDoCallBack();

    public native void YvImUpdateGps(int i, int i2, String str);

    public static boolean initApplicationOnCreate(Application application, String appId) {
        return initApplicationOnCreate(application, appId, 1800000);
    }

    public static boolean initApplicationOnCreate(final Application application, final String appId, long time) {
        SystemConfigFactory.getInstance().buildConfig(false);
        Intent intent = new Intent(application, VioceService.class);
        intent.putExtra("appId", appId);
        intent.putExtra("time", time);
        application.startService(intent);
        MyLog.e(TAG, "startService_initApplicationOnCreate");
        mAppId = appId;
        mApplication = application;
        context = application;
        postToMain(new Runnable() {
            public void run() {
                YvLoginInit.loadYayaCoreLibIfIncluded(application, appId);
            }
        });
        return true;
    }

    public static void release() {
    }

    private static void postToMain(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    private static void loadYayaCoreLibIfIncluded(Context c, String appId) {
        Class<?> clazz = getCLib();
        if (clazz != null) {
            MyLog.d(TAG, "initCLib");
            initCLib(clazz, c, appId);
        }
    }

    private static Class<?> getCLib() {
        try {
            return Class.forName("com.yaya.sdk.core.CoreLib");
        } catch (ClassNotFoundException e) {
            MyLog.d(TAG, "CLib not found");
            return null;
        }
    }

    private static void initCLib(Class<?> clibClazz, Context context, String appId) {
        try {
            try {
                clibClazz.getMethod("init", new Class[]{Context.class, String.class}).invoke(clibClazz, new Object[]{context, appId});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e2) {
            MyLog.w(TAG, "init method not found !");
        }
    }

    public int YvLoginCallBack(long appid, long yunvaid) {
        if (mApplication == null) {
            throw new RuntimeException("the application is null. Please create the Application class, and call the method for initApplicationOnCreate(Application application, String appId) ");
        } else if (mAppId == null || !mAppId.equals(appid + "")) {
            throw new RuntimeException("the initApplicationOnCreate appId is null, or it and the init sdk appId is not the same appId.");
        } else {
            int r_c = initc();
            if (r_c == 0) {
                throw new RuntimeException("com.yunva.im.sdk.lib.YvLoginInit.context  is null .Please initialize  ");
            }
            readMetaDataFromService(context);
            if (!VioceService.isStarted) {
                onCreateVoiceService(Long.valueOf(appid), Long.valueOf(yunvaid), this.orTest);
            }
            if (this.orTest) {
                this.mHandler.sendEmptyMessage(1);
            }
            return r_c;
        }
    }

    public void YvInitCallBack(long appid, boolean test) {
        this.orTest = test;
        if (test) {
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
            Log.e("System.err", "警告: 当前yunva-IMSDK 运行环境为测试环境，请在应用上线时更改为正式环境！！.");
        }
    }

    private int initc() {
        if (context != null) {
            return 1;
        }
        try {
            if (UnityPlayer.currentActivity != null) {
                context = UnityPlayer.currentActivity;
                Log.v("dalvikvm", " This is Unity ...");
                return 1;
            } else if (context == null) {
                return 0;
            } else {
                return 1;
            }
        } catch (NoClassDefFoundError e) {
            try {
                if (Cocos2dxActivity.getContext() != null) {
                    context = Cocos2dxActivity.getContext();
                    Log.v("dalvikvm", " This is Cocos2dx ...");
                    return 1;
                } else if (context == null) {
                    return 0;
                } else {
                    return 1;
                }
            } catch (NoClassDefFoundError e2) {
                try {
                    if (context == null) {
                        return 0;
                    }
                    Log.v("dalvikvm", " This is Android  ...");
                    return 1;
                } catch (Error e3) {
                    if (context == null) {
                        return 0;
                    }
                    Log.v("dalvikvm", " This is unknown engine ...");
                    return 1;
                }
            }
        }
    }

    private void onCreateVoiceService(Long appId, Long userId, boolean isTest) {
        try {
            SystemConfigFactory.getInstance().buildConfig(isTest);
            Intent intent = new Intent(context, VioceService.class);
            intent.putExtra("appId", appId + "");
            context.startService(intent);
            MyLog.e(TAG, "startService_onCreateVoiceService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMetaDataFromService(Context context) {
        try {
            context.getPackageManager().getServiceInfo(new ComponentName(context, VioceService.class), 128);
        } catch (NameNotFoundException e) {
            throw new RuntimeException("not found services 配置错误：AndroidManifest.xml 配置文件 缺少配置 services ---> com.yunva.im.sdk.lib.service.VioceService 请配置后重试！！");
        }
    }

    public void YvImDispatchAsync() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.mHandler.sendEmptyMessage(2);
        } else {
            YvImDoCallBack();
        }
    }

    public int YvImGetGpsCallBack(int locate_gps, int locate_wifi, int locate_cell, int locate_network, int locate_bluetooth) {
        this.lbsUtil = LBSUtil.getInstance(context, this.mGetLbsInfoReturnListener);
        if ((locate_gps & 1) == 1) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(3);
            } else {
                this.lbsUtil.getLocationByGPS();
            }
        }
        if ((locate_gps & 2) == 2) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(4);
            } else {
                this.lbsUtil.getLocationByWiFi();
            }
        }
        if ((locate_gps & 4) == 4) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(5);
            } else {
                this.lbsUtil.getLocationByCell();
            }
        }
        if ((locate_gps & 8) == 8) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(8);
            } else {
                this.lbsUtil.getLocationByIP();
            }
        }
        if ((locate_gps & 16) == 16) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(16);
            } else {
                this.lbsUtil.getLocationByBlueTooth();
            }
        }
        if ((locate_gps & 32) == 32) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(32);
            } else {
                this.lbsUtil.getLocationByWifiCell();
            }
        }
        if ((locate_gps & 64) == 64) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mHandler.sendEmptyMessage(64);
            } else {
                this.lbsUtil.getLocationByAGPS();
            }
        }
        return 0;
    }
}
