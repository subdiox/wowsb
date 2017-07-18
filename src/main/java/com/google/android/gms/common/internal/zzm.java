package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzm implements Callback {
    private final Handler mHandler;
    private final zza zzaFU;
    private final ArrayList<ConnectionCallbacks> zzaFV = new ArrayList();
    final ArrayList<ConnectionCallbacks> zzaFW = new ArrayList();
    private final ArrayList<OnConnectionFailedListener> zzaFX = new ArrayList();
    private volatile boolean zzaFY = false;
    private final AtomicInteger zzaFZ = new AtomicInteger(0);
    private boolean zzaGa = false;
    private final Object zzrJ = new Object();

    public interface zza {
        boolean isConnected();

        Bundle zzuC();
    }

    public zzm(Looper looper, zza com_google_android_gms_common_internal_zzm_zza) {
        this.zzaFU = com_google_android_gms_common_internal_zzm_zza;
        this.mHandler = new Handler(looper, this);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.zzrJ) {
                if (this.zzaFY && this.zzaFU.isConnected() && this.zzaFV.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzaFU.zzuC());
                }
            }
            return true;
        }
        Log.wtf("GmsClientEvents", "Don't know how to handle message: " + message.what, new Exception());
        return false;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        boolean contains;
        zzac.zzw(connectionCallbacks);
        synchronized (this.zzrJ) {
            contains = this.zzaFV.contains(connectionCallbacks);
        }
        return contains;
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        boolean contains;
        zzac.zzw(onConnectionFailedListener);
        synchronized (this.zzrJ) {
            contains = this.zzaFX.contains(onConnectionFailedListener);
        }
        return contains;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzac.zzw(connectionCallbacks);
        synchronized (this.zzrJ) {
            if (this.zzaFV.contains(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 62).append("registerConnectionCallbacks(): listener ").append(valueOf).append(" is already registered").toString());
            } else {
                this.zzaFV.add(connectionCallbacks);
            }
        }
        if (this.zzaFU.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzac.zzw(onConnectionFailedListener);
        synchronized (this.zzrJ) {
            if (this.zzaFX.contains(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 67).append("registerConnectionFailedListener(): listener ").append(valueOf).append(" is already registered").toString());
            } else {
                this.zzaFX.add(onConnectionFailedListener);
            }
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzac.zzw(connectionCallbacks);
        synchronized (this.zzrJ) {
            if (!this.zzaFV.remove(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 52).append("unregisterConnectionCallbacks(): listener ").append(valueOf).append(" not found").toString());
            } else if (this.zzaGa) {
                this.zzaFW.add(connectionCallbacks);
            }
        }
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzac.zzw(onConnectionFailedListener);
        synchronized (this.zzrJ) {
            if (!this.zzaFX.remove(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 57).append("unregisterConnectionFailedListener(): listener ").append(valueOf).append(" not found").toString());
            }
        }
    }

    public void zzcV(int i) {
        boolean z = false;
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            z = true;
        }
        zzac.zza(z, (Object) "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.zzrJ) {
            this.zzaGa = true;
            ArrayList arrayList = new ArrayList(this.zzaFV);
            int i2 = this.zzaFZ.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.zzaFY || this.zzaFZ.get() != i2) {
                    break;
                } else if (this.zzaFV.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzaFW.clear();
            this.zzaGa = false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzn(com.google.android.gms.common.ConnectionResult r6) {
        /*
        r5 = this;
        r1 = 1;
        r0 = android.os.Looper.myLooper();
        r2 = r5.mHandler;
        r2 = r2.getLooper();
        if (r0 != r2) goto L_0x0046;
    L_0x000d:
        r0 = r1;
    L_0x000e:
        r2 = "onConnectionFailure must only be called on the Handler thread";
        com.google.android.gms.common.internal.zzac.zza(r0, r2);
        r0 = r5.mHandler;
        r0.removeMessages(r1);
        r1 = r5.zzrJ;
        monitor-enter(r1);
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x0054 }
        r2 = r5.zzaFX;	 Catch:{ all -> 0x0054 }
        r0.<init>(r2);	 Catch:{ all -> 0x0054 }
        r2 = r5.zzaFZ;	 Catch:{ all -> 0x0054 }
        r2 = r2.get();	 Catch:{ all -> 0x0054 }
        r3 = r0.iterator();	 Catch:{ all -> 0x0054 }
    L_0x002c:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0054 }
        if (r0 == 0) goto L_0x0057;
    L_0x0032:
        r0 = r3.next();	 Catch:{ all -> 0x0054 }
        r0 = (com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener) r0;	 Catch:{ all -> 0x0054 }
        r4 = r5.zzaFY;	 Catch:{ all -> 0x0054 }
        if (r4 == 0) goto L_0x0044;
    L_0x003c:
        r4 = r5.zzaFZ;	 Catch:{ all -> 0x0054 }
        r4 = r4.get();	 Catch:{ all -> 0x0054 }
        if (r4 == r2) goto L_0x0048;
    L_0x0044:
        monitor-exit(r1);	 Catch:{ all -> 0x0054 }
    L_0x0045:
        return;
    L_0x0046:
        r0 = 0;
        goto L_0x000e;
    L_0x0048:
        r4 = r5.zzaFX;	 Catch:{ all -> 0x0054 }
        r4 = r4.contains(r0);	 Catch:{ all -> 0x0054 }
        if (r4 == 0) goto L_0x002c;
    L_0x0050:
        r0.onConnectionFailed(r6);	 Catch:{ all -> 0x0054 }
        goto L_0x002c;
    L_0x0054:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0054 }
        throw r0;
    L_0x0057:
        monitor-exit(r1);	 Catch:{ all -> 0x0054 }
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzm.zzn(com.google.android.gms.common.ConnectionResult):void");
    }

    public void zzq(Bundle bundle) {
        boolean z = true;
        zzac.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object) "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.zzrJ) {
            zzac.zzaw(!this.zzaGa);
            this.mHandler.removeMessages(1);
            this.zzaGa = true;
            if (this.zzaFW.size() != 0) {
                z = false;
            }
            zzac.zzaw(z);
            ArrayList arrayList = new ArrayList(this.zzaFV);
            int i = this.zzaFZ.get();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.zzaFY || !this.zzaFU.isConnected() || this.zzaFZ.get() != i) {
                    break;
                } else if (!this.zzaFW.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzaFW.clear();
            this.zzaGa = false;
        }
    }

    public void zzxX() {
        this.zzaFY = false;
        this.zzaFZ.incrementAndGet();
    }

    public void zzxY() {
        this.zzaFY = true;
    }
}
