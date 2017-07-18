package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzm implements Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zzc(parcel, 1, signInConfiguration.versionCode);
        zzc.zza(parcel, 2, signInConfiguration.zzry(), false);
        zzc.zza(parcel, 5, signInConfiguration.zzrz(), i, false);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaa(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbp(i);
    }

    public SignInConfiguration zzaa(Parcel parcel) {
        GoogleSignInOptions googleSignInOptions = null;
        int zzaY = zzb.zzaY(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 2:
                    str = zzb.zzq(parcel, zzaX);
                    break;
                case 5:
                    googleSignInOptions = (GoogleSignInOptions) zzb.zza(parcel, zzaX, GoogleSignInOptions.CREATOR);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new SignInConfiguration(i, str, googleSignInOptions);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public SignInConfiguration[] zzbp(int i) {
        return new SignInConfiguration[i];
    }
}
