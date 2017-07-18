package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.zza;

public class AccountChangeEventsRequest extends zza {
    public static final Creator<AccountChangeEventsRequest> CREATOR = new zzb();
    final int mVersion;
    Account zzahh;
    @Deprecated
    String zzaiu;
    int zzaiw;

    public AccountChangeEventsRequest() {
        this.mVersion = 1;
    }

    AccountChangeEventsRequest(int i, int i2, String str, Account account) {
        this.mVersion = i;
        this.zzaiw = i2;
        this.zzaiu = str;
        if (account != null || TextUtils.isEmpty(str)) {
            this.zzahh = account;
        } else {
            this.zzahh = new Account(str, "com.google");
        }
    }

    public Account getAccount() {
        return this.zzahh;
    }

    @Deprecated
    public String getAccountName() {
        return this.zzaiu;
    }

    public int getEventIndex() {
        return this.zzaiw;
    }

    public AccountChangeEventsRequest setAccount(Account account) {
        this.zzahh = account;
        return this;
    }

    @Deprecated
    public AccountChangeEventsRequest setAccountName(String str) {
        this.zzaiu = str;
        return this;
    }

    public AccountChangeEventsRequest setEventIndex(int i) {
        this.zzaiw = i;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb.zza(this, parcel, i);
    }
}
