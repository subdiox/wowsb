package com.google.android.gms.internal;

import com.google.android.gms.auth.api.proxy.AuthApiStatusCodes;
import com.google.android.gms.internal.zzb.zza;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class zzu implements zzg {
    protected static final boolean DEBUG = zzt.DEBUG;
    private static int zzan = AuthApiStatusCodes.AUTH_API_INVALID_CREDENTIALS;
    private static int zzao = 4096;
    protected final zzz zzap;
    protected final zzv zzaq;

    public zzu(zzz com_google_android_gms_internal_zzz) {
        this(com_google_android_gms_internal_zzz, new zzv(zzao));
    }

    public zzu(zzz com_google_android_gms_internal_zzz, zzv com_google_android_gms_internal_zzv) {
        this.zzap = com_google_android_gms_internal_zzz;
        this.zzaq = com_google_android_gms_internal_zzv;
    }

    protected static Map<String, String> zza(Header[] headerArr) {
        Map<String, String> treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private void zza(long j, zzl<?> com_google_android_gms_internal_zzl_, byte[] bArr, StatusLine statusLine) {
        if (DEBUG || j > ((long) zzan)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = com_google_android_gms_internal_zzl_;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(com_google_android_gms_internal_zzl_.zzq().zzd());
            zzt.zzb(str, objArr);
        }
    }

    private static void zza(String str, zzl<?> com_google_android_gms_internal_zzl_, zzs com_google_android_gms_internal_zzs) throws zzs {
        zzp zzq = com_google_android_gms_internal_zzl_.zzq();
        int zzp = com_google_android_gms_internal_zzl_.zzp();
        try {
            zzq.zza(com_google_android_gms_internal_zzs);
            com_google_android_gms_internal_zzl_.zzc(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(zzp)}));
        } catch (zzs e) {
            com_google_android_gms_internal_zzl_.zzc(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(zzp)}));
            throw e;
        }
    }

    private void zza(Map<String, String> map, zza com_google_android_gms_internal_zzb_zza) {
        if (com_google_android_gms_internal_zzb_zza != null) {
            if (com_google_android_gms_internal_zzb_zza.zza != null) {
                map.put("If-None-Match", com_google_android_gms_internal_zzb_zza.zza);
            }
            if (com_google_android_gms_internal_zzb_zza.zzc > 0) {
                map.put("If-Modified-Since", DateUtils.formatDate(new Date(com_google_android_gms_internal_zzb_zza.zzc)));
            }
        }
    }

    private byte[] zza(HttpEntity httpEntity) throws IOException, zzq {
        zzab com_google_android_gms_internal_zzab = new zzab(this.zzaq, (int) httpEntity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new zzq();
            }
            bArr = this.zzaq.zzb(1024);
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                com_google_android_gms_internal_zzab.write(bArr, 0, read);
            }
            byte[] toByteArray = com_google_android_gms_internal_zzab.toByteArray();
            return toByteArray;
        } finally {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                zzt.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzaq.zza(bArr);
            com_google_android_gms_internal_zzab.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.internal.zzj zza(com.google.android.gms.internal.zzl<?> r19) throws com.google.android.gms.internal.zzs {
        /*
        r18 = this;
        r16 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r3 = 0;
        r14 = 0;
        r6 = java.util.Collections.emptyMap();
        r2 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r4 = r19.zzh();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r0 = r18;
        r0.zza(r2, r4);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r0 = r18;
        r4 = r0.zzap;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r0 = r19;
        r15 = r4.zza(r0, r2);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x00de }
        r12 = r15.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r4 = r12.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r2 = r15.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r6 = zza(r2);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r4 != r2) goto L_0x0065;
    L_0x0036:
        r2 = r19.zzh();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        if (r2 != 0) goto L_0x004c;
    L_0x003c:
        r3 = new com.google.android.gms.internal.zzj;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r5 = 0;
        r7 = 1;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
    L_0x004b:
        return r3;
    L_0x004c:
        r3 = r2.zzf;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r3.putAll(r6);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r7 = new com.google.android.gms.internal.zzj;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r8 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r9 = r2.data;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r10 = r2.zzf;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r11 = 1;
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r12 = r2 - r16;
        r7.<init>(r8, r9, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r3 = r7;
        goto L_0x004b;
    L_0x0065:
        r2 = r15.getEntity();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        if (r2 == 0) goto L_0x009f;
    L_0x006b:
        r2 = r15.getEntity();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        r0 = r18;
        r11 = r0.zza(r2);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
    L_0x0075:
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        r8 = r2 - r16;
        r7 = r18;
        r10 = r19;
        r7.zza(r8, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r4 < r2) goto L_0x008a;
    L_0x0086:
        r2 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r4 <= r2) goto L_0x00a3;
    L_0x008a:
        r2 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        throw r2;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
    L_0x0090:
        r2 = move-exception;
        r2 = "socket";
        r3 = new com.google.android.gms.internal.zzr;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x009f:
        r2 = 0;
        r11 = new byte[r2];	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015a }
        goto L_0x0075;
    L_0x00a3:
        r3 = new com.google.android.gms.internal.zzj;	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        r8 = r8 - r16;
        r5 = r11;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x0090, ConnectTimeoutException -> 0x00b1, MalformedURLException -> 0x00c0, IOException -> 0x015e }
        goto L_0x004b;
    L_0x00b1:
        r2 = move-exception;
        r2 = "connection";
        r3 = new com.google.android.gms.internal.zzr;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x00c0:
        r2 = move-exception;
        r3 = new java.lang.RuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Bad URL ";
        r4 = r4.append(r5);
        r5 = r19.getUrl();
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4, r2);
        throw r3;
    L_0x00de:
        r2 = move-exception;
        r5 = r14;
    L_0x00e0:
        if (r3 == 0) goto L_0x0124;
    L_0x00e2:
        r2 = r3.getStatusLine();
        r4 = r2.getStatusCode();
        r2 = "Unexpected response code %d for %s";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r7 = 0;
        r8 = java.lang.Integer.valueOf(r4);
        r3[r7] = r8;
        r7 = 1;
        r8 = r19.getUrl();
        r3[r7] = r8;
        com.google.android.gms.internal.zzt.zzc(r2, r3);
        if (r5 == 0) goto L_0x014c;
    L_0x0102:
        r3 = new com.google.android.gms.internal.zzj;
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);
        r2 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r4 == r2) goto L_0x0116;
    L_0x0112:
        r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r4 != r2) goto L_0x012a;
    L_0x0116:
        r2 = "auth";
        r4 = new com.google.android.gms.internal.zza;
        r4.<init>(r3);
        r0 = r19;
        zza(r2, r0, r4);
        goto L_0x0004;
    L_0x0124:
        r3 = new com.google.android.gms.internal.zzk;
        r3.<init>(r2);
        throw r3;
    L_0x012a:
        r2 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r4 < r2) goto L_0x0138;
    L_0x012e:
        r2 = 499; // 0x1f3 float:6.99E-43 double:2.465E-321;
        if (r4 > r2) goto L_0x0138;
    L_0x0132:
        r2 = new com.google.android.gms.internal.zzd;
        r2.<init>(r3);
        throw r2;
    L_0x0138:
        r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        if (r4 < r2) goto L_0x0146;
    L_0x013c:
        r2 = 599; // 0x257 float:8.4E-43 double:2.96E-321;
        if (r4 > r2) goto L_0x0146;
    L_0x0140:
        r2 = new com.google.android.gms.internal.zzq;
        r2.<init>(r3);
        throw r2;
    L_0x0146:
        r2 = new com.google.android.gms.internal.zzq;
        r2.<init>(r3);
        throw r2;
    L_0x014c:
        r2 = "network";
        r3 = new com.google.android.gms.internal.zzi;
        r3.<init>();
        r0 = r19;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x015a:
        r2 = move-exception;
        r5 = r14;
        r3 = r15;
        goto L_0x00e0;
    L_0x015e:
        r2 = move-exception;
        r5 = r11;
        r3 = r15;
        goto L_0x00e0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzu.zza(com.google.android.gms.internal.zzl):com.google.android.gms.internal.zzj");
    }
}
