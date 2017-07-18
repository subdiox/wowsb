package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;
import java.util.List;

public class AccountChangeEventsResponse extends zza {
    public static final Creator<AccountChangeEventsResponse> CREATOR = new zzc();
    final int mVersion;
    final List<AccountChangeEvent> zzth;

    AccountChangeEventsResponse(int i, List<AccountChangeEvent> list) {
        this.mVersion = i;
        this.zzth = (List) zzac.zzw(list);
    }

    public AccountChangeEventsResponse(List<AccountChangeEvent> list) {
        this.mVersion = 1;
        this.zzth = (List) zzac.zzw(list);
    }

    public List<AccountChangeEvent> getEvents() {
        return this.zzth;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
