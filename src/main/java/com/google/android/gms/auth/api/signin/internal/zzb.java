package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzabq;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class zzb extends AsyncTaskLoader<Void> implements zzabq {
    private Semaphore zzaku = new Semaphore(0);
    private Set<GoogleApiClient> zzakv;

    public zzb(Context context, Set<GoogleApiClient> set) {
        super(context);
        this.zzakv = set;
    }

    public /* synthetic */ Object loadInBackground() {
        return zzrp();
    }

    protected void onStartLoading() {
        this.zzaku.drainPermits();
        forceLoad();
    }

    public Void zzrp() {
        int i = 0;
        for (GoogleApiClient zza : this.zzakv) {
            i = zza.zza((zzabq) this) ? i + 1 : i;
        }
        try {
            this.zzaku.tryAcquire(i, 5, TimeUnit.SECONDS);
        } catch (Throwable e) {
            Log.i("GACSignInLoader", "Unexpected InterruptedException", e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public void zzrq() {
        this.zzaku.release();
    }
}
