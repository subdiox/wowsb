package com.appsflyer;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.lang.ref.WeakReference;

@RequiresApi(api = 14)
class d implements ActivityLifecycleCallbacks {
    private static d instance;
    private boolean foreground = false;
    private a listener = null;
    private boolean paused = true;

    interface a {
        void onBecameBackground(WeakReference<Activity> weakReference);

        void onBecameForeground(Activity activity);
    }

    d() {
    }

    public static d init(Application application) {
        if (instance == null) {
            instance = new d();
            if (VERSION.SDK_INT >= 14) {
                application.registerActivityLifecycleCallbacks(instance);
            }
        }
        return instance;
    }

    public static d getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("Foreground is not initialised - invoke at least once with parameter init/get");
    }

    public void registerListener(a listener) {
        this.listener = listener;
    }

    public void onActivityResumed(Activity activity) {
        boolean z = false;
        this.paused = false;
        if (!this.foreground) {
            z = true;
        }
        this.foreground = true;
        if (z) {
            try {
                this.listener.onBecameForeground(activity);
            } catch (Throwable e) {
                a.afLogE("Listener threw exception! ", e);
            }
        }
    }

    public void onActivityPaused(final Activity activity) {
        this.paused = true;
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (Throwable e) {
                    a.afLogE("Sleeping attempt failed (essential for background state verification)\n", e);
                }
                if (d.this.foreground && d.this.paused) {
                    d.this.foreground = false;
                    try {
                        WeakReference weakReference = new WeakReference(activity);
                        d.this.listener.onBecameBackground(weakReference);
                        weakReference.clear();
                    } catch (Throwable e2) {
                        a.afLogE("Listener threw exception! ", e2);
                        cancel(true);
                    }
                }
                return null;
            }
        }.execute(new Void[0]);
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }
}
