package com.google.android.gms.auth.api.proxy;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProxyResponse extends zza {
    public static final Creator<ProxyResponse> CREATOR = new zzd();
    public static final int STATUS_CODE_NO_CONNECTION = -1;
    public final byte[] body;
    public final int googlePlayServicesStatusCode;
    public final PendingIntent recoveryAction;
    public final int statusCode;
    final int versionCode;
    final Bundle zzajR;

    ProxyResponse(int i, int i2, PendingIntent pendingIntent, int i3, Bundle bundle, byte[] bArr) {
        this.versionCode = i;
        this.googlePlayServicesStatusCode = i2;
        this.statusCode = i3;
        this.zzajR = bundle;
        this.body = bArr;
        this.recoveryAction = pendingIntent;
    }

    public ProxyResponse(int i, PendingIntent pendingIntent, int i2, Bundle bundle, byte[] bArr) {
        this(1, i, pendingIntent, i2, bundle, bArr);
    }

    private ProxyResponse(int i, Bundle bundle, byte[] bArr) {
        this(1, 0, null, i, bundle, bArr);
    }

    public ProxyResponse(int i, Map<String, String> map, byte[] bArr) {
        this(i, zzW(map), bArr);
    }

    public static ProxyResponse createErrorProxyResponse(int i, PendingIntent pendingIntent, int i2, Map<String, String> map, byte[] bArr) {
        return new ProxyResponse(1, i, pendingIntent, i2, zzW(map), bArr);
    }

    private static Bundle zzW(Map<String, String> map) {
        Bundle bundle = new Bundle();
        if (map == null) {
            return bundle;
        }
        for (Entry entry : map.entrySet()) {
            bundle.putString((String) entry.getKey(), (String) entry.getValue());
        }
        return bundle;
    }

    public Map<String, String> getHeaders() {
        if (this.zzajR == null) {
            return Collections.emptyMap();
        }
        Map<String, String> hashMap = new HashMap();
        for (String str : this.zzajR.keySet()) {
            hashMap.put(str, this.zzajR.getString(str));
        }
        return hashMap;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }
}
