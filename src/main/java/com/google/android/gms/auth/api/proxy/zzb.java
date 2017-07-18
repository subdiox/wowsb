package com.google.android.gms.auth.api.proxy;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;

public class zzb implements Creator<zza> {
    static void zza(zza com_google_android_gms_auth_api_proxy_zza, Parcel parcel, int i) {
        int zzaZ = zzc.zzaZ(parcel);
        zzc.zza(parcel, 1, com_google_android_gms_auth_api_proxy_zza.hostname, false);
        zzc.zzc(parcel, 2, com_google_android_gms_auth_api_proxy_zza.port);
        zzc.zza(parcel, 3, com_google_android_gms_auth_api_proxy_zza.timeoutMillis);
        zzc.zza(parcel, 4, com_google_android_gms_auth_api_proxy_zza.body, false);
        zzc.zza(parcel, 5, com_google_android_gms_auth_api_proxy_zza.method, false);
        zzc.zzc(parcel, 1000, com_google_android_gms_auth_api_proxy_zza.versionCode);
        zzc.zzJ(parcel, zzaZ);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzT(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzbi(i);
    }

    public zza zzT(Parcel parcel) {
        int i = 0;
        String str = null;
        int zzaY = com.google.android.gms.common.internal.safeparcel.zzb.zzaY(parcel);
        long j = 0;
        byte[] bArr = null;
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzaY) {
            int zzaX = com.google.android.gms.common.internal.safeparcel.zzb.zzaX(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zzb.zzdc(zzaX)) {
                case 1:
                    str2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, zzaX);
                    break;
                case 2:
                    i = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, zzaX);
                    break;
                case 3:
                    j = com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, zzaX);
                    break;
                case 4:
                    bArr = com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, zzaX);
                    break;
                case 5:
                    str = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, zzaX);
                    break;
                case 1000:
                    i2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, zzaX);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, zzaX);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaY) {
            return new zza(i2, str2, i, j, bArr, str);
        }
        throw new zza("Overread allowed size end=" + zzaY, parcel);
    }

    public zza[] zzbi(int i) {
        return new zza[i];
    }
}
