package com.yaya.sdk.utils;

import android.content.Context;
import android.widget.Toast;
import com.yaya.sdk.constants.Constants;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileUtils {
    private static MessageDigest md5;

    public static String getCodeFromFile(String fileName, Context context) {
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] data = new byte[is.available()];
            is.read(data);
            is.close();
            return new String(data);
        } catch (Exception e) {
            Toast.makeText(context, "读取出错", 0).show();
            return "FAIL";
        }
    }

    static {
        md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String string2Md5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if (((x & 255) >> 4) == 0) {
                sb.append("0").append(Integer.toHexString(x & 255));
            } else {
                sb.append(Integer.toHexString(x & 255));
            }
        }
        return sb.toString();
    }

    public static boolean deleteDownloadFile(String filePath) {
        if (filePath == null || filePath.length() <= 0) {
            return true;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void write2File(String log) {
        FileWriter fileWriter;
        Throwable th;
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(Constants.ROOT_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileWriter fileWriter2 = new FileWriter("yaya_jb/ddd.txt", true);
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
                try {
                    bufferedWriter2.write(log + "\n");
                    bufferedWriter2.flush();
                    bufferedWriter2.close();
                    fileWriter2.close();
                    try {
                        bufferedWriter2.close();
                        bufferedWriter = bufferedWriter2;
                        fileWriter = fileWriter2;
                    } catch (IOException e) {
                        bufferedWriter = bufferedWriter2;
                        fileWriter = fileWriter2;
                    }
                } catch (Exception e2) {
                    bufferedWriter = bufferedWriter2;
                    fileWriter = fileWriter2;
                    try {
                        bufferedWriter.close();
                    } catch (IOException e3) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedWriter = bufferedWriter2;
                    fileWriter = fileWriter2;
                    try {
                        bufferedWriter.close();
                    } catch (IOException e4) {
                    }
                    throw th;
                }
            } catch (Exception e5) {
                fileWriter = fileWriter2;
                bufferedWriter.close();
            } catch (Throwable th3) {
                th = th3;
                fileWriter = fileWriter2;
                bufferedWriter.close();
                throw th;
            }
        } catch (Exception e6) {
            bufferedWriter.close();
        } catch (Throwable th4) {
            th = th4;
            bufferedWriter.close();
            throw th;
        }
    }
}
