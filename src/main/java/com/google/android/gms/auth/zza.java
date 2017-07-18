package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zza implements Creator<AccountChangeEvent> {
    static void zza(AccountChangeEvent accountChangeEvent, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zzc(parcel, 1, accountChangeEvent.mVersion);
        zzc.zza(parcel, 2, accountChangeEvent.zzait);
        zzc.zza(parcel, 3, accountChangeEvent.zzaiu, false);
        zzc.zzc(parcel, 4, accountChangeEvent.zzaiv);
        zzc.zzc(parcel, 5, accountChangeEvent.zzaiw);
        zzc.zza(parcel, 6, accountChangeEvent.zzaix, false);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzG(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaV(i);
    }

    public AccountChangeEvent zzG(Parcel parcel) {
        String str = null;
        int i = 0;
        int zzaY = zzb.zzaY(parcel);
        long j = 0;
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    i3 = zzb.zzg(parcel, zzaX);
                    break;
                case 2:
                    j = zzb.zzi(parcel, zzaX);
                    break;
                case 3:
                    str2 = zzb.zzq(parcel, zzaX);
                    break;
                case 4:
                    i2 = zzb.zzg(parcel, zzaX);
                    break;
                case 5:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 6:
                    str = zzb.zzq(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new AccountChangeEvent(i3, j, str2, i2, i, str);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zzb.zza("Overread allowed size end=" + zzaY, parcel);
    }

    public AccountChangeEvent[] zzaV(int i) {
        return new AccountChangeEvent[i];
    }
}
