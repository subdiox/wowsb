package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import java.util.List;

public class zzc implements Creator<AccountChangeEventsResponse> {
    static void zza(AccountChangeEventsResponse accountChangeEventsResponse, Parcel parcel, int i) {
        int zzaZ = com.google.android.gms.common.internal.safeparcel.zzc.zzaZ(parcel);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1, accountChangeEventsResponse.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 2, accountChangeEventsResponse.zzth, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzI(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaX(i);
    }

    public AccountChangeEventsResponse zzI(Parcel parcel) {
        int zzaY = zzb.zzaY(parcel);
        int i = 0;
        List list = null;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 2:
                    list = zzb.zzc(parcel, zzaX, AccountChangeEvent.CREATOR);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new AccountChangeEventsResponse(i, list);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public AccountChangeEventsResponse[] zzaX(int i) {
        return new AccountChangeEventsResponse[i];
    }
}
