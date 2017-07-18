package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.internal.zzux.zza;

public class zzuv extends zzl<zzux> {
    public zzuv(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 74, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
    }

    protected zzux zzaB(IBinder iBinder) {
        return zza.zzaD(iBinder);
    }

    @NonNull
    protected String zzeA() {
        return "com.google.android.gms.auth.api.accountactivationstate.internal.IAccountActivationStateService";
    }

    @NonNull
    protected String zzez() {
        return "com.google.android.gms.auth.api.accountactivationstate.START";
    }

    protected /* synthetic */ IInterface zzh(IBinder iBinder) {
        return zzaB(iBinder);
    }
}
