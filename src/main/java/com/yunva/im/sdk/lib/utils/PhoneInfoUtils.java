package com.yunva.im.sdk.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Display;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PhoneInfoUtils {
    public static final byte CPU_TYPE_ARM_V5 = (byte) 1;
    public static final byte CPU_TYPE_ARM_V6 = (byte) 2;
    public static final byte CPU_TYPE_ARM_V7 = (byte) 3;
    public static final byte CPU_TYPE_DEFAULT = (byte) 0;
    public static final byte NETWORK_TYPE_2G = (byte) 3;
    public static final byte NETWORK_TYPE_3G = (byte) 2;
    public static final byte NETWORK_TYPE_INVALID = (byte) 0;
    public static final byte NETWORK_TYPE_WIFI = (byte) 1;
    private static final String TAG = "PhoneInfoUtil";

    public static String getImsi(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (imsi != null && !imsi.equals("")) {
            return imsi;
        }
        imsi = getDoubleImsi(context, 0);
        if (imsi == null || imsi.equals("")) {
            return getDoubleImsi(context, 1);
        }
        return imsi;
    }

    private static String getDoubleImsi(Context context, int cardID) {
        String imsi = null;
        if (context == null || cardID < 0 || cardID > 1) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        try {
            Object ob_imsi = Class.forName("android.telephony.TelephonyManager").getMethod("getSubscriberIdGemini", new Class[]{Integer.TYPE}).invoke(telephonyManager, new Object[]{Integer.valueOf(cardID)});
            if (ob_imsi != null) {
                imsi = ob_imsi.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imsi;
    }

    public static String getImei(Context context) {
        String imei = "000000000000000";
        try {
            imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            MyLog.w(TAG, "PhoneInfoUtil：001:" + e.toString());
        }
        return imei;
    }

    public static String getSimOperator(Context context) {
        String operator = "";
        try {
            operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        } catch (Exception e) {
            MyLog.w(TAG, "PhoneInfoUtil：002:" + e.toString());
        }
        return operator;
    }

    public static String getSimOperatorName(Context context) {
        String operatorName = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm.getSimState() == 5) {
                operatorName = tm.getSimOperatorName();
            }
        } catch (Exception e) {
            MyLog.w(TAG, "PhoneInfoUtil：003:" + e.toString());
        }
        return operatorName;
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static int getSystemVersion() {
        return VERSION.SDK_INT;
    }

    public static short getTerminalVersion() {
        return (short) Integer.parseInt(VERSION.RELEASE.replace(".", "").substring(0, 2));
    }

    public static String getWifiMacAddress(Context context) {
        String macAddress = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
        WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
        }
        if (macAddress == null) {
            return "";
        }
        return macAddress;
    }

    public static byte getCpuType() {
        String cpuName = getCpuName();
        if (cpuName == null || cpuName.length() == 0) {
            return (byte) 0;
        }
        if (cpuName.contains("ARMv7")) {
            return (byte) 3;
        }
        if (cpuName.contains("ARMv6")) {
            return (byte) 2;
        }
        if (cpuName.contains("ARMv5")) {
            return (byte) 1;
        }
        return (byte) 0;
    }

    private static String getCpuName() {
        BufferedReader br;
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        FileReader fr = null;
        BufferedReader br2 = null;
        String[] array = null;
        try {
            FileReader fr2 = new FileReader("/proc/cpuinfo");
            try {
                br = new BufferedReader(fr2);
            } catch (FileNotFoundException e3) {
                e = e3;
                fr = fr2;
                try {
                    e.printStackTrace();
                    if (br2 != null) {
                        try {
                            br2.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    if (array != null) {
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (br2 != null) {
                        try {
                            br2.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            throw th;
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e222 = e4;
                fr = fr2;
                e222.printStackTrace();
                if (br2 != null) {
                    try {
                        br2.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                fr = fr2;
                if (br2 != null) {
                    br2.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
            try {
                array = br.readLine().split(":\\s+", 2);
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                        br2 = br;
                        fr = fr2;
                    }
                }
                if (fr2 != null) {
                    fr2.close();
                }
                br2 = br;
                fr = fr2;
            } catch (FileNotFoundException e5) {
                e = e5;
                br2 = br;
                fr = fr2;
                e.printStackTrace();
                if (br2 != null) {
                    br2.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (IOException e6) {
                e22222 = e6;
                br2 = br;
                fr = fr2;
                e22222.printStackTrace();
                if (br2 != null) {
                    br2.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (Throwable th4) {
                th = th4;
                br2 = br;
                fr = fr2;
                if (br2 != null) {
                    br2.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            e.printStackTrace();
            if (br2 != null) {
                br2.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (array != null) {
            }
            return null;
        } catch (IOException e8) {
            e22222 = e8;
            e22222.printStackTrace();
            if (br2 != null) {
                br2.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (array != null) {
            }
            return null;
        }
        if (array != null || array.length < 2) {
            return null;
        }
        return array[1];
    }

    public static byte getNetWorkType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return (byte) 0;
        }
        int nType = networkInfo.getType();
        if (nType == 1) {
            return (byte) 1;
        }
        if (nType != 0) {
            return (byte) 0;
        }
        return isFastMobileNetwork(context) ? (byte) 2 : (byte) 3;
    }

    private static boolean isFastMobileNetwork(Context context) {
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 3:
                return true;
            case 5:
                return true;
            case 6:
                return true;
            case 8:
                return true;
            case 9:
                return true;
            case 10:
                return true;
            case 12:
                return true;
            case 13:
                return true;
            case 14:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    public static String getDisplayStr(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getWidth() + "x" + display.getHeight();
    }

    public static boolean isSdRun() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean checkWriteExPermision(Context context) {
        boolean permission;
        String writeSdPermission = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (context.getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()) == 0) {
            permission = true;
        } else {
            permission = false;
        }
        if (permission && VERSION.SDK_INT < 25) {
            return true;
        }
        return false;
    }

    public static boolean checkAlertPermision(Context context) {
        String writeSdPermission = "android.permission.SYSTEM_ALERT_WINDOW";
        boolean permission = context.getPackageManager().checkPermission("android.permission.SYSTEM_ALERT_WINDOW", context.getPackageName()) == 0;
        MyLog.d("", "checkAlertPermision-1-permission" + permission);
        return permission;
    }

    public static int getWindowLayoutType(Context context) {
        int persion = 2005;
        if (VERSION.SDK_INT < 19) {
            return 2002;
        }
        checkAlertPermision(context);
        if (context != null && Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            persion = checkAlertPermision(context) ? 2003 : -5;
        }
        return persion;
    }
}
