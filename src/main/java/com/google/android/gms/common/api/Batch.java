package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.internal.zzaaf;
import java.util.ArrayList;
import java.util.List;

public final class Batch extends zzaaf<BatchResult> {
    private int zzayM;
    private boolean zzayN;
    private boolean zzayO;
    private final PendingResult<?>[] zzayP;
    private final Object zzrJ;

    public static final class Builder {
        private GoogleApiClient zzanE;
        private List<PendingResult<?>> zzayR = new ArrayList();

        public Builder(GoogleApiClient googleApiClient) {
            this.zzanE = googleApiClient;
        }

        public <R extends Result> BatchResultToken<R> add(PendingResult<R> pendingResult) {
            BatchResultToken<R> batchResultToken = new BatchResultToken(this.zzayR.size());
            this.zzayR.add(pendingResult);
            return batchResultToken;
        }

        public Batch build() {
            return new Batch(this.zzayR, this.zzanE);
        }
    }

    private Batch(List<PendingResult<?>> list, GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzrJ = new Object();
        this.zzayM = list.size();
        this.zzayP = new PendingResult[this.zzayM];
        if (list.isEmpty()) {
            zzb(new BatchResult(Status.zzazx, this.zzayP));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            PendingResult pendingResult = (PendingResult) list.get(i);
            this.zzayP[i] = pendingResult;
            pendingResult.zza(new zza(this) {
                final /* synthetic */ Batch zzayQ;

                {
                    this.zzayQ = r1;
                }

                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void zzy(com.google.android.gms.common.api.Status r6) {
                    /*
                    r5 = this;
                    r0 = r5.zzayQ;
                    r1 = r0.zzrJ;
                    monitor-enter(r1);
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r0 = r0.isCanceled();	 Catch:{ all -> 0x0039 }
                    if (r0 == 0) goto L_0x0011;
                L_0x000f:
                    monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                L_0x0010:
                    return;
                L_0x0011:
                    r0 = r6.isCanceled();	 Catch:{ all -> 0x0039 }
                    if (r0 == 0) goto L_0x003c;
                L_0x0017:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r2 = 1;
                    r0.zzayO = r2;	 Catch:{ all -> 0x0039 }
                L_0x001d:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r0.zzayM = r0.zzayM - 1;	 Catch:{ all -> 0x0039 }
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r0 = r0.zzayM;	 Catch:{ all -> 0x0039 }
                    if (r0 != 0) goto L_0x0037;
                L_0x002a:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r0 = r0.zzayO;	 Catch:{ all -> 0x0039 }
                    if (r0 == 0) goto L_0x0049;
                L_0x0032:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    super.cancel();	 Catch:{ all -> 0x0039 }
                L_0x0037:
                    monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                    goto L_0x0010;
                L_0x0039:
                    r0 = move-exception;
                    monitor-exit(r1);	 Catch:{ all -> 0x0039 }
                    throw r0;
                L_0x003c:
                    r0 = r6.isSuccess();	 Catch:{ all -> 0x0039 }
                    if (r0 != 0) goto L_0x001d;
                L_0x0042:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r2 = 1;
                    r0.zzayN = r2;	 Catch:{ all -> 0x0039 }
                    goto L_0x001d;
                L_0x0049:
                    r0 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r0 = r0.zzayN;	 Catch:{ all -> 0x0039 }
                    if (r0 == 0) goto L_0x0069;
                L_0x0051:
                    r0 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x0039 }
                    r2 = 13;
                    r0.<init>(r2);	 Catch:{ all -> 0x0039 }
                L_0x0058:
                    r2 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r3 = new com.google.android.gms.common.api.BatchResult;	 Catch:{ all -> 0x0039 }
                    r4 = r5.zzayQ;	 Catch:{ all -> 0x0039 }
                    r4 = r4.zzayP;	 Catch:{ all -> 0x0039 }
                    r3.<init>(r0, r4);	 Catch:{ all -> 0x0039 }
                    r2.zzb(r3);	 Catch:{ all -> 0x0039 }
                    goto L_0x0037;
                L_0x0069:
                    r0 = com.google.android.gms.common.api.Status.zzazx;	 Catch:{ all -> 0x0039 }
                    goto L_0x0058;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.Batch.1.zzy(com.google.android.gms.common.api.Status):void");
                }
            });
        }
    }

    public void cancel() {
        super.cancel();
        for (PendingResult cancel : this.zzayP) {
            cancel.cancel();
        }
    }

    public BatchResult createFailedResult(Status status) {
        return new BatchResult(status, this.zzayP);
    }

    public /* synthetic */ Result zzc(Status status) {
        return createFailedResult(status);
    }
}
