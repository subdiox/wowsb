package com.google.android.gms.auth.api.proxy;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzd implements Creator<ProxyResponse> {
    static void zza(ProxyResponse proxyResponse, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zzc(parcel, 1, proxyResponse.googlePlayServicesStatusCode);
        zzc.zza(parcel, 2, proxyResponse.recoveryAction, i, false);
        zzc.zzc(parcel, 3, proxyResponse.statusCode);
        zzc.zza(parcel, 4, proxyResponse.zzajR, false);
        zzc.zza(parcel, 5, proxyResponse.body, false);
        zzc.zzc(parcel, 1000, proxyResponse.versionCode);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzV(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbk(i);
    }

    public ProxyResponse zzV(Parcel parcel) {
        byte[] bArr = null;
        int i = 0;
        int zzaY = zzb.zzaY(parcel);
        Bundle bundle = null;
        PendingIntent pendingIntent = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    i2 = zzb.zzg(parcel, zzaX);
                    break;
                case 2:
                    pendingIntent = (PendingIntent) zzb.zza(parcel, zzaX, PendingIntent.CREATOR);
                    break;
                case 3:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 4:
                    bundle = zzb.zzs(parcel, zzaX);
                    break;
                case 5:
                    bArr = zzb.zzt(parcel, zzaX);
                    break;
                case 1000:
                    i3 = zzb.zzg(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new ProxyResponse(i3, i2, pendingIntent, i, bundle, bArr);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public ProxyResponse[] zzbk(int i) {
        return new ProxyResponse[i];
    }
}
