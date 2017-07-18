package com.google.android.gms.internal;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;

public abstract class zzaae extends zzabe implements OnCancelListener {
    protected boolean mStarted;
    private ConnectionResult zzaAa;
    private int zzaAb;
    private final Handler zzaAc;
    protected boolean zzazZ;
    protected final GoogleApiAvailability zzazn;

    private class zza implements Runnable {
        final /* synthetic */ zzaae zzaAd;

        private zza(zzaae com_google_android_gms_internal_zzaae) {
            this.zzaAd = com_google_android_gms_internal_zzaae;
        }

        @MainThread
        public void run() {
            if (!this.zzaAd.mStarted) {
                return;
            }
            if (this.zzaAd.zzaAa.hasResolution()) {
                this.zzaAd.zzaCR.startActivityForResult(GoogleApiActivity.zzb(this.zzaAd.getActivity(), this.zzaAd.zzaAa.getResolution(), this.zzaAd.zzaAb, false), 1);
            } else if (this.zzaAd.zzazn.isUserResolvableError(this.zzaAd.zzaAa.getErrorCode())) {
                this.zzaAd.zzazn.zza(this.zzaAd.getActivity(), this.zzaAd.zzaCR, this.zzaAd.zzaAa.getErrorCode(), 2, this.zzaAd);
            } else if (this.zzaAd.zzaAa.getErrorCode() == 18) {
                final Dialog zza = this.zzaAd.zzazn.zza(this.zzaAd.getActivity(), this.zzaAd);
                this.zzaAd.zzazn.zza(this.zzaAd.getActivity().getApplicationContext(), new com.google.android.gms.internal.zzaaz.zza(this) {
                    final /* synthetic */ zza zzaAf;

                    public void zzvE() {
                        this.zzaAf.zzaAd.zzvD();
                        if (zza.isShowing()) {
                            zza.dismiss();
                        }
                    }
                });
            } else {
                this.zzaAd.zza(this.zzaAd.zzaAa, this.zzaAd.zzaAb);
            }
        }
    }

    protected zzaae(zzabf com_google_android_gms_internal_zzabf) {
        this(com_google_android_gms_internal_zzabf, GoogleApiAvailability.getInstance());
    }

    zzaae(zzabf com_google_android_gms_internal_zzabf, GoogleApiAvailability googleApiAvailability) {
        super(com_google_android_gms_internal_zzabf);
        this.zzaAb = -1;
        this.zzaAc = new Handler(Looper.getMainLooper());
        this.zzazn = googleApiAvailability;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r6, int r7, android.content.Intent r8) {
        /*
        r5 = this;
        r4 = 18;
        r2 = 13;
        r0 = 1;
        r1 = 0;
        switch(r6) {
            case 1: goto L_0x0027;
            case 2: goto L_0x0010;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = r1;
    L_0x000a:
        if (r0 == 0) goto L_0x003d;
    L_0x000c:
        r5.zzvD();
    L_0x000f:
        return;
    L_0x0010:
        r2 = r5.zzazn;
        r3 = r5.getActivity();
        r2 = r2.isGooglePlayServicesAvailable(r3);
        if (r2 != 0) goto L_0x0047;
    L_0x001c:
        r1 = r5.zzaAa;
        r1 = r1.getErrorCode();
        if (r1 != r4) goto L_0x000a;
    L_0x0024:
        if (r2 != r4) goto L_0x000a;
    L_0x0026:
        goto L_0x000f;
    L_0x0027:
        r3 = -1;
        if (r7 == r3) goto L_0x000a;
    L_0x002a:
        if (r7 != 0) goto L_0x0009;
    L_0x002c:
        if (r8 == 0) goto L_0x0045;
    L_0x002e:
        r0 = "<<ResolutionFailureErrorDetail>>";
        r0 = r8.getIntExtra(r0, r2);
    L_0x0034:
        r2 = new com.google.android.gms.common.ConnectionResult;
        r3 = 0;
        r2.<init>(r0, r3);
        r5.zzaAa = r2;
        goto L_0x0009;
    L_0x003d:
        r0 = r5.zzaAa;
        r1 = r5.zzaAb;
        r5.zza(r0, r1);
        goto L_0x000f;
    L_0x0045:
        r0 = r2;
        goto L_0x0034;
    L_0x0047:
        r0 = r1;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzaae.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), this.zzaAb);
        zzvD();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzazZ = bundle.getBoolean("resolving_error", false);
            if (this.zzazZ) {
                this.zzaAb = bundle.getInt("failed_client_id", -1);
                this.zzaAa = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution"));
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzazZ);
        if (this.zzazZ) {
            bundle.putInt("failed_client_id", this.zzaAb);
            bundle.putInt("failed_status", this.zzaAa.getErrorCode());
            bundle.putParcelable("failed_resolution", this.zzaAa.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    protected abstract void zza(ConnectionResult connectionResult, int i);

    public void zzb(ConnectionResult connectionResult, int i) {
        if (!this.zzazZ) {
            this.zzazZ = true;
            this.zzaAb = i;
            this.zzaAa = connectionResult;
            this.zzaAc.post(new zza());
        }
    }

    protected void zzvD() {
        this.zzaAb = -1;
        this.zzazZ = false;
        this.zzaAa = null;
        zzvx();
    }

    protected abstract void zzvx();
}
