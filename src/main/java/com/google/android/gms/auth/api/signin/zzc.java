package com.google.android.gms.auth.api.signin;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;

public class zzc implements Creator<SignInAccount> {
    static void zza(SignInAccount signInAccount, Parcel parcel, int i) {
        int zzaZ = com.google.android.gms.common.internal.safeparcel.zzc.zzaZ(parcel);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1, signInAccount.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 4, signInAccount.zzaka, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 7, signInAccount.zzro(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 8, signInAccount.zzadi, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzY(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbn(i);
    }

    public SignInAccount zzY(Parcel parcel) {
        int zzaY = zzb.zzaY(parcel);
        int i = 0;
        String str = "";
        GoogleSignInAccount googleSignInAccount = null;
        String str2 = "";
        while (parcel.dataPosition() < zzaY) {
            GoogleSignInAccount googleSignInAccount2;
            String str3;
            int zzg;
            String str4;
            int zzaX = zzb.zzaX(parcel);
            String str5;
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    str5 = str2;
                    googleSignInAccount2 = googleSignInAccount;
                    str3 = str;
                    zzg = zzb.zzg(parcel, zzaX);
                    str4 = str5;
                    break;
                case 4:
                    zzg = i;
                    GoogleSignInAccount googleSignInAccount3 = googleSignInAccount;
                    str3 = zzb.zzq(parcel, zzaX);
                    str4 = str2;
                    googleSignInAccount2 = googleSignInAccount3;
                    break;
                case 7:
                    str3 = str;
                    zzg = i;
                    str5 = str2;
                    googleSignInAccount2 = (GoogleSignInAccount) zzb.zza(parcel, zzaX, GoogleSignInAccount.CREATOR);
                    str4 = str5;
                    break;
                case 8:
                    str4 = zzb.zzq(parcel, zzaX);
                    googleSignInAccount2 = googleSignInAccount;
                    str3 = str;
                    zzg = i;
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    str4 = str2;
                    googleSignInAccount2 = googleSignInAccount;
                    str3 = str;
                    zzg = i;
                    break;
            }
            i = zzg;
            str = str3;
            googleSignInAccount = googleSignInAccount2;
            str2 = str4;
        }
        if (parcel.dataPosition() == zzaY) {
            return new SignInAccount(i, str, googleSignInAccount, str2);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public SignInAccount[] zzbn(int i) {
        return new SignInAccount[i];
    }
}
