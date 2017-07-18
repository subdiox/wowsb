package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzuz;

public final class IdToken extends zza implements ReflectedParcelable {
    public static final Creator<IdToken> CREATOR = new zze();
    final int zzaiI;
    @NonNull
    private final String zzajB;
    @NonNull
    private final String zzaji;

    IdToken(int i, @NonNull String str, @NonNull String str2) {
        zzuz.zzct(str);
        zzac.zzb(!TextUtils.isEmpty(str2), (Object) "id token string cannot be null or empty");
        this.zzaiI = i;
        this.zzaji = str;
        this.zzajB = str2;
    }

    public IdToken(@NonNull String str, @NonNull String str2) {
        this(1, str, str2);
    }

    @NonNull
    public String getAccountType() {
        return this.zzaji;
    }

    @NonNull
    public String getIdToken() {
        return this.zzajB;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zze.zza(this, parcel, i);
    }
}
