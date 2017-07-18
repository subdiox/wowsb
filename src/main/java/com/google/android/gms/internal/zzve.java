package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.PasswordSpecification;
import com.google.android.gms.common.internal.zzac;

public class zzve {
    public static PendingIntent zza(Context context, AuthCredentialsOptions authCredentialsOptions, HintRequest hintRequest) {
        zzac.zzb((Object) context, (Object) "context must not be null");
        zzac.zzb((Object) hintRequest, (Object) "request must not be null");
        return PendingIntent.getActivity(context, CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE, zzva.zza(context, hintRequest, zza(authCredentialsOptions)), 268435456);
    }

    public static PasswordSpecification zza(AuthCredentialsOptions authCredentialsOptions) {
        return (authCredentialsOptions == null || authCredentialsOptions.zzqT() == null) ? PasswordSpecification.zzajC : authCredentialsOptions.zzqT();
    }
}
