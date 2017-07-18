package com.appsflyer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.UUID;

class g {
    private static String sID = null;

    public static synchronized String id(WeakReference<Context> context) {
        String str;
        synchronized (g.class) {
            if (context.get() == null) {
                str = sID;
            } else {
                if (sID == null) {
                    str = readInstallationSP(context);
                    if (str != null) {
                        sID = str;
                    } else {
                        try {
                            File file = new File(((Context) context.get()).getFilesDir(), "AF_INSTALLATION");
                            if (file.exists()) {
                                sID = readInstallationFile(file);
                                file.delete();
                            } else {
                                sID = generateId(context);
                            }
                            writeInstallationSP(context, sID);
                        } catch (Throwable e) {
                            a.afLogE("Error getting AF unique ID", e);
                        }
                    }
                }
                str = sID;
            }
        }
        return str;
    }

    private static String readInstallationFile(File installation) {
        RandomAccessFile randomAccessFile;
        Throwable th;
        byte[] bArr = null;
        try {
            randomAccessFile = new RandomAccessFile(installation, "r");
            try {
                bArr = new byte[((int) randomAccessFile.length())];
                randomAccessFile.readFully(bArr);
                randomAccessFile.close();
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e3) {
                    }
                }
                if (bArr == null) {
                    bArr = new byte[0];
                }
                return new String(bArr);
            } catch (Throwable th2) {
                th = th2;
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            randomAccessFile = null;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            if (bArr == null) {
                bArr = new byte[0];
            }
            return new String(bArr);
        } catch (Throwable th3) {
            Throwable th4 = th3;
            randomAccessFile = null;
            th = th4;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            throw th;
        }
        if (bArr == null) {
            bArr = new byte[0];
        }
        return new String(bArr);
    }

    private static String generateId(WeakReference<Context> context) throws NameNotFoundException {
        PackageInfo packageInfo = ((Context) context.get()).getPackageManager().getPackageInfo(((Context) context.get()).getPackageName(), 0);
        if (VERSION.SDK_INT >= 9) {
            return packageInfo.firstInstallTime + "-" + Math.abs(new Random().nextLong());
        }
        return UUID.randomUUID().toString();
    }

    private static String readInstallationSP(WeakReference<Context> context) {
        if (context.get() == null) {
            return null;
        }
        return ((Context) context.get()).getSharedPreferences("appsflyer-data", 0).getString("AF_INSTALLATION", null);
    }

    @SuppressLint({"CommitPrefEdits"})
    private static void writeInstallationSP(WeakReference<Context> context, String sId) throws NameNotFoundException {
        Editor edit = ((Context) context.get()).getSharedPreferences("appsflyer-data", 0).edit();
        edit.putString("AF_INSTALLATION", sId);
        if (VERSION.SDK_INT >= 9) {
            edit.apply();
        } else {
            edit.commit();
        }
    }
}
