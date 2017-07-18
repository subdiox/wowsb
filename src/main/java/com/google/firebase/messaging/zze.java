package com.google.firebase.messaging;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zze implements Creator<RemoteMessage> {
    static void zza(RemoteMessage remoteMessage, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 2, remoteMessage.zzaic, false);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzlS(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzqI(i);
    }

    public RemoteMessage zzlS(Parcel parcel) {
        int zzaY = zzb.zzaY(parcel);
        Bundle bundle = null;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 2:
                    bundle = zzb.zzs(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new RemoteMessage(bundle);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public RemoteMessage[] zzqI(int i) {
        return new RemoteMessage[i];
    }
}
