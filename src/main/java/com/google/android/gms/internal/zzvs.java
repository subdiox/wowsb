package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzaad.zza;

abstract class zzvs extends zza<ProxyResult, zzvp> {
    public zzvs(GoogleApiClient googleApiClient) {
        super(com.google.android.gms.auth.api.zza.API, googleApiClient);
    }

    public /* synthetic */ void setResult(Object obj) {
        super.zzb((ProxyResult) obj);
    }

    protected abstract void zza(Context context, zzvr com_google_android_gms_internal_zzvr) throws RemoteException;

    protected final void zza(zzvp com_google_android_gms_internal_zzvp) throws RemoteException {
        zza(com_google_android_gms_internal_zzvp.getContext(), (zzvr) com_google_android_gms_internal_zzvp.zzxD());
    }

    protected /* synthetic */ Result zzc(Status status) {
        return zzk(status);
    }

    protected ProxyResult zzk(Status status) {
        return new zzvu(status);
    }
}
