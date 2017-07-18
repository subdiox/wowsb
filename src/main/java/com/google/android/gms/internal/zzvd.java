package com.google.android.gms.internal;

import android.content.Context;
import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzaad.zza;

abstract class zzvd<R extends Result> extends zza<R, zzvf> {
    zzvd(GoogleApiClient googleApiClient) {
        super(Auth.CREDENTIALS_API, googleApiClient);
    }

    public /* synthetic */ void setResult(Object obj) {
        super.zzb((Result) obj);
    }

    protected abstract void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws DeadObjectException, RemoteException;

    protected final void zza(zzvf com_google_android_gms_internal_zzvf) throws DeadObjectException, RemoteException {
        zza(com_google_android_gms_internal_zzvf.getContext(), (zzvl) com_google_android_gms_internal_zzvf.zzxD());
    }
}
