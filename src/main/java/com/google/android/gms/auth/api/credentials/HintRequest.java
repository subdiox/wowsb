package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;

public final class HintRequest extends zza implements ReflectedParcelable {
    public static final Creator<HintRequest> CREATOR = new zzd();
    final int zzaiI;
    private final boolean zzajA;
    private final String[] zzajs;
    private final boolean zzajv;
    private final String zzajw;
    private final String zzajx;
    private final CredentialPickerConfig zzajy;
    private final boolean zzajz;

    public static final class Builder {
        private boolean zzajA;
        private String[] zzajs;
        private boolean zzajv = false;
        private String zzajw;
        private String zzajx;
        private CredentialPickerConfig zzajy = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        private boolean zzajz;

        public HintRequest build() {
            if (this.zzajs == null) {
                this.zzajs = new String[0];
            }
            if (this.zzajz || this.zzajA || this.zzajs.length != 0) {
                return new HintRequest();
            }
            throw new IllegalStateException("At least one authentication method must be specified");
        }

        public Builder setAccountTypes(String... strArr) {
            if (strArr == null) {
                strArr = new String[0];
            }
            this.zzajs = strArr;
            return this;
        }

        public Builder setEmailAddressIdentifierSupported(boolean z) {
            this.zzajz = z;
            return this;
        }

        public Builder setHintPickerConfig(@NonNull CredentialPickerConfig credentialPickerConfig) {
            this.zzajy = (CredentialPickerConfig) zzac.zzw(credentialPickerConfig);
            return this;
        }

        public Builder setIdTokenNonce(@Nullable String str) {
            this.zzajx = str;
            return this;
        }

        public Builder setIdTokenRequested(boolean z) {
            this.zzajv = z;
            return this;
        }

        public Builder setPhoneNumberIdentifierSupported(boolean z) {
            this.zzajA = z;
            return this;
        }

        public Builder setServerClientId(@Nullable String str) {
            this.zzajw = str;
            return this;
        }
    }

    HintRequest(int i, CredentialPickerConfig credentialPickerConfig, boolean z, boolean z2, String[] strArr, boolean z3, String str, String str2) {
        this.zzaiI = i;
        this.zzajy = (CredentialPickerConfig) zzac.zzw(credentialPickerConfig);
        this.zzajz = z;
        this.zzajA = z2;
        this.zzajs = (String[]) zzac.zzw(strArr);
        if (this.zzaiI < 2) {
            this.zzajv = true;
            this.zzajw = null;
            this.zzajx = null;
            return;
        }
        this.zzajv = z3;
        this.zzajw = str;
        this.zzajx = str2;
    }

    private HintRequest(Builder builder) {
        this(2, builder.zzajy, builder.zzajz, builder.zzajA, builder.zzajs, builder.zzajv, builder.zzajw, builder.zzajx);
    }

    @NonNull
    public String[] getAccountTypes() {
        return this.zzajs;
    }

    @NonNull
    public CredentialPickerConfig getHintPickerConfig() {
        return this.zzajy;
    }

    @Nullable
    public String getIdTokenNonce() {
        return this.zzajx;
    }

    @Nullable
    public String getServerClientId() {
        return this.zzajw;
    }

    public boolean isEmailAddressIdentifierSupported() {
        return this.zzajz;
    }

    public boolean isIdTokenRequested() {
        return this.zzajv;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }

    public boolean zzqX() {
        return this.zzajA;
    }
}
