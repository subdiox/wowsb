package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzvn implements Creator<zzvm> {
    static void zza(zzvm com_google_android_gms_internal_zzvm, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_internal_zzvm.getCredential(), i, false);
        zzc.zzc(parcel, 1000, com_google_android_gms_internal_zzvm.zzaiI);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzS(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbh(i);
    }

    public zzvm zzS(Parcel parcel) {
        int zzaY = zzb.zzaY(parcel);
        int i = 0;
        Credential credential = null;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    credential = (Credential) zzb.zza(parcel, zzaX, Credential.CREATOR);
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
            return new zzvm(i, credential);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public zzvm[] zzbh(int i) {
        return new zzvm[i];
    }
}
