package com.google.android.gms.auth.api.signin;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;

public class SignInAccount extends zza implements ReflectedParcelable {
    public static final Creator<SignInAccount> CREATOR = new zzc();
    final int versionCode;
    @Deprecated
    String zzadi;
    @Deprecated
    String zzaka;
    private GoogleSignInAccount zzakt;

    SignInAccount(int i, String str, GoogleSignInAccount googleSignInAccount, String str2) {
        this.versionCode = i;
        this.zzakt = googleSignInAccount;
        this.zzaka = zzac.zzh(str, "8.3 and 8.4 SDKs require non-null email");
        this.zzadi = zzac.zzh(str2, "8.3 and 8.4 SDKs require non-null userId");
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }

    public GoogleSignInAccount zzro() {
        return this.zzakt;
    }
}
