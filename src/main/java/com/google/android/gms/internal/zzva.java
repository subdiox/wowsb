package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zzd;

public final class zzva {
    public static Intent zza(Context context, HintRequest hintRequest, PasswordSpecification passwordSpecification) {
        Intent putExtra = new Intent("com.google.android.gms.auth.api.credentials.PICKER").putExtra("com.google.android.gms.credentials.hintRequestVersion", 2).putExtra("com.google.android.gms.credentials.RequestType", "Hints");
        zzd.zza((SafeParcelable) passwordSpecification, putExtra, "com.google.android.gms.credentials.PasswordSpecification");
        zzd.zza((SafeParcelable) hintRequest, putExtra, "com.google.android.gms.credentials.HintRequest");
        return putExtra;
    }
}
