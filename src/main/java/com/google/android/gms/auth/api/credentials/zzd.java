package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzd implements Creator<HintRequest> {
    static void zza(HintRequest hintRequest, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 1, hintRequest.getHintPickerConfig(), i, false);
        zzc.zza(parcel, 2, hintRequest.isEmailAddressIdentifierSupported());
        zzc.zza(parcel, 3, hintRequest.zzqX());
        zzc.zza(parcel, 4, hintRequest.getAccountTypes(), false);
        zzc.zza(parcel, 5, hintRequest.isIdTokenRequested());
        zzc.zza(parcel, 6, hintRequest.getServerClientId(), false);
        zzc.zza(parcel, 7, hintRequest.getIdTokenNonce(), false);
        zzc.zzc(parcel, 1000, hintRequest.zzaiI);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzN(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbc(i);
    }

    public HintRequest zzN(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzaY = zzb.zzaY(parcel);
        String str2 = null;
        String[] strArr = null;
        boolean z2 = false;
        boolean z3 = false;
        CredentialPickerConfig credentialPickerConfig = null;
        int i = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    credentialPickerConfig = (CredentialPickerConfig) zzb.zza(parcel, zzaX, CredentialPickerConfig.CREATOR);
                    break;
                case 2:
                    z3 = zzb.zzc(parcel, zzaX);
                    break;
                case 3:
                    z2 = zzb.zzc(parcel, zzaX);
                    break;
                case 4:
                    strArr = zzb.zzC(parcel, zzaX);
                    break;
                case 5:
                    z = zzb.zzc(parcel, zzaX);
                    break;
                case 6:
                    str2 = zzb.zzq(parcel, zzaX);
                    break;
                case 7:
                    str = zzb.zzq(parcel, zzaX);
                    break;
                case 1000:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new HintRequest(i, credentialPickerConfig, z3, z2, strArr, z, str2, str);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public HintRequest[] zzbc(int i) {
        return new HintRequest[i];
    }
}
