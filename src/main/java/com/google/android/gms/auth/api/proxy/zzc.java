package com.google.android.gms.auth.api.proxy;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;

public class zzc implements Creator<ProxyRequest> {
    static void zza(ProxyRequest proxyRequest, Parcel parcel, int i) {
        int zzaZ = com.google.android.gms.common.internal.safeparcel.zzc.zzaZ(parcel);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 1, proxyRequest.url, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 2, proxyRequest.httpMethod);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 3, proxyRequest.timeoutMillis);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 4, proxyRequest.body, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zza(parcel, 5, proxyRequest.zzajR, false);
        com.google.android.gms.common.internal.safeparcel.zzc.zzc(parcel, 1000, proxyRequest.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzU(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbj(i);
    }

    public ProxyRequest zzU(Parcel parcel) {
        int i = 0;
        Bundle bundle = null;
        int zzaY = zzb.zzaY(parcel);
        long j = 0;
        byte[] bArr = null;
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = zzb.zzaX(parcel);
            switch (zzb.zzdc(zzaX)) {
                case 1:
                    str = zzb.zzq(parcel, zzaX);
                    break;
                case 2:
                    i = zzb.zzg(parcel, zzaX);
                    break;
                case 3:
                    j = zzb.zzi(parcel, zzaX);
                    break;
                case 4:
                    bArr = zzb.zzt(parcel, zzaX);
                    break;
                case 5:
                    bundle = zzb.zzs(parcel, zzaX);
                    break;
                case 1000:
                    i2 = zzb.zzg(parcel, zzaX);
                    break;
                default:
                    zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new ProxyRequest(i2, str, i, j, bArr, bundle);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public ProxyRequest[] zzbj(int i) {
        return new ProxyRequest[i];
    }
}
