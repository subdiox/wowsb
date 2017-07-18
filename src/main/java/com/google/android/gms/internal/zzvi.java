package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.common.internal.safeparcel.zza;

public final class zzvi extends zza {
    public static final Creator<zzvi> CREATOR = new zzvj();
    final int zzaiI;
    private final PasswordSpecification zzajb;

    zzvi(int i, PasswordSpecification passwordSpecification) {
        this.zzaiI = i;
        this.zzajb = passwordSpecification;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzvj.zza(this, parcel, i);
    }

    public PasswordSpecification zzqT() {
        return this.zzajb;
    }
}
