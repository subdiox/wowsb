package com.yunva.im.sdk.lib.dynamicload.utils;

import android.content.Context;
import com.yunva.im.sdk.lib.utils.MyLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsUtils {
    private static String TAG = "AssetsUtil";

    public static boolean isExistFile(Context context, String fileName) {
        try {
            String[] fileNames = context.getAssets().list("");
            if (fileNames == null || fileNames.length <= 0) {
                return false;
            }
            for (String contains : fileNames) {
                if (contains.contains(fileName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyAssetsToSD(Context context, String sourceFileName, String targetFolder, boolean isOverWritten) {
        Exception e;
        Throwable th;
        File mWorkingPath = new File(targetFolder);
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            File outFile = new File(mWorkingPath, sourceFileName);
            if (outFile.exists()) {
                if (isOverWritten) {
                    outFile.delete();
                } else {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e2) {
                            MyLog.w(TAG, "e:" + e2.toString());
                        }
                    }
                    if (out != null) {
                        out.close();
                    }
                    return true;
                }
            }
            in = context.getAssets().open(sourceFileName);
            OutputStream out2 = new FileOutputStream(outFile);
            if (in != null) {
                try {
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = in.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        out2.write(buf, 0, len);
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    out = out2;
                    try {
                        MyLog.w(TAG, "e:" + e2.toString());
                        throw new RuntimeException("yayavoice_for_assets.jar is not found ...");
                    } catch (Throwable th2) {
                        th = th2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e22) {
                                MyLog.w(TAG, "e:" + e22.toString());
                                throw th;
                            }
                        }
                        if (out != null) {
                            out.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = out2;
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e222) {
                    MyLog.w(TAG, "e:" + e222.toString());
                }
            }
            if (out2 != null) {
                out2.close();
            }
            out = out2;
            return true;
        } catch (Exception e4) {
            e222 = e4;
            MyLog.w(TAG, "e:" + e222.toString());
            throw new RuntimeException("yayavoice_for_assets.jar is not found ...");
        }
    }

    public static boolean copyAssetsToPhone(Context context, String fileName) {
        try {
            InputStream in = context.getAssets().open(fileName);
            FileOutputStream fos = context.openFileOutput(fileName, 3);
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    fos.write(buf, 0, len);
                } else {
                    in.close();
                    fos.close();
                    return true;
                }
            }
        } catch (Exception e) {
            MyLog.w(TAG, "e:" + e.toString());
            return false;
        }
    }
}
