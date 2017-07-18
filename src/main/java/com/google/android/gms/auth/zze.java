package com.google.android.gms.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.internal.zzn;
import com.google.android.gms.common.zzg;
import com.google.android.gms.internal.zzacm;
import com.google.android.gms.internal.zzvv;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class zze {
    public static final int CHANGE_TYPE_ACCOUNT_ADDED = 1;
    public static final int CHANGE_TYPE_ACCOUNT_REMOVED = 2;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_FROM = 3;
    public static final int CHANGE_TYPE_ACCOUNT_RENAMED_TO = 4;
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    @SuppressLint({"InlinedApi"})
    public static final String KEY_ANDROID_PACKAGE_NAME = "androidPackageName";
    @SuppressLint({"InlinedApi"})
    public static final String KEY_CALLER_UID = "callerUid";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    public static final String WORK_ACCOUNT_TYPE = "com.google.work";
    private static final zzacm zzaiA = zzd.zzb("GoogleAuthUtil");
    private static final String[] zzaiy = new String[]{"com.google", "com.google.work", "cn.google"};
    private static final ComponentName zzaiz = new ComponentName("com.google.android.gms", "com.google.android.gms.auth.GetToken");

    private interface zza<T> {
        T zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException;
    }

    class AnonymousClass1 implements zza<TokenData> {
        final /* synthetic */ Bundle val$options;
        final /* synthetic */ Account zzaiB;
        final /* synthetic */ String zzaiC;

        AnonymousClass1(Account account, String str, Bundle bundle) {
            this.zzaiB = account;
            this.zzaiC = str;
            this.val$options = bundle;
        }

        public TokenData zzat(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            Bundle bundle = (Bundle) zze.zzn(com.google.android.gms.internal.zzcb.zza.zza(iBinder).zza(this.zzaiB, this.zzaiC, this.val$options));
            TokenData zzd = TokenData.zzd(bundle, "tokenDetails");
            if (zzd != null) {
                return zzd;
            }
            String string = bundle.getString("Error");
            Intent intent = (Intent) bundle.getParcelable("userRecoveryIntent");
            zzvv zzcE = zzvv.zzcE(string);
            if (zzvv.zza(zzcE)) {
                Object[] objArr = new Object[1];
                String valueOf = String.valueOf(zzcE);
                objArr[0] = new StringBuilder(String.valueOf(valueOf).length() + 31).append("isUserRecoverableError status: ").append(valueOf).toString();
                zze.zzaiA.zzf("GoogleAuthUtil", objArr);
                throw new UserRecoverableAuthException(string, intent);
            } else if (zzvv.zzb(zzcE)) {
                throw new IOException(string);
            } else {
                throw new GoogleAuthException(string);
            }
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzat(iBinder);
        }
    }

    class AnonymousClass2 implements zza<Void> {
        final /* synthetic */ String zzaiD;
        final /* synthetic */ Bundle zzaiE;

        AnonymousClass2(String str, Bundle bundle) {
            this.zzaiD = str;
            this.zzaiE = bundle;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzav(iBinder);
        }

        public Void zzav(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            Bundle bundle = (Bundle) zze.zzn(com.google.android.gms.internal.zzcb.zza.zza(iBinder).zza(this.zzaiD, this.zzaiE));
            String string = bundle.getString("Error");
            if (bundle.getBoolean("booleanResult")) {
                return null;
            }
            throw new GoogleAuthException(string);
        }
    }

    class AnonymousClass3 implements zza<List<AccountChangeEvent>> {
        final /* synthetic */ String zzaiF;
        final /* synthetic */ int zzaiG;

        AnonymousClass3(String str, int i) {
            this.zzaiF = str;
            this.zzaiG = i;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzaw(iBinder);
        }

        public List<AccountChangeEvent> zzaw(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return ((AccountChangeEventsResponse) zze.zzn(com.google.android.gms.internal.zzcb.zza.zza(iBinder).zza(new AccountChangeEventsRequest().setAccountName(this.zzaiF).setEventIndex(this.zzaiG)))).getEvents();
        }
    }

    class AnonymousClass4 implements zza<Bundle> {
        final /* synthetic */ Account zzaiB;

        AnonymousClass4(Account account) {
            this.zzaiB = account;
        }

        public /* synthetic */ Object zzau(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return zzax(iBinder);
        }

        public Bundle zzax(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
            return (Bundle) zze.zzn(com.google.android.gms.internal.zzcb.zza.zza(iBinder).zza(this.zzaiB));
        }
    }

    static {
        int i = VERSION.SDK_INT;
        i = VERSION.SDK_INT;
    }

    zze() {
    }

    public static void clearToken(Context context, String str) throws GooglePlayServicesAvailabilityException, GoogleAuthException, IOException {
        zzac.zzdk("Calling this from your main thread can lead to deadlock");
        zzaq(context);
        Bundle bundle = new Bundle();
        String str2 = context.getApplicationInfo().packageName;
        bundle.putString("clientPackageName", str2);
        if (!bundle.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            bundle.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        zza(context, zzaiz, new AnonymousClass2(str, bundle));
    }

    public static List<AccountChangeEvent> getAccountChangeEvents(Context context, int i, String str) throws GoogleAuthException, IOException {
        zzac.zzh(str, "accountName must be provided");
        zzac.zzdk("Calling this from your main thread can lead to deadlock");
        zzaq(context);
        return (List) zza(context, zzaiz, new AnonymousClass3(str, i));
    }

    public static String getAccountId(Context context, String str) throws GoogleAuthException, IOException {
        zzac.zzh(str, "accountName must be provided");
        zzac.zzdk("Calling this from your main thread can lead to deadlock");
        zzaq(context);
        return getToken(context, str, "^^_account_id_^^", new Bundle());
    }

    public static String getToken(Context context, Account account, String str) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, account, str, new Bundle());
    }

    public static String getToken(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        zzc(account);
        return zzc(context, account, str, bundle).getToken();
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, "com.google"), str2);
    }

    @Deprecated
    public static String getToken(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, new Account(str, "com.google"), str2, bundle);
    }

    @RequiresPermission("android.permission.MANAGE_ACCOUNTS")
    @Deprecated
    public static void invalidateToken(Context context, String str) {
        AccountManager.get(context).invalidateAuthToken("com.google", str);
    }

    @TargetApi(23)
    public static Bundle removeAccount(Context context, Account account) throws GoogleAuthException, IOException {
        zzac.zzw(context);
        zzc(account);
        zzaq(context);
        return (Bundle) zza(context, zzaiz, new AnonymousClass4(account));
    }

    private static <T> T zza(Context context, ComponentName componentName, zza<T> com_google_android_gms_auth_zze_zza_T) throws IOException, GoogleAuthException {
        Throwable e;
        ServiceConnection com_google_android_gms_common_zza = new com.google.android.gms.common.zza();
        zzn zzaU = zzn.zzaU(context);
        if (zzaU.zza(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil")) {
            try {
                T zzau = com_google_android_gms_auth_zze_zza_T.zzau(com_google_android_gms_common_zza.zzuX());
                zzaU.zzb(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil");
                return zzau;
            } catch (RemoteException e2) {
                e = e2;
                try {
                    zzaiA.zze("GoogleAuthUtil", "Error on service connection.", e);
                    throw new IOException("Error on service connection.", e);
                } catch (Throwable th) {
                    zzaU.zzb(componentName, com_google_android_gms_common_zza, "GoogleAuthUtil");
                }
            } catch (InterruptedException e3) {
                e = e3;
                zzaiA.zze("GoogleAuthUtil", "Error on service connection.", e);
                throw new IOException("Error on service connection.", e);
            }
        }
        throw new IOException("Could not bind to service.");
    }

    private static void zzaq(Context context) throws GoogleAuthException {
        try {
            zzg.zzaq(context.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            throw new GooglePlayServicesAvailabilityException(e.getConnectionStatusCode(), e.getMessage(), e.getIntent());
        } catch (GooglePlayServicesNotAvailableException e2) {
            throw new GoogleAuthException(e2.getMessage());
        }
    }

    public static TokenData zzc(Context context, Account account, String str, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        zzac.zzdk("Calling this from your main thread can lead to deadlock");
        zzac.zzh(str, "Scope cannot be empty or null.");
        zzc(account);
        zzaq(context);
        Bundle bundle2 = bundle == null ? new Bundle() : new Bundle(bundle);
        String str2 = context.getApplicationInfo().packageName;
        bundle2.putString("clientPackageName", str2);
        if (TextUtils.isEmpty(bundle2.getString(KEY_ANDROID_PACKAGE_NAME))) {
            bundle2.putString(KEY_ANDROID_PACKAGE_NAME, str2);
        }
        bundle2.putLong("service_connection_start_time_millis", SystemClock.elapsedRealtime());
        return (TokenData) zza(context, zzaiz, new AnonymousClass1(account, str, bundle2));
    }

    private static void zzc(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        } else if (TextUtils.isEmpty(account.name)) {
            throw new IllegalArgumentException("Account name cannot be empty!");
        } else {
            String[] strArr = zzaiy;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                if (!strArr[i].equals(account.type)) {
                    i++;
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException("Account type not supported");
        }
    }

    static void zzi(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Callback cannot be null.");
        }
        try {
            Intent.parseUri(intent.toUri(1), 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }

    private static <T> T zzn(T t) throws IOException {
        if (t != null) {
            return t;
        }
        zzaiA.zzf("GoogleAuthUtil", "Binder call returned null.");
        throw new IOException("Service unavailable.");
    }
}
