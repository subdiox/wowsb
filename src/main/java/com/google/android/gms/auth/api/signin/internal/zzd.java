package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.auth.api.signin.internal.zzk.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzl;

public class zzd extends zzl<zzk> {
    private final GoogleSignInOptions zzakw;

    public zzd(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, GoogleSignInOptions googleSignInOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 91, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        if (googleSignInOptions == null) {
            googleSignInOptions = new Builder().build();
        }
        if (!com_google_android_gms_common_internal_zzg.zzxM().isEmpty()) {
            Builder builder = new Builder(googleSignInOptions);
            for (Scope requestScopes : com_google_android_gms_common_internal_zzg.zzxM()) {
                builder.requestScopes(requestScopes, new Scope[0]);
            }
            googleSignInOptions = builder.build();
        }
        this.zzakw = googleSignInOptions;
    }

    protected zzk zzaK(IBinder iBinder) {
        return zza.zzaM(iBinder);
    }

    protected String zzeA() {
        return "com.google.android.gms.auth.api.signin.internal.ISignInService";
    }

    protected String zzez() {
        return "com.google.android.gms.auth.api.signin.service.START";
    }

    protected /* synthetic */ IInterface zzh(IBinder iBinder) {
        return zzaK(iBinder);
    }

    public boolean zzrr() {
        return true;
    }

    public Intent zzrs() {
        return zze.zza(getContext(), this.zzakw);
    }

    public GoogleSignInOptions zzrt() {
        return this.zzakw;
    }
}
