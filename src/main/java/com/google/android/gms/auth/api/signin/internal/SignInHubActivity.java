package com.google.android.gms.auth.api.signin.internal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

@KeepName
public class SignInHubActivity extends FragmentActivity {
    private zzn zzakI;
    private SignInConfiguration zzakJ;
    private boolean zzakK;
    private int zzakL;
    private Intent zzakM;

    private class zza implements LoaderCallbacks<Void> {
        final /* synthetic */ SignInHubActivity zzakN;

        private zza(SignInHubActivity signInHubActivity) {
            this.zzakN = signInHubActivity;
        }

        public Loader<Void> onCreateLoader(int i, Bundle bundle) {
            return new zzb(this.zzakN, GoogleApiClient.zzvm());
        }

        public /* synthetic */ void onLoadFinished(Loader loader, Object obj) {
            zza(loader, (Void) obj);
        }

        public void onLoaderReset(Loader<Void> loader) {
        }

        public void zza(Loader<Void> loader, Void voidR) {
            this.zzakN.setResult(this.zzakN.zzakL, this.zzakN.zzakM);
            this.zzakN.finish();
        }
    }

    private void zza(int i, Intent intent) {
        if (intent != null) {
            SignInAccount signInAccount = (SignInAccount) intent.getParcelableExtra(GoogleSignInApi.EXTRA_SIGN_IN_ACCOUNT);
            if (signInAccount != null && signInAccount.zzro() != null) {
                Parcelable zzro = signInAccount.zzro();
                this.zzakI.zzb(zzro, this.zzakJ.zzrz());
                intent.removeExtra(GoogleSignInApi.EXTRA_SIGN_IN_ACCOUNT);
                intent.putExtra("googleSignInAccount", zzro);
                this.zzakK = true;
                this.zzakL = i;
                this.zzakM = intent;
                zzrA();
                return;
            } else if (intent.hasExtra("errorCode")) {
                zzbq(intent.getIntExtra("errorCode", 8));
                return;
            }
        }
        zzbq(8);
    }

    private void zzbq(int i) {
        Parcelable status = new Status(i);
        Intent intent = new Intent();
        intent.putExtra("googleSignInStatus", status);
        setResult(0, intent);
        finish();
    }

    private void zzj(Intent intent) {
        intent.setPackage("com.google.android.gms");
        intent.putExtra("config", this.zzakJ);
        try {
            startActivityForResult(intent, 40962);
        } catch (ActivityNotFoundException e) {
            Log.w("AuthSignInClient", "Could not launch sign in Intent. Google Play Service is probably being updated...");
            zzbq(8);
        }
    }

    private void zzrA() {
        getSupportLoaderManager().initLoader(0, null, new zza());
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return true;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        setResult(0);
        switch (i) {
            case 40962:
                zza(i2, intent);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzakI = zzn.zzas(this);
        Intent intent = getIntent();
        if (!"com.google.android.gms.auth.GOOGLE_SIGN_IN".equals(intent.getAction())) {
            String str = "AuthSignInClient";
            String str2 = "Unknown action: ";
            String valueOf = String.valueOf(intent.getAction());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            finish();
        }
        this.zzakJ = (SignInConfiguration) intent.getParcelableExtra("config");
        if (this.zzakJ == null) {
            Log.e("AuthSignInClient", "Activity started with invalid configuration.");
            setResult(0);
            finish();
        } else if (bundle == null) {
            zzj(new Intent("com.google.android.gms.auth.GOOGLE_SIGN_IN"));
        } else {
            this.zzakK = bundle.getBoolean("signingInGoogleApiClients");
            if (this.zzakK) {
                this.zzakL = bundle.getInt("signInResultCode");
                this.zzakM = (Intent) bundle.getParcelable("signInResultData");
                zzrA();
            }
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("signingInGoogleApiClients", this.zzakK);
        if (this.zzakK) {
            bundle.putInt("signInResultCode", this.zzakL);
            bundle.putParcelable("signInResultData", this.zzakM);
        }
    }
}
