package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.zza;

public final class zzvm extends zza {
    public static final Creator<zzvm> CREATOR = new zzvn();
    final int zzaiI;
    private final Credential zzajL;

    zzvm(int i, Credential credential) {
        this.zzaiI = i;
        this.zzajL = credential;
    }

    public zzvm(Credential credential) {
        this(1, credential);
    }

    public Credential getCredential() {
        return this.zzajL;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzvn.zza(this, parcel, i);
    }
}
