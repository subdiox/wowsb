package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyApi;
import com.google.android.gms.auth.api.proxy.ProxyApi.ProxyResult;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzac;

public class zzvt implements ProxyApi {
    public PendingResult<ProxyResult> performProxyRequest(GoogleApiClient googleApiClient, final ProxyRequest proxyRequest) {
        zzac.zzw(googleApiClient);
        zzac.zzw(proxyRequest);
        return googleApiClient.zzb(new zzvs(this, googleApiClient) {
            protected void zza(Context context, zzvr com_google_android_gms_internal_zzvr) throws RemoteException {
                com_google_android_gms_internal_zzvr.zza(new zzvo(this) {
                    final /* synthetic */ AnonymousClass1 zzajX;

                    {
                        this.zzajX = r1;
                    }

                    public void zza(ProxyResponse proxyResponse) {
                        this.zzajX.zzb(new zzvu(proxyResponse));
                    }
                }, proxyRequest);
            }
        });
    }
}
