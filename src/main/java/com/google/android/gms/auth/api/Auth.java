package com.google.android.gms.auth.api;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.zzc;
import com.google.android.gms.auth.api.signin.internal.zzd;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Api.zzf;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.internal.zzut;
import com.google.android.gms.internal.zzuu;
import com.google.android.gms.internal.zzuv;
import com.google.android.gms.internal.zzvc;
import com.google.android.gms.internal.zzvf;
import com.google.android.gms.internal.zzvt;
import java.util.Collections;
import java.util.List;

public final class Auth {
    public static final Api<AuthCredentialsOptions> CREDENTIALS_API = new Api("Auth.CREDENTIALS_API", zzaiV, zzaiS);
    public static final CredentialsApi CredentialsApi = new zzvc();
    public static final Api<GoogleSignInOptions> GOOGLE_SIGN_IN_API = new Api("Auth.GOOGLE_SIGN_IN_API", zzaiX, zzaiU);
    public static final GoogleSignInApi GoogleSignInApi = new zzc();
    public static final Api<zzb> PROXY_API = zza.API;
    public static final ProxyApi ProxyApi = new zzvt();
    public static final zzf<zzvf> zzaiS = new zzf();
    public static final zzf<zzuv> zzaiT = new zzf();
    public static final zzf<zzd> zzaiU = new zzf();
    private static final zza<zzvf, AuthCredentialsOptions> zzaiV = new zza<zzvf, AuthCredentialsOptions>() {
        public zzvf zza(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, AuthCredentialsOptions authCredentialsOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzvf(context, looper, com_google_android_gms_common_internal_zzg, authCredentialsOptions, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final zza<zzuv, NoOptions> zzaiW = new zza<zzuv, NoOptions>() {
        public /* synthetic */ zze zza(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzd(context, looper, com_google_android_gms_common_internal_zzg, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzuv zzd(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzuv(context, looper, com_google_android_gms_common_internal_zzg, connectionCallbacks, onConnectionFailedListener);
        }
    };
    private static final zza<zzd, GoogleSignInOptions> zzaiX = new zza<zzd, GoogleSignInOptions>() {
        public zzd zza(Context context, Looper looper, zzg com_google_android_gms_common_internal_zzg, @Nullable GoogleSignInOptions googleSignInOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzd(context, looper, com_google_android_gms_common_internal_zzg, googleSignInOptions, connectionCallbacks, onConnectionFailedListener);
        }

        public List<Scope> zza(@Nullable GoogleSignInOptions googleSignInOptions) {
            return googleSignInOptions == null ? Collections.emptyList() : googleSignInOptions.zzrj();
        }

        public /* synthetic */ List zzp(@Nullable Object obj) {
            return zza((GoogleSignInOptions) obj);
        }
    };
    public static final Api<NoOptions> zzaiY = new Api("Auth.ACCOUNT_STATUS_API", zzaiW, zzaiT);
    public static final zzut zzaiZ = new zzuu();

    public static final class AuthCredentialsOptions implements Optional {
        private final String zzaja;
        private final PasswordSpecification zzajb;

        public static class Builder {
            @NonNull
            private PasswordSpecification zzajb = PasswordSpecification.zzajC;
        }

        public Bundle zzqL() {
            Bundle bundle = new Bundle();
            bundle.putString("consumer_package", this.zzaja);
            bundle.putParcelable("password_specification", this.zzajb);
            return bundle;
        }

        public PasswordSpecification zzqT() {
            return this.zzajb;
        }
    }

    private Auth() {
    }
}
