package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzvh implements Creator<zzvg> {
    static void zza(zzvg com_google_android_gms_internal_zzvg, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_internal_zzvg.getCredential(), i, false);
        zzc.zzc(parcel, 1000, com_google_android_gms_internal_zzvg.zzaiI);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzQ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbf(i);
    }

    public zzvg zzQ(Parcel parcel) {
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
            return new zzvg(i, credential);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public zzvg[] zzbf(int i) {
        return new zzvg[i];
    }
}
