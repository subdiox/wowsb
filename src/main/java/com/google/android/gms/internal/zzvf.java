package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.internal.zzvl.zza;

public final class zzvf extends zzl<zzvl> {
    @Nullable
    private final AuthCredentialsOptions zzajQ;

    public zzvf(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, AuthCredentialsOptions authCredentialsOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 68, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        this.zzajQ = authCredentialsOptions;
    }

    protected zzvl zzaE(IBinder iBinder) {
        return zza.zzaG(iBinder);
    }

    protected String zzeA() {
        return "com.google.android.gms.auth.api.credentials.internal.ICredentialsService";
    }

    protected String zzez() {
        return "com.google.android.gms.auth.api.credentials.service.START";
    }

    protected /* synthetic */ IInterface zzh(IBinder iBinder) {
        return zzaE(iBinder);
    }

    protected Bundle zzqL() {
        return this.zzajQ == null ? new Bundle() : this.zzajQ.zzqL();
    }

    AuthCredentialsOptions zzrc() {
        return this.zzajQ;
    }
}
