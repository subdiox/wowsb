package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzvj implements Creator<zzvi> {
    static void zza(zzvi com_google_android_gms_internal_zzvi, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_internal_zzvi.zzqT(), i, false);
        zzc.zzc(parcel, 1000, com_google_android_gms_internal_zzvi.zzaiI);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzR(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbg(i);
    }

    public zzvi zzR(Parcel parcel) {
        int zzaY = zzb.zzaY(parcel);
        int i = 0;
        PasswordSpecification passwordSpecification = null;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    passwordSpecification = (PasswordSpecification) zzb.zza(parcel, zzaX, PasswordSpecification.CREATOR);
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
            return new zzvi(i, passwordSpecification);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public zzvi[] zzbg(int i) {
        return new zzvi[i];
    }
}
