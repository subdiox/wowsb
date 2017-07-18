package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.Binder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.zzi.zza;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.zzg;

public class zzl extends zza {
    private final Context mContext;

    public zzl(Context context) {
        this.mContext = context;
    }

    private void zzrw() {
        if (!zzg.zzf(this.mContext, Binder.getCallingUid())) {
            throw new SecurityException("Calling UID " + Binder.getCallingUid() + " is not Google Play services.");
        }
    }

    private void zzrx() {
        zzn zzas = zzn.zzas(this.mContext);
        GoogleSignInAccount zzrB = zzas.zzrB();
        HasOptions hasOptions = GoogleSignInOptions.DEFAULT_SIGN_IN;
        if (zzrB != null) {
            hasOptions = zzas.zzrC();
        }
        GoogleApiClient build = new Builder(this.mContext).addApi(Auth.GOOGLE_SIGN_IN_API, hasOptions).build();
        try {
            if (build.blockingConnect().isSuccess()) {
                if (zzrB != null) {
                    Auth.GoogleSignInApi.revokeAccess(build);
                } else {
                    build.clearDefaultAccountAndReconnect();
                }
            }
            build.disconnect();
        } catch (Throwable th) {
            build.disconnect();
        }
    }

    public void zzrv() {
        zzrw();
        zzrx();
    }
}
