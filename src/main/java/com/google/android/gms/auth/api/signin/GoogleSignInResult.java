package com.google.android.gms.auth.api.signin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public class GoogleSignInResult implements Result {
    private Status zzair;
    private GoogleSignInAccount zzaks;

    public GoogleSignInResult(@Nullable GoogleSignInAccount googleSignInAccount, @NonNull Status status) {
        this.zzaks = googleSignInAccount;
        this.zzair = status;
    }

    @Nullable
    public GoogleSignInAccount getSignInAccount() {
        return this.zzaks;
    }

    @NonNull
    public Status getStatus() {
        return this.zzair;
    }

    public boolean isSuccess() {
        return this.zzair.isSuccess();
    }
}
