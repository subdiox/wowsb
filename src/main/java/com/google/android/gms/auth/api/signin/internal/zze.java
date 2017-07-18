package com.google.android.gms.auth.api.signin.internal;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.internal.zzaax;
import com.google.android.gms.internal.zzabk;
import com.google.android.gms.internal.zzacm;
import java.util.HashSet;

public final class zze {
    private static zzacm zzakx = new zzacm("GoogleSignInCommon", new String[0]);

    private static abstract class zza<R extends Result> extends com.google.android.gms.internal.zzaad.zza<R, zzd> {
        public zza(GoogleApiClient googleApiClient) {
            super(Auth.GOOGLE_SIGN_IN_API, googleApiClient);
        }

        public /* synthetic */ void setResult(Object obj) {
            super.zzb((Result) obj);
        }
    }

    class AnonymousClass1 extends zza<GoogleSignInResult> {
        final /* synthetic */ zzn zzaky;
        final /* synthetic */ GoogleSignInOptions zzakz;

        AnonymousClass1(GoogleApiClient googleApiClient, zzn com_google_android_gms_auth_api_signin_internal_zzn, GoogleSignInOptions googleSignInOptions) {
            this.zzaky = com_google_android_gms_auth_api_signin_internal_zzn;
            this.zzakz = googleSignInOptions;
            super(googleApiClient);
        }

        protected void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd) throws RemoteException {
            ((zzk) com_google_android_gms_auth_api_signin_internal_zzd.zzxD()).zza(new zza(this) {
                final /* synthetic */ AnonymousClass1 zzakA;

                {
                    this.zzakA = r1;
                }

                public void zza(GoogleSignInAccount googleSignInAccount, Status status) throws RemoteException {
                    if (googleSignInAccount != null) {
                        this.zzakA.zzaky.zzb(googleSignInAccount, this.zzakA.zzakz);
                    }
                    this.zzakA.zzb(new GoogleSignInResult(googleSignInAccount, status));
                }
            }, this.zzakz);
        }

        protected /* synthetic */ Result zzc(Status status) {
            return zzn(status);
        }

        protected GoogleSignInResult zzn(Status status) {
            return new GoogleSignInResult(null, status);
        }
    }

    class AnonymousClass2 extends zza<Status> {
        AnonymousClass2(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        protected void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd) throws RemoteException {
            ((zzk) com_google_android_gms_auth_api_signin_internal_zzd.zzxD()).zzb(new zza(this) {
                final /* synthetic */ AnonymousClass2 zzakB;

                {
                    this.zzakB = r1;
                }

                public void zzl(Status status) throws RemoteException {
                    this.zzakB.zzb(status);
                }
            }, com_google_android_gms_auth_api_signin_internal_zzd.zzrt());
        }

        protected Status zzb(Status status) {
            return status;
        }

        protected /* synthetic */ Result zzc(Status status) {
            return zzb(status);
        }
    }

    class AnonymousClass3 extends zza<Status> {
        AnonymousClass3(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        protected void zza(zzd com_google_android_gms_auth_api_signin_internal_zzd) throws RemoteException {
            ((zzk) com_google_android_gms_auth_api_signin_internal_zzd.zzxD()).zzc(new zza(this) {
                final /* synthetic */ AnonymousClass3 zzakC;

                {
                    this.zzakC = r1;
                }

                public void zzm(Status status) throws RemoteException {
                    this.zzakC.zzb(status);
                }
            }, com_google_android_gms_auth_api_signin_internal_zzd.zzrt());
        }

        protected Status zzb(Status status) {
            return status;
        }

        protected /* synthetic */ Result zzc(Status status) {
            return zzb(status);
        }
    }

    public static GoogleSignInResult getSignInResultFromIntent(Intent intent) {
        if (intent == null || (!intent.hasExtra("googleSignInStatus") && !intent.hasExtra("googleSignInAccount"))) {
            return null;
        }
        GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) intent.getParcelableExtra("googleSignInAccount");
        Status status = (Status) intent.getParcelableExtra("googleSignInStatus");
        if (googleSignInAccount != null) {
            status = Status.zzazx;
        }
        return new GoogleSignInResult(googleSignInAccount, status);
    }

    public static Intent zza(Context context, GoogleSignInOptions googleSignInOptions) {
        zzakx.zzb("GoogleSignInCommon", "getSignInIntent()");
        Parcelable signInConfiguration = new SignInConfiguration(context.getPackageName(), googleSignInOptions);
        Intent intent = new Intent("com.google.android.gms.auth.GOOGLE_SIGN_IN");
        intent.setClass(context, SignInHubActivity.class);
        intent.putExtra("config", signInConfiguration);
        return intent;
    }

    static GoogleSignInResult zza(zzn com_google_android_gms_auth_api_signin_internal_zzn, GoogleSignInOptions googleSignInOptions) {
        zzakx.zzb("GoogleSignInCommon", "getEligibleSavedSignInResult()");
        zzac.zzw(googleSignInOptions);
        GoogleSignInOptions zzrC = com_google_android_gms_auth_api_signin_internal_zzn.zzrC();
        if (zzrC == null || !zza(zzrC.getAccount(), googleSignInOptions.getAccount()) || googleSignInOptions.zzrk()) {
            return null;
        }
        if ((googleSignInOptions.isIdTokenRequested() && (!zzrC.isIdTokenRequested() || !googleSignInOptions.getServerClientId().equals(zzrC.getServerClientId()))) || !new HashSet(zzrC.zzrj()).containsAll(new HashSet(googleSignInOptions.zzrj()))) {
            return null;
        }
        GoogleSignInAccount zzrB = com_google_android_gms_auth_api_signin_internal_zzn.zzrB();
        return (zzrB == null || zzrB.zza()) ? null : new GoogleSignInResult(zzrB, Status.zzazx);
    }

    public static OptionalPendingResult<GoogleSignInResult> zza(GoogleApiClient googleApiClient, Context context, GoogleSignInOptions googleSignInOptions) {
        zzn zzas = zzn.zzas(context);
        Result zza = zza(zzas, googleSignInOptions);
        if (zza == null) {
            return zza(googleApiClient, zzas, googleSignInOptions);
        }
        zzakx.zzb("GoogleSignInCommon", "Eligible saved sign in result found");
        return PendingResults.zzb(zza, googleApiClient);
    }

    private static OptionalPendingResult<GoogleSignInResult> zza(GoogleApiClient googleApiClient, zzn com_google_android_gms_auth_api_signin_internal_zzn, GoogleSignInOptions googleSignInOptions) {
        zzakx.zzb("GoogleSignInCommon", "trySilentSignIn()");
        return new zzabk(googleApiClient.zza(new AnonymousClass1(googleApiClient, com_google_android_gms_auth_api_signin_internal_zzn, googleSignInOptions)));
    }

    public static PendingResult<Status> zza(GoogleApiClient googleApiClient, Context context) {
        zzakx.zzb("GoogleSignInCommon", "Signing out");
        zzar(context);
        return googleApiClient.zzb(new AnonymousClass2(googleApiClient));
    }

    private static boolean zza(Account account, Account account2) {
        return account == null ? account2 == null : account.equals(account2);
    }

    private static void zzar(Context context) {
        zzn.zzas(context).zzrD();
        for (GoogleApiClient zzvn : GoogleApiClient.zzvm()) {
            zzvn.zzvn();
        }
        zzaax.zzwx();
    }

    public static PendingResult<Status> zzb(GoogleApiClient googleApiClient, Context context) {
        zzakx.zzb("GoogleSignInCommon", "Revoking access");
        zzar(context);
        return googleApiClient.zzb(new AnonymousClass3(googleApiClient));
    }
}
