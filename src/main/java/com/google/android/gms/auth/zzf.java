package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import java.util.List;

public class zzf implements Creator<TokenData> {
    static void zza(TokenData tokenData, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zzc(parcel, 1, tokenData.zzaiI);
        zzc.zza(parcel, 2, tokenData.getToken(), false);
        zzc.zza(parcel, 3, tokenData.zzqO(), false);
        zzc.zza(parcel, 4, tokenData.zzqP());
        zzc.zza(parcel, 5, tokenData.zzqQ());
        zzc.zzb(parcel, 6, tokenData.zzqR(), false);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzJ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaY(i);
    }

    public TokenData zzJ(Parcel parcel) {
        List list = null;
        boolean z = false;
        int zzaY = zzb.zzaY(parcel);
        boolean z2 = false;
        Long l = null;
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 2:
                    str = zzb.zzq(parcel, zzaX);
                    break;
                case 3:
                    l = zzb.zzj(parcel, zzaX);
                    break;
                case 4:
                    z2 = zzb.zzc(parcel, zzaX);
                    break;
                case 5:
                    z = zzb.zzc(parcel, zzaX);
                    break;
                case 6:
                    list = zzb.zzE(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new TokenData(i, str, l, z2, z, list);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public TokenData[] zzaY(int i) {
        return new TokenData[i];
    }
}
