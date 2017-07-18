package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.auth.api.zzb;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.internal.zzvr.zza;

public final class zzvp extends zzl<zzvr> {
    private final Bundle zzaje;

    public zzvp(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, zzb com_google_android_gms_auth_api_zzb, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 16, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        this.zzaje = com_google_android_gms_auth_api_zzb == null ? new Bundle() : com_google_android_gms_auth_api_zzb.zzqU();
    }

    protected zzvr zzaH(IBinder iBinder) {
        return zza.zzaJ(iBinder);
    }

    protected String zzeA() {
        return "com.google.android.gms.auth.api.internal.IAuthService";
    }

    protected String zzez() {
        return "com.google.android.gms.auth.service.START";
    }

    protected /* synthetic */ IInterface zzh(IBinder iBinder) {
        return zzaH(iBinder);
    }

    protected Bundle zzqL() {
        return this.zzaje;
    }

    public boolean zzrd() {
        zzg zzxW = zzxW();
        return (TextUtils.isEmpty(zzxW.getAccountName()) || zzxW.zzc(com.google.android.gms.auth.api.zza.API).isEmpty()) ? false : true;
    }
}
