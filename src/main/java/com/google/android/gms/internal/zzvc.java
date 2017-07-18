package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzaad.zzb;

public final class zzvc implements CredentialsApi {

    private static class zza extends zzuy {
        private zzb<Status> zzajP;

        zza(zzb<Status> com_google_android_gms_internal_zzaad_zzb_com_google_android_gms_common_api_Status) {
            this.zzajP = com_google_android_gms_internal_zzaad_zzb_com_google_android_gms_common_api_Status;
        }

        public void zzh(Status status) {
            this.zzajP.setResult(status);
        }
    }

    private AuthCredentialsOptions zza(GoogleApiClient googleApiClient) {
        return ((zzvf) googleApiClient.zza(Auth.zzaiS)).zzrc();
    }

    public PendingResult<Status> delete(GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb(new zzvd<Status>(this, googleApiClient) {
            protected void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws RemoteException {
                com_google_android_gms_internal_zzvl.zza(new zza(this), new zzvg(credential));
            }

            protected Status zzb(Status status) {
                return status;
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzb(status);
            }
        });
    }

    public PendingResult<Status> disableAutoSignIn(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zzvd<Status>(this, googleApiClient) {
            protected void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws RemoteException {
                com_google_android_gms_internal_zzvl.zza(new zza(this));
            }

            protected Status zzb(Status status) {
                return status;
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzb(status);
            }
        });
    }

    public PendingIntent getHintPickerIntent(GoogleApiClient googleApiClient, HintRequest hintRequest) {
        zzac.zzb(googleApiClient.zza(Auth.CREDENTIALS_API), (Object) "Auth.CREDENTIALS_API must be added to GoogleApiClient to use this API");
        return zzve.zza(googleApiClient.getContext(), zza(googleApiClient), hintRequest);
    }

    public PendingResult<CredentialRequestResult> request(GoogleApiClient googleApiClient, final CredentialRequest credentialRequest) {
        return googleApiClient.zza(new zzvd<CredentialRequestResult>(this, googleApiClient) {
            protected void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws RemoteException {
                com_google_android_gms_internal_zzvl.zza(new zzuy(this) {
                    final /* synthetic */ AnonymousClass1 zzajN;

                    {
                        this.zzajN = r1;
                    }

                    public void zza(Status status, Credential credential) {
                        this.zzajN.zzb(new zzvb(status, credential));
                    }

                    public void zzh(Status status) {
                        this.zzajN.zzb(zzvb.zzi(status));
                    }
                }, credentialRequest);
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzj(status);
            }

            protected CredentialRequestResult zzj(Status status) {
                return zzvb.zzi(status);
            }
        });
    }

    public PendingResult<Status> save(GoogleApiClient googleApiClient, final Credential credential) {
        return googleApiClient.zzb(new zzvd<Status>(this, googleApiClient) {
            protected void zza(Context context, zzvl com_google_android_gms_internal_zzvl) throws RemoteException {
                com_google_android_gms_internal_zzvl.zza(new zza(this), new zzvm(credential));
            }

            protected Status zzb(Status status) {
                return status;
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzb(status);
            }
        });
    }
}
