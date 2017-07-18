package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;

public final class SignInConfiguration extends zza implements ReflectedParcelable {
    public static final Creator<SignInConfiguration> CREATOR = new zzm();
    final int versionCode;
    private final String zzakG;
    private GoogleSignInOptions zzakH;

    SignInConfiguration(int i, String str, GoogleSignInOptions googleSignInOptions) {
        this.versionCode = i;
        this.zzakG = zzac.zzdr(str);
        this.zzakH = googleSignInOptions;
    }

    public SignInConfiguration(String str, GoogleSignInOptions googleSignInOptions) {
        this(3, str, googleSignInOptions);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            SignInConfiguration signInConfiguration = (SignInConfiguration) obj;
            if (!this.zzakG.equals(signInConfiguration.zzry())) {
                return false;
            }
            if (this.zzakH == null) {
                if (signInConfiguration.zzrz() != null) {
                    return false;
                }
            } else if (!this.zzakH.equals(signInConfiguration.zzrz())) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return new zzh().zzq(this.zzakG).zzq(this.zzakH).zzru();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzm.zza(this, parcel, i);
    }

    public String zzry() {
        return this.zzakG;
    }

    public GoogleSignInOptions zzrz() {
        return this.zzakH;
    }
}
