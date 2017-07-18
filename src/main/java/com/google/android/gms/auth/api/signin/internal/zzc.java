package com.google.android.gms.auth.api.signin.internal;

import android.content.Intent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

public class zzc implements GoogleSignInApi {
    private GoogleSignInOptions zzb(GoogleApiClient googleApiClient) {
        return ((zzd) googleApiClient.zza(Auth.zzaiU)).zzrt();
    }

    public Intent getSignInIntent(GoogleApiClient googleApiClient) {
        return zze.zza(googleApiClient.getContext(), zzb(googleApiClient));
    }

    public GoogleSignInResult getSignInResultFromIntent(Intent intent) {
        return zze.getSignInResultFromIntent(intent);
    }

    public PendingResult<Status> revokeAccess(GoogleApiClient googleApiClient) {
        return zze.zzb(googleApiClient, googleApiClient.getContext());
    }

    public PendingResult<Status> signOut(GoogleApiClient googleApiClient) {
        return zze.zza(googleApiClient, googleApiClient.getContext());
    }

    public OptionalPendingResult<GoogleSignInResult> silentSignIn(GoogleApiClient googleApiClient) {
        return zze.zza(googleApiClient, googleApiClient.getContext(), zzb(googleApiClient));
    }
}
