package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public final class zzabu extends Fragment implements zzabf {
    private static WeakHashMap<FragmentActivity, WeakReference<zzabu>> zzaCS = new WeakHashMap();
    private int zzJO = 0;
    private Map<String, zzabe> zzaCT = new ArrayMap();
    private Bundle zzaCU;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.internal.zzabu zza(android.support.v4.app.FragmentActivity r3) {
        /*
        r0 = zzaCS;
        r0 = r0.get(r3);
        r0 = (java.lang.ref.WeakReference) r0;
        if (r0 == 0) goto L_0x0013;
    L_0x000a:
        r0 = r0.get();
        r0 = (com.google.android.gms.internal.zzabu) r0;
        if (r0 == 0) goto L_0x0013;
    L_0x0012:
        return r0;
    L_0x0013:
        r0 = r3.getSupportFragmentManager();	 Catch:{ ClassCastException -> 0x0048 }
        r1 = "SupportLifecycleFragmentImpl";
        r0 = r0.findFragmentByTag(r1);	 Catch:{ ClassCastException -> 0x0048 }
        r0 = (com.google.android.gms.internal.zzabu) r0;	 Catch:{ ClassCastException -> 0x0048 }
        if (r0 == 0) goto L_0x0027;
    L_0x0021:
        r1 = r0.isRemoving();
        if (r1 == 0) goto L_0x003d;
    L_0x0027:
        r0 = new com.google.android.gms.internal.zzabu;
        r0.<init>();
        r1 = r3.getSupportFragmentManager();
        r1 = r1.beginTransaction();
        r2 = "SupportLifecycleFragmentImpl";
        r1 = r1.add(r0, r2);
        r1.commitAllowingStateLoss();
    L_0x003d:
        r1 = zzaCS;
        r2 = new java.lang.ref.WeakReference;
        r2.<init>(r0);
        r1.put(r3, r2);
        goto L_0x0012;
    L_0x0048:
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r2 = "Fragment with tag SupportLifecycleFragmentImpl is not a SupportLifecycleFragmentImpl";
        r1.<init>(r2, r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzabu.zza(android.support.v4.app.FragmentActivity):com.google.android.gms.internal.zzabu");
    }

    private void zzb(final String str, @NonNull final zzabe com_google_android_gms_internal_zzabe) {
        if (this.zzJO > 0) {
            new Handler(Looper.getMainLooper()).post(new Runnable(this) {
                final /* synthetic */ zzabu zzaDk;

                public void run() {
                    if (this.zzaDk.zzJO >= 1) {
                        com_google_android_gms_internal_zzabe.onCreate(this.zzaDk.zzaCU != null ? this.zzaDk.zzaCU.getBundle(str) : null);
                    }
                    if (this.zzaDk.zzJO >= 2) {
                        com_google_android_gms_internal_zzabe.onStart();
                    }
                    if (this.zzaDk.zzJO >= 3) {
                        com_google_android_gms_internal_zzabe.onStop();
                    }
                    if (this.zzaDk.zzJO >= 4) {
                        com_google_android_gms_internal_zzabe.onDestroy();
                    }
                }
            });
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (zzabe dump : this.zzaCT.values()) {
            dump.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (zzabe onActivityResult : this.zzaCT.values()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzJO = 1;
        this.zzaCU = bundle;
        for (Entry entry : this.zzaCT.entrySet()) {
            ((zzabe) entry.getValue()).onCreate(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.zzJO = 4;
        for (zzabe onDestroy : this.zzaCT.values()) {
            onDestroy.onDestroy();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            for (Entry entry : this.zzaCT.entrySet()) {
                Bundle bundle2 = new Bundle();
                ((zzabe) entry.getValue()).onSaveInstanceState(bundle2);
                bundle.putBundle((String) entry.getKey(), bundle2);
            }
        }
    }

    public void onStart() {
        super.onStart();
        this.zzJO = 2;
        for (zzabe onStart : this.zzaCT.values()) {
            onStart.onStart();
        }
    }

    public void onStop() {
        super.onStop();
        this.zzJO = 3;
        for (zzabe onStop : this.zzaCT.values()) {
            onStop.onStop();
        }
    }

    public <T extends zzabe> T zza(String str, Class<T> cls) {
        return (zzabe) cls.cast(this.zzaCT.get(str));
    }

    public void zza(String str, @NonNull zzabe com_google_android_gms_internal_zzabe) {
        if (this.zzaCT.containsKey(str)) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(str).length() + 59).append("LifecycleCallback with tag ").append(str).append(" already added to this fragment.").toString());
        }
        this.zzaCT.put(str, com_google_android_gms_internal_zzabe);
        zzb(str, com_google_android_gms_internal_zzabe);
    }

    public /* synthetic */ Activity zzwV() {
        return zzwZ();
    }

    public FragmentActivity zzwZ() {
        return getActivity();
    }
}
