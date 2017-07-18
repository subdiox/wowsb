package com.tencent.bugly.unity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* compiled from: BUGLY */
public class UnityAgent {
    private static UnityAgent a = null;
    public static String sdkPackageName = "com.tencent.bugly";
    private Context b = null;
    private Handler c = null;
    private boolean d = false;

    /* compiled from: BUGLY */
    private static class a {
        public static Object a(String str, String str2, Object obj) {
            try {
                Field declaredField = Class.forName(str).getDeclaredField(str2);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
                return null;
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
                return null;
            }
        }

        public static Object a(String str, String str2, Object[] objArr, Class<?>... clsArr) {
            Object obj = null;
            try {
                Method declaredMethod = Class.forName(str).getDeclaredMethod(str2, clsArr);
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke(null, objArr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            return obj;
        }

        public static Object a(String str, Object[] objArr, Class<?>... clsArr) {
            try {
                Class cls = Class.forName(str);
                if (objArr == null) {
                    return cls.newInstance();
                }
                return cls.getConstructor(clsArr).newInstance(objArr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
                return null;
            } catch (InstantiationException e3) {
                e3.printStackTrace();
                return null;
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
                return null;
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
                return null;
            } catch (Exception e6) {
                e6.printStackTrace();
                return null;
            }
        }
    }

    public static synchronized UnityAgent getInstance() {
        UnityAgent unityAgent;
        synchronized (UnityAgent.class) {
            if (a == null) {
                a = new UnityAgent();
            }
            unityAgent = a;
        }
        return unityAgent;
    }

    private UnityAgent() {
        applicationContext();
        try {
            this.c = new Handler(Looper.getMainLooper());
        } catch (Exception e) {
            printLog(2, "Fail to get the main looper handler");
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return "1.3.1";
    }

    public Context applicationContext() {
        if (this.b == null) {
            Activity currentActivity = currentActivity();
            if (currentActivity != null) {
                this.b = currentActivity.getApplicationContext();
            }
        }
        return this.b;
    }

    public Activity currentActivity() {
        try {
            Object a = a.a("com.unity3d.player.UnityPlayer", "currentActivity", null);
            if (a != null && (a instanceof Activity)) {
                return (Activity) a;
            }
        } catch (Exception e) {
            Log.w("UnityAgent", "Failed to get the current activity from UnityPlayer");
            e.printStackTrace();
        }
        return null;
    }

    public void setSDKPackagePrefixName(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            sdkPackageName = packageName;
        }
    }

    public void sendUnityMessage(String arg0, String arg1, String arg2) {
        try {
            a.a("com.unity3d.player.UnityPlayer", "UnitySendMessage", new Object[]{arg0, arg1, arg2}, String.class, String.class, String.class);
        } catch (Exception e) {
            Log.w("UnityAgent", "Fail to send message to UnityPlayer by UnitySendMessage");
            e.printStackTrace();
        }
    }

    public void setLogEnable(boolean enable) {
        this.d = enable;
    }

    public void initSDK(final String appId) {
        if (TextUtils.isEmpty(appId)) {
            printLog(2, "Please input valid app id from bugly.");
            return;
        }
        final boolean z = this.d;
        final Activity currentActivity = currentActivity();
        if (currentActivity != null) {
            currentActivity.runOnUiThread(new Runnable(this) {
                final /* synthetic */ UnityAgent d;

                public void run() {
                    Log.i("UnityAgent", String.format("init the sdk with '%s'", new Object[]{appId}));
                    UnityAgent.b(currentActivity.getApplicationContext(), appId, z, null, null, null, -1);
                }
            });
        } else {
            printLog(3, "Fail to init sdk");
        }
    }

    public void initWithConfiguration(String appId, String channel, String version, String userId, long delay) {
        if (TextUtils.isEmpty(appId)) {
            printLog(2, "Please input valid app id from bugly.");
            return;
        }
        final boolean z = this.d;
        final Activity currentActivity = currentActivity();
        if (currentActivity != null) {
            final String str = appId;
            final String str2 = channel;
            final String str3 = version;
            final String str4 = userId;
            final long j = delay;
            currentActivity.runOnUiThread(new Runnable(this) {
                final /* synthetic */ UnityAgent h;

                public void run() {
                    Log.i("UnityAgent", String.format("init the sdk with '%s'", new Object[]{str}));
                    UnityAgent.b(currentActivity.getApplicationContext(), str, z, str2, str3, str4, j);
                }
            });
            return;
        }
        printLog(3, "Fail to init sdk");
    }

    private static void b(Context context, String str, boolean z, String str2, String str3, String str4, long j) {
        if (context == null || TextUtils.isEmpty(str)) {
            Log.w("UnityAgent", "Fail to init the crash report");
            return;
        }
        Object obj;
        if (a(context, str2, str3, j) != null) {
            Class cls;
            try {
                cls = Class.forName(b("crashreport.CrashReport$UserStrategy"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                cls = null;
            } catch (Exception e2) {
                e2.printStackTrace();
                cls = null;
            }
            if (cls != null) {
                a.a(b("crashreport.CrashReport"), "initCrashReport", new Object[]{context, str, Boolean.valueOf(z), r3}, Context.class, String.class, Boolean.TYPE, cls);
                obj = 1;
                if (obj == null) {
                    a.a(b("crashreport.CrashReport"), "initCrashReport", new Object[]{context, str, Boolean.valueOf(z)}, Context.class, String.class, Boolean.TYPE);
                }
                if (!TextUtils.isEmpty(str4)) {
                    a.a(b("crashreport.CrashReport"), "setUserId", new Object[]{str4}, String.class);
                }
            }
        }
        obj = null;
        if (obj == null) {
            a.a(b("crashreport.CrashReport"), "initCrashReport", new Object[]{context, str, Boolean.valueOf(z)}, Context.class, String.class, Boolean.TYPE);
        }
        if (!TextUtils.isEmpty(str4)) {
            a.a(b("crashreport.CrashReport"), "setUserId", new Object[]{str4}, String.class);
        }
    }

    private static Object a(Context context, String str, String str2, long j) {
        if (context == null || (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2))) {
            return null;
        }
        Object a = a.a(b("crashreport.CrashReport$UserStrategy"), new Object[]{context}, Context.class);
        if (a != null) {
            Class cls = a.getClass();
            try {
                cls.getDeclaredMethod("setAppChannel", new Class[]{String.class}).invoke(a, new Object[]{str});
                cls.getDeclaredMethod("setAppVersion", new Class[]{String.class}).invoke(a, new Object[]{str2});
                cls.getDeclaredMethod("setAppReportDelay", new Class[]{Long.TYPE}).invoke(a, new Object[]{Long.valueOf(j)});
                return a;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        return null;
    }

    public void printLog(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.startsWith("<Log>")) {
                printLog(1, msg);
            } else if (msg.startsWith("<LogDebug>")) {
                printLog(0, msg);
            } else if (msg.startsWith("<LogInfo>")) {
                printLog(1, msg);
            } else if (msg.startsWith("<LogWarning>")) {
                printLog(2, msg);
            } else if (msg.startsWith("<LogAssert>")) {
                printLog(2, msg);
            } else if (msg.startsWith("<LogError>")) {
                printLog(3, msg);
            } else if (msg.startsWith("<LogException>")) {
                printLog(3, msg);
            } else {
                printLog(0, msg);
            }
        }
    }

    public void printLog(int level, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (level < 0) {
                Log.d("UnityAgent", msg);
            }
            if (level == 0) {
                a("d", msg);
            }
            if (level == 1) {
                a("i", msg);
            }
            if (level == 2) {
                a("w", msg);
            }
            if (level >= 3) {
                a("e", msg);
            }
        }
    }

    private void a(String str, String str2) {
        a.a(b("crashreport.BuglyLog"), str, new Object[]{"", str2}, String.class, String.class);
    }

    public void setSdkConfig(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            Context applicationContext = applicationContext();
            String b = b("crashreport.CrashReport");
            String str = "putSdkData";
            Method a = a(b);
            if (a != null) {
                a.setAccessible(true);
                try {
                    a.invoke(null, new Object[]{applicationContext, "SDK_" + key, value});
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
            a.a(b, str, new Object[]{applicationContext, "SDK_" + key, value}, Context.class, String.class, String.class);
        }
    }

    private Method a(String str) {
        try {
            Method[] declaredMethods = Class.forName(str).getDeclaredMethods();
            if (declaredMethods != null && declaredMethods.length > 0) {
                for (Method method : declaredMethods) {
                    String toGenericString = method.toGenericString();
                    if (toGenericString.startsWith(String.format("private static void %s.", new Object[]{str})) && toGenericString.endsWith("(android.content.Context,java.lang.String,java.lang.String)")) {
                        break;
                    }
                }
            }
            Method method2 = null;
            return method2;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void setUserId(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            a.a(b("crashreport.CrashReport"), "setUserId", new Object[]{userId}, String.class);
        }
    }

    public void setScene(int sceneId) {
        if (applicationContext() != null) {
            a.a(b("crashreport.CrashReport"), "setUserSceneTag", new Object[]{applicationContext(), Integer.valueOf(sceneId)}, Context.class, Integer.TYPE);
        }
    }

    public void addSceneValue(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && applicationContext() != null) {
            a.a(b("crashreport.CrashReport"), "putUserData", new Object[]{applicationContext(), key, value}, Context.class, String.class, String.class);
        }
    }

    public void removeSceneValue(String key) {
        if (!TextUtils.isEmpty(key) && applicationContext() != null) {
            a.a(b("crashreport.CrashReport"), "removeUserData", new Object[]{applicationContext(), key}, Context.class, String.class);
        }
    }

    public void traceException(String type, String message, String stacks, boolean autoExit) {
        a.a(b("crashreport.inner.InnerAPI"), "postU3dCrashAsync", new Object[]{type, message, stacks}, String.class, String.class, String.class);
        if (autoExit) {
            a(3000);
        }
    }

    private void a(long j) {
        long max = Math.max(0, j);
        if (this.c != null) {
            this.c.postDelayed(new Runnable(this) {
                final /* synthetic */ UnityAgent a;

                {
                    this.a = r1;
                }

                public void run() {
                    this.a.exitApplication();
                }
            }, max);
            return;
        }
        try {
            Thread.sleep(max);
            exitApplication();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exitApplication() {
        printLog(2, String.format("Exit application by kill process[%d]", new Object[]{Integer.valueOf(Process.myPid())}));
        Process.killProcess(r0);
    }

    private static String b(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        if (sdkPackageName == null) {
            sdkPackageName = "com.tencent.bugly";
        }
        stringBuilder.append(sdkPackageName);
        stringBuilder.append(".");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}
