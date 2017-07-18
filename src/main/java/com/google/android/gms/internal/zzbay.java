package com.google.android.gms.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.util.zzt;
import com.google.android.gms.common.util.zzw;
import com.google.android.gms.common.util.zzz;

public class zzbay {
    private static boolean DEBUG = false;
    private static String TAG = "WakeLock";
    private static String zzbEv = "*gcore*:";
    private final Context mContext;
    private final String zzaHF;
    private final String zzaHH;
    private int zzbEA;
    private int zzbEB;
    private final WakeLock zzbEw;
    private final int zzbEx;
    private final String zzbEy;
    private boolean zzbEz;
    private WorkSource zzbjm;

    public zzbay(Context context, int i, String str) {
        this(context, i, str, null, context == null ? null : context.getPackageName());
    }

    @SuppressLint({"UnwrappedWakeLock"})
    public zzbay(Context context, int i, String str, String str2, String str3) {
        this(context, i, str, str2, str3, null);
    }

    @SuppressLint({"UnwrappedWakeLock"})
    public zzbay(Context context, int i, String str, String str2, String str3, String str4) {
        this.zzbEz = true;
        zzac.zzh(str, "Wake lock name can NOT be empty");
        this.zzbEx = i;
        this.zzbEy = str2;
        this.zzaHH = str4;
        this.mContext = context.getApplicationContext();
        if ("com.google.android.gms".equals(context.getPackageName())) {
            this.zzaHF = str;
        } else {
            String valueOf = String.valueOf(zzbEv);
            String valueOf2 = String.valueOf(str);
            this.zzaHF = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        }
        this.zzbEw = ((PowerManager) context.getSystemService("power")).newWakeLock(i, str);
        if (zzz.zzbf(this.mContext)) {
            if (zzw.zzdz(str3)) {
                str3 = context.getPackageName();
            }
            this.zzbjm = zzz.zzF(context, str3);
            zzc(this.zzbjm);
        }
    }

    private void zzd(WorkSource workSource) {
        try {
            this.zzbEw.setWorkSource(workSource);
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, e.toString());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzgM(java.lang.String r10) {
        /*
        r9 = this;
        r0 = r9.zzgN(r10);
        r5 = r9.zzo(r10, r0);
        monitor-enter(r9);
        r1 = r9.zzbEz;	 Catch:{ all -> 0x0045 }
        if (r1 == 0) goto L_0x0017;
    L_0x000d:
        r1 = r9.zzbEA;	 Catch:{ all -> 0x0045 }
        r1 = r1 + -1;
        r9.zzbEA = r1;	 Catch:{ all -> 0x0045 }
        if (r1 == 0) goto L_0x0020;
    L_0x0015:
        if (r0 != 0) goto L_0x0020;
    L_0x0017:
        r0 = r9.zzbEz;	 Catch:{ all -> 0x0045 }
        if (r0 != 0) goto L_0x0043;
    L_0x001b:
        r0 = r9.zzbEB;	 Catch:{ all -> 0x0045 }
        r1 = 1;
        if (r0 != r1) goto L_0x0043;
    L_0x0020:
        r0 = com.google.android.gms.common.stats.zze.zzyX();	 Catch:{ all -> 0x0045 }
        r1 = r9.mContext;	 Catch:{ all -> 0x0045 }
        r2 = r9.zzbEw;	 Catch:{ all -> 0x0045 }
        r2 = com.google.android.gms.common.stats.zzc.zza(r2, r5);	 Catch:{ all -> 0x0045 }
        r3 = 8;
        r4 = r9.zzaHF;	 Catch:{ all -> 0x0045 }
        r6 = r9.zzaHH;	 Catch:{ all -> 0x0045 }
        r7 = r9.zzbEx;	 Catch:{ all -> 0x0045 }
        r8 = r9.zzbjm;	 Catch:{ all -> 0x0045 }
        r8 = com.google.android.gms.common.util.zzz.zzb(r8);	 Catch:{ all -> 0x0045 }
        r0.zza(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ all -> 0x0045 }
        r0 = r9.zzbEB;	 Catch:{ all -> 0x0045 }
        r0 = r0 + -1;
        r9.zzbEB = r0;	 Catch:{ all -> 0x0045 }
    L_0x0043:
        monitor-exit(r9);	 Catch:{ all -> 0x0045 }
        return;
    L_0x0045:
        r0 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0045 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbay.zzgM(java.lang.String):void");
    }

    private boolean zzgN(String str) {
        return (TextUtils.isEmpty(str) || str.equals(this.zzbEy)) ? false : true;
    }

    private String zzo(String str, boolean z) {
        return this.zzbEz ? z ? str : this.zzbEy : this.zzbEy;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzo(java.lang.String r13, long r14) {
        /*
        r12 = this;
        r0 = r12.zzgN(r13);
        r6 = r12.zzo(r13, r0);
        monitor-enter(r12);
        r1 = r12.zzbEz;	 Catch:{ all -> 0x0044 }
        if (r1 == 0) goto L_0x0017;
    L_0x000d:
        r1 = r12.zzbEA;	 Catch:{ all -> 0x0044 }
        r2 = r1 + 1;
        r12.zzbEA = r2;	 Catch:{ all -> 0x0044 }
        if (r1 == 0) goto L_0x001f;
    L_0x0015:
        if (r0 != 0) goto L_0x001f;
    L_0x0017:
        r0 = r12.zzbEz;	 Catch:{ all -> 0x0044 }
        if (r0 != 0) goto L_0x0042;
    L_0x001b:
        r0 = r12.zzbEB;	 Catch:{ all -> 0x0044 }
        if (r0 != 0) goto L_0x0042;
    L_0x001f:
        r1 = com.google.android.gms.common.stats.zze.zzyX();	 Catch:{ all -> 0x0044 }
        r2 = r12.mContext;	 Catch:{ all -> 0x0044 }
        r0 = r12.zzbEw;	 Catch:{ all -> 0x0044 }
        r3 = com.google.android.gms.common.stats.zzc.zza(r0, r6);	 Catch:{ all -> 0x0044 }
        r4 = 7;
        r5 = r12.zzaHF;	 Catch:{ all -> 0x0044 }
        r7 = r12.zzaHH;	 Catch:{ all -> 0x0044 }
        r8 = r12.zzbEx;	 Catch:{ all -> 0x0044 }
        r0 = r12.zzbjm;	 Catch:{ all -> 0x0044 }
        r9 = com.google.android.gms.common.util.zzz.zzb(r0);	 Catch:{ all -> 0x0044 }
        r10 = r14;
        r1.zza(r2, r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ all -> 0x0044 }
        r0 = r12.zzbEB;	 Catch:{ all -> 0x0044 }
        r0 = r0 + 1;
        r12.zzbEB = r0;	 Catch:{ all -> 0x0044 }
    L_0x0042:
        monitor-exit(r12);	 Catch:{ all -> 0x0044 }
        return;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0044 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbay.zzo(java.lang.String, long):void");
    }

    public void acquire(long j) {
        zzt.zzzg();
        zzo(null, j);
        this.zzbEw.acquire(j);
    }

    public boolean isHeld() {
        return this.zzbEw.isHeld();
    }

    public void release() {
        zzgM(null);
        this.zzbEw.release();
    }

    public void setReferenceCounted(boolean z) {
        this.zzbEw.setReferenceCounted(z);
        this.zzbEz = z;
    }

    public void zzc(WorkSource workSource) {
        if (workSource != null && zzz.zzbf(this.mContext)) {
            if (this.zzbjm != null) {
                this.zzbjm.add(workSource);
            } else {
                this.zzbjm = workSource;
            }
            zzd(this.zzbjm);
        }
    }
}
