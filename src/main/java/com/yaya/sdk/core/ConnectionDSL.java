package com.yaya.sdk.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.yaya.sdk.constants.Constants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionDSL {
    private static final String TAG = ConnectionDSL.class.getSimpleName();
    private static ConnectionDSL instance_;
    private Context mAppContext;
    private HandlerThread mConnectionThread;

    public void tryCatchFinally(java.lang.Runnable r3, java.lang.Runnable r4, java.lang.Runnable r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0008 in list [B:3:0x0005]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r2 = this;
        r3.run();	 Catch:{ Exception -> 0x0009, all -> 0x0015 }
        if (r5 == 0) goto L_0x0008;
    L_0x0005:
        r5.run();
    L_0x0008:
        return;
    L_0x0009:
        r0 = move-exception;
        if (r4 == 0) goto L_0x000f;
    L_0x000c:
        r4.run();	 Catch:{ Exception -> 0x0009, all -> 0x0015 }
    L_0x000f:
        if (r5 == 0) goto L_0x0008;
    L_0x0011:
        r5.run();
        goto L_0x0008;
    L_0x0015:
        r1 = move-exception;
        if (r5 == 0) goto L_0x001b;
    L_0x0018:
        r5.run();
    L_0x001b:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yaya.sdk.core.ConnectionDSL.tryCatchFinally(java.lang.Runnable, java.lang.Runnable, java.lang.Runnable):void");
    }

    private ConnectionDSL() {
    }

    public static ConnectionDSL getInstance() {
        if (instance_ == null) {
            instance_ = new ConnectionDSL();
        }
        return instance_;
    }

    public void init(Context c) {
        this.mAppContext = c.getApplicationContext();
        this.mConnectionThread = new HandlerThread("Connection");
        this.mConnectionThread.start();
    }

    @SuppressLint({"NewApi"})
    public void destroy() {
        if (VERSION.SDK_INT >= 18) {
            this.mConnectionThread.quitSafely();
        } else {
            this.mConnectionThread.quit();
        }
    }

    public Context getContext() {
        return this.mAppContext;
    }

    public void startDown(String url, String fileName, String path) {
        if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(url)) {
            OprateLib.startDown(url, fileName, path);
        }
    }

    public void getNextCode(int nextId) {
        Context context = Constants.GLOABLE_CONTEXT;
        String requestId = Constants.LAST_REQUEST_ID;
        Integer lastID = Constants.LAST_HELLO_ID;
        if (context != null && !TextUtils.isEmpty(requestId) && lastID.intValue() != 0) {
            OprateLib.pullNextFromServer(nextId, context, requestId, lastID);
        }
    }

    public void logv(String tag, String msg) {
    }

    public void logd(String tag, String msg) {
    }

    public void logi(String tag, String msg) {
    }

    public void logw(String tag, String msg) {
    }

    public void loge(String tag, String msg) {
    }

    public void scheduleTask(Runnable task, int flag) {
        if (task != null) {
            if (flag == 0) {
                new Handler(Looper.getMainLooper()).post(task);
            } else if (flag == 1) {
                new Handler(this.mConnectionThread.getLooper()).post(task);
            } else if (flag == 2) {
                new Thread(task).start();
            }
        }
    }

    public void tryCatch(Runnable r0, Runnable r1) {
        try {
            r0.run();
        } catch (Exception e) {
            if (r1 != null) {
                r1.run();
            }
        }
    }

    public SQLiteDatabase getDbWithPath(String path) {
        SQLiteDatabase sQLiteDatabase = null;
        if (new File(path).exists()) {
            sQLiteDatabase = null;
            try {
                sQLiteDatabase = SQLiteDatabase.openDatabase(path, null, 0);
            } catch (Exception e) {
            }
        }
        return sQLiteDatabase;
    }

    public String httpPost(String url, String body) {
        try {
            URL httpUrl = new URL(url);
            if (body == null) {
                body = "";
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(6000);
                conn.setRequestMethod("POST");
                conn.getOutputStream().write(body.getBytes("utf-8"));
                InputStream is = conn.getInputStream();
                int av = is.available();
                if (av == 0) {
                    return null;
                }
                byte[] data = new byte[av];
                is.read(data);
                return new String(data, "utf-8");
            } catch (IOException e) {
                return null;
            }
        } catch (MalformedURLException e2) {
            return null;
        }
    }
}
