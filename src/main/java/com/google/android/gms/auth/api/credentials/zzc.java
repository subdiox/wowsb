package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;

public class zzc implements Creator<CredentialRequest> {
    static void zza(CredentialRequest credentialRequest, Parcel parcel, int i) {
        int zzaZ = com.google.android.gms.common.internal.safeparcel.zzc.zzaZ(parcel);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 1, credentialRequest.isPasswordLoginSupported());
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 2, credentialRequest.getAccountTypes(), false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 3, credentialRequest.getCredentialPickerConfig(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 4, credentialRequest.getCredentialHintPickerConfig(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 5, credentialRequest.isIdTokenRequested());
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 6, credentialRequest.getServerClientId(), false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 7, credentialRequest.getIdTokenNonce(), false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1000, credentialRequest.zzaiI);
        com.google.android.gms.common.internal.safeparcel.zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzM(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbb(i);
    }

    public CredentialRequest zzM(Parcel parcel) {
        boolean z = false;
        String str = null;
        int zzaY = zzb.zzaY(parcel);
        String str2 = null;
        CredentialPickerConfig credentialPickerConfig = null;
        CredentialPickerConfig credentialPickerConfig2 = null;
        String[] strArr = null;
        boolean z2 = false;
        int i = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    z2 = zzb.zzc(parcel, zzaX);
                    break;
                case 2:
                    strArr = zzb.zzC(parcel, zzaX);
                    break;
                case 3:
                    credentialPickerConfig2 = (CredentialPickerConfig) zzb.zza(parcel, zzaX, CredentialPickerConfig.CREATOR);
                    break;
                case 4:
                    credentialPickerConfig = (CredentialPickerConfig) zzb.zza(parcel, zzaX, CredentialPickerConfig.CREATOR);
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
            return new CredentialRequest(i, z2, strArr, credentialPickerConfig2, credentialPickerConfig, z, str2, str);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public CredentialRequest[] zzbb(int i) {
        return new CredentialRequest[i];
    }
}
