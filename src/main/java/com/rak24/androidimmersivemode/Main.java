package com.rak24.androidimmersivemode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.unity3d.player.UnityPlayer;

@TargetApi(21)
public class Main {
    static Main _INSTANCE = null;
    static BroadcastReceiver br;
    public static boolean gamePaused;
    static int lastHeightDiff = 0;
    static Application unityApp;

    class AnonymousClass2 implements Runnable {
        private final /* synthetic */ Activity val$act;

        AnonymousClass2(Activity activity) {
            this.val$act = activity;
        }

        public void run() {
            Main.ImmersiveMode(this.val$act.getWindow().getDecorView());
        }
    }

    public Main() {
        _INSTANCE = this;
    }

    public static Main instance() {
        if (_INSTANCE == null) {
            return new Main();
        }
        return _INSTANCE;
    }

    public void EnableImmersiveMode(Context currentContext) {
        if (VERSION.SDK_INT >= 19) {
            Activity activity = (Activity) currentContext;
            ImmersiveMode(activity.getWindow().getDecorView());
            SetUIChangeListener(activity.getWindow().getDecorView());
            unityApp = UnityPlayer.currentActivity.getApplication();
            unityApp.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                public void onActivityResumed(Activity activity) {
                    Main.ImmersiveMode(activity.getWindow().getDecorView());
                }

                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }

                public void onActivityDestroyed(Activity activity) {
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivityStopped(Activity activity) {
                }
            });
        }
    }

    public static void ImmersiveMode(View v) {
        if (VERSION.SDK_INT >= 19) {
            v.setSystemUiVisibility(7686);
        }
    }

    public static void ImmersiveModeFromCache(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            new Handler().postDelayed(new AnonymousClass2(activity), 700);
        }
    }

    public static void EnableAppPin(Activity unityActivity) {
        if (VERSION.SDK_INT >= 21) {
            unityActivity.startLockTask();
        }
    }

    public static void DisableAppPin(Activity unityActivity) {
        if (VERSION.SDK_INT >= 21) {
            unityActivity.stopLockTask();
        }
    }

    void SetUIChangeListener(View v) {
        final View unityView = v;
        v.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & 4) == 0) {
                    Main.ImmersiveMode(unityView);
                }
            }
        });
    }
}
