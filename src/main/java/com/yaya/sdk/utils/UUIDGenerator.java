package com.yaya.sdk.utils;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class UUIDGenerator {
    private static final String UUID_FILENAME = "phone_uuid.tmp";
    private static final String UUID_PATH = "/uuinfo";

    public static String getUUID(Context context) {
        String filePath = getTootPath(context) + UUID_PATH;
        String phone_uuid = readFile(filePath, UUID_FILENAME);
        if (phone_uuid != null && phone_uuid.length() != 0) {
            return phone_uuid;
        }
        phone_uuid = UUID.randomUUID().toString().replaceAll("-", "").trim();
        writeFile(filePath, phone_uuid, UUID_FILENAME);
        return phone_uuid;
    }

    public static String getTempUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").trim();
    }

    public static String getTootPath(Context context) {
        String root = "";
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().getPath().toString() + File.separator + "yaya";
        }
        return context.getFilesDir().getAbsolutePath() + File.separator + "yaya";
    }

    private static void writeFile(String filePath, String body, String fileName) {
        Throwable th;
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(filePath + File.separator + fileName));
            try {
                bw2.write(body);
                bw2.flush();
                if (bw2 != null) {
                    try {
                        bw2.close();
                    } catch (IOException e) {
                        bw = bw2;
                        return;
                    }
                }
                bw = bw2;
            } catch (IOException e2) {
                bw = bw2;
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                bw = bw2;
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            if (bw != null) {
                bw.close();
            }
        } catch (Throwable th3) {
            th = th3;
            if (bw != null) {
                bw.close();
            }
            throw th;
        }
    }

    private static String readFile(String filePath, String fileName) {
        Throwable th;
        String result = "";
        BufferedReader br = null;
        try {
            File file = new File(filePath + File.separator + fileName);
            if (file.exists()) {
                BufferedReader br2 = new BufferedReader(new FileReader(file));
                try {
                    result = br2.readLine();
                    br = br2;
                } catch (IOException e) {
                    br = br2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e2) {
                        }
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
            if (br != null) {
                br.close();
            }
            return result;
        } catch (Throwable th3) {
            th = th3;
            if (br != null) {
                br.close();
            }
            throw th;
        }
        return result;
    }
}
