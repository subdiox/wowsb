package com.yunva.im.sdk.lib.dynamicload.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.yunva.im.sdk.lib.YvLoginInit;
import com.yunva.im.sdk.lib.config.SystemConfigFactory;
import com.yunva.im.sdk.lib.utils.MyLog;
import com.yunva.im.sdk.lib.utils.PhoneInfoUtils;
import com.yunva.im.sdk.lib.utils.VersionUtil;
import dalvik.system.DexClassLoader;
import java.io.File;

public class JarUtils {
    private static final boolean IS_OVER_WRITTEN = true;
    private static final String TAG = JarUtils.class.getSimpleName();
    private static DexClassLoader dexClassLoader = null;

    public static DexClassLoader getDexClassLoader(Context context, String jarFile, String jarOutputPath) {
        if (dexClassLoader == null) {
            initialJarEnvironment(context, jarFile, jarOutputPath);
            jarOutputPath = getApkPath(context);
            if (PhoneInfoUtils.getSystemVersion() >= 14) {
                dexClassLoader = loadJarLibrary(jarOutputPath + File.separator + jarFile, context.getDir("dex", 0).getAbsolutePath());
            } else {
                dexClassLoader = loadJarLibrary(jarOutputPath + File.separator + jarFile, jarOutputPath);
            }
        }
        return dexClassLoader;
    }

    private static String getApkPath(Context context) {
        if (PhoneInfoUtils.checkWriteExPermision(context)) {
            return SystemConfigFactory.getInstance().getJarPath();
        }
        return YvLoginInit.context.getFilesDir().getAbsolutePath();
    }

    private static File getApkFile(Context context, String jarFile) {
        return new File(getApkPath(context), jarFile);
    }

    public static void initialJarEnvironment(Context context, String jarFile, String jarOutputPath) {
        if (PhoneInfoUtils.checkWriteExPermision(context)) {
            MyLog.d("", "initialJarEnvironment-1-to sd");
            AssetsUtils.copyAssetsToSD(context, jarFile, jarOutputPath, true);
            return;
        }
        MyLog.d("", "initialJarEnvironment-2-to data");
        AssetsUtils.copyAssetsToPhone(context, jarFile);
    }

    public static synchronized DexClassLoader loadJarLibrary(String jarPath, String jarOutputPath) {
        DexClassLoader dexClassLoader = null;
        synchronized (JarUtils.class) {
            File file = new File(jarPath);
            if (file.exists()) {
                dexClassLoader = new DexClassLoader(file.toString(), jarOutputPath, null, ClassLoader.getSystemClassLoader().getParent());
            }
        }
        return dexClassLoader;
    }

    private static AssetManager createAssetManager(String apkPath) {
        AssetManager assetManager;
        try {
            assetManager = (AssetManager) AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(assetManager, new Object[]{apkPath});
            return assetManager;
        } catch (Throwable th) {
            System.out.println("debug:createAssetManager :" + th.getMessage());
            th.printStackTrace();
            return null;
        }
    }

    public static Resources getBundleResource(Context context) {
        File apkFile = getApkFile(context, VersionUtil.DefaultVersionName);
        System.out.println("debug:apkPath = " + apkFile.toString() + ",exists=" + apkFile.exists());
        return new Resources(createAssetManager(apkFile.toString()), context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    public static String getApkPackage(Context context) {
        PackageInfo info = context.getPackageManager().getPackageArchiveInfo(getApkFile(context, VersionUtil.DefaultVersionName).toString(), 1);
        if (info != null) {
            return info.packageName;
        }
        return null;
    }

    public static Boolean isJarExitsInSDCardorFileDir(String jarName, String jarOutputPath) {
        File mWorkingPath = new File(jarOutputPath);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }
        try {
            if (new File(mWorkingPath, jarName).exists()) {
                return Boolean.valueOf(true);
            }
        } catch (Exception e) {
            MyLog.e(TAG, e.getStackTrace().toString());
        }
        return Boolean.valueOf(false);
    }
}
