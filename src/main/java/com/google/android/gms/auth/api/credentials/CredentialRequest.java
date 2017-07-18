package com.google.android.gms.auth.api.credentials;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.zzac;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CredentialRequest extends zza {
    public static final Creator<CredentialRequest> CREATOR = new zzc();
    final int zzaiI;
    private final boolean zzajr;
    private final String[] zzajs;
    private final CredentialPickerConfig zzajt;
    private final CredentialPickerConfig zzaju;
    private final boolean zzajv;
    private final String zzajw;
    private final String zzajx;

    public static final class Builder {
        private boolean zzajr;
        private String[] zzajs;
        private CredentialPickerConfig zzajt;
        private CredentialPickerConfig zzaju;
        private boolean zzajv = false;
        @Nullable
        private String zzajw = null;
        @Nullable
        private String zzajx;

        public CredentialRequest build() {
            if (this.zzajs == null) {
                this.zzajs = new String[0];
            }
            if (this.zzajr || this.zzajs.length != 0) {
                return new CredentialRequest();
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

        public Builder setCredentialHintPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzaju = credentialPickerConfig;
            return this;
        }

        public Builder setCredentialPickerConfig(CredentialPickerConfig credentialPickerConfig) {
            this.zzajt = credentialPickerConfig;
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

        public Builder setPasswordLoginSupported(boolean z) {
            this.zzajr = z;
            return this;
        }

        public Builder setServerClientId(@Nullable String str) {
            this.zzajw = str;
            return this;
        }

        @Deprecated
        public Builder setSupportsPasswordLogin(boolean z) {
            return setPasswordLoginSupported(z);
        }
    }

    CredentialRequest(int i, boolean z, String[] strArr, CredentialPickerConfig credentialPickerConfig, CredentialPickerConfig credentialPickerConfig2, boolean z2, String str, String str2) {
        this.zzaiI = i;
        this.zzajr = z;
        this.zzajs = (String[]) zzac.zzw(strArr);
        if (credentialPickerConfig == null) {
            credentialPickerConfig = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzajt = credentialPickerConfig;
        if (credentialPickerConfig2 == null) {
            credentialPickerConfig2 = new com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Builder().build();
        }
        this.zzaju = credentialPickerConfig2;
        if (i < 3) {
            this.zzajv = true;
            this.zzajw = null;
            this.zzajx = null;
            return;
        }
        this.zzajv = z2;
        this.zzajw = str;
        this.zzajx = str2;
    }

    private CredentialRequest(Builder builder) {
        this(3, builder.zzajr, builder.zzajs, builder.zzajt, builder.zzaju, builder.zzajv, builder.zzajw, builder.zzajx);
    }

    @NonNull
    public String[] getAccountTypes() {
        return this.zzajs;
    }

    @NonNull
    public Set<String> getAccountTypesSet() {
        return new HashSet(Arrays.asList(this.zzajs));
    }

    @NonNull
    public CredentialPickerConfig getCredentialHintPickerConfig() {
        return this.zzaju;
    }

    @NonNull
    public CredentialPickerConfig getCredentialPickerConfig() {
        return this.zzajt;
    }

    @Nullable
    public String getIdTokenNonce() {
        return this.zzajx;
    }

    @Nullable
    public String getServerClientId() {
        return this.zzajw;
    }

    @Deprecated
    public boolean getSupportsPasswordLogin() {
        return isPasswordLoginSupported();
    }

    public boolean isIdTokenRequested() {
        return this.zzajv;
    }

    public boolean isPasswordLoginSupported() {
        return this.zzajr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzc.zza(this, parcel, i);
    }
}
