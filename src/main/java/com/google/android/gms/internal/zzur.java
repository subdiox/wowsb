package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.RemoteException;
import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.auth.account.WorkAccountApi;
import com.google.android.gms.auth.account.WorkAccountApi.AddAccountResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

public class zzur implements WorkAccountApi {
    private static final Status zzaiO = new Status(13);

    static class zza extends com.google.android.gms.auth.account.zza.zza {
        zza() {
        }

        public void zzac(boolean z) {
            throw new UnsupportedOperationException();
        }

        public void zzd(Account account) {
            throw new UnsupportedOperationException();
        }
    }

    static class zzb implements AddAccountResult {
        private final Account zzahh;
        private final Status zzair;

        public zzb(Status status, Account account) {
            this.zzair = status;
            this.zzahh = account;
        }

        public Account getAccount() {
            return this.zzahh;
        }

        public Status getStatus() {
            return this.zzair;
        }
    }

    static class zzc implements Result {
        private final Status zzair;

        public zzc(Status status) {
            this.zzair = status;
        }

        public Status getStatus() {
            return this.zzair;
        }
    }

    public PendingResult<AddAccountResult> addWorkAccount(GoogleApiClient googleApiClient, final String str) {
        return googleApiClient.zzb(new com.google.android.gms.internal.zzaad.zza<AddAccountResult, zzus>(this, WorkAccount.API, googleApiClient) {
            public /* synthetic */ void setResult(Object obj) {
                super.zzb((AddAccountResult) obj);
            }

            protected void zza(zzus com_google_android_gms_internal_zzus) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzus.zzxD()).zza(new zza(this) {
                    final /* synthetic */ AnonymousClass2 zzaiQ;

                    {
                        this.zzaiQ = r1;
                    }

                    public void zzd(Account account) {
                        this.zzaiQ.zzb(new zzb(account != null ? Status.zzazx : zzur.zzaiO, account));
                    }
                }, str);
            }

            protected /* synthetic */ Result zzc(Status status) {
                return zzf(status);
            }

            protected AddAccountResult zzf(Status status) {
                return new zzb(status, null);
            }
        });
    }

    public PendingResult<Result> removeWorkAccount(GoogleApiClient googleApiClient, final Account account) {
        return googleApiClient.zzb(new com.google.android.gms.internal.zzaad.zza<Result, zzus>(this, WorkAccount.API, googleApiClient) {
            public /* synthetic */ void setResult(Object obj) {
                super.zzb((Result) obj);
            }

            protected void zza(zzus com_google_android_gms_internal_zzus) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzus.zzxD()).zza(new zza(this) {
                    final /* synthetic */ AnonymousClass3 zzaiR;

                    {
                        this.zzaiR = r1;
                    }

                    public void zzac(boolean z) {
                        this.zzaiR.zzb(new zzc(z ? Status.zzazx : zzur.zzaiO));
                    }
                }, account);
            }

            protected Result zzc(Status status) {
                return new zzc(status);
            }
        });
    }

    public void setWorkAuthenticatorEnabled(GoogleApiClient googleApiClient, final boolean z) {
        googleApiClient.zzb(new com.google.android.gms.internal.zzaad.zza<Result, zzus>(this, WorkAccount.API, googleApiClient) {
            public /* synthetic */ void setResult(Object obj) {
                super.zzb((Result) obj);
            }

            protected void zza(zzus com_google_android_gms_internal_zzus) throws RemoteException {
                ((com.google.android.gms.auth.account.zzb) com_google_android_gms_internal_zzus.zzxD()).zzad(z);
            }

            protected Result zzc(Status status) {
                return null;
            }
        });
    }
}
