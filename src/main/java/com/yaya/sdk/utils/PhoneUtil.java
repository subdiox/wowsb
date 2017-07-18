package com.yaya.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class PhoneUtil {
    public static final String CPU_TYPE_ARM_V5 = "1";
    public static final String CPU_TYPE_ARM_V6 = "2";
    public static final String CPU_TYPE_ARM_V7 = "3";
    public static final String CPU_TYPE_DEFAULT = "0";
    private static final String TAG = "TelephonyUtil";
    private static final String UUID_FILENAME = "phone_uuid.tmp";
    private static final String UUID_PATH = "/uuinfo";

    public static String getOsType() {
        return "1";
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return null;
        }
    }

    public static int getAppVersionCode(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return i;
        }
    }

    public static String getEnvironment(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("environment");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCpuType() {
        String cpuName = getCpuName();
        if (cpuName == null) {
            return "0";
        }
        if (cpuName.contains("ARMv7")) {
            return CPU_TYPE_ARM_V7;
        }
        if (cpuName.contains("ARMv6")) {
            return CPU_TYPE_ARM_V6;
        }
        if (cpuName.contains("ARMv5")) {
            return "1";
        }
        return "0";
    }

    public static String getCpuName() {
        Throwable th;
        FileReader fr = null;
        BufferedReader br = null;
        String[] array = null;
        try {
            BufferedReader br2;
            FileReader fr2 = new FileReader("/proc/cpuinfo");
            try {
                br2 = new BufferedReader(fr2);
            } catch (FileNotFoundException e) {
                fr = fr2;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e2) {
                    }
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (IOException e3) {
                fr = fr2;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e4) {
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
                fr = fr2;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e5) {
                        throw th;
                    }
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
            try {
                array = br2.readLine().split(":\\s+", 2);
                if (br2 != null) {
                    try {
                        br2.close();
                    } catch (IOException e6) {
                        br = br2;
                        fr = fr2;
                    }
                }
                if (fr2 != null) {
                    fr2.close();
                }
                br = br2;
                fr = fr2;
            } catch (FileNotFoundException e7) {
                br = br2;
                fr = fr2;
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (IOException e8) {
                br = br2;
                fr = fr2;
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (array != null) {
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                br = br2;
                fr = fr2;
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e9) {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (array != null) {
            }
            return null;
        } catch (IOException e10) {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (array != null) {
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            throw th;
        }
        if (array != null || array.length < 2) {
            return null;
        }
        return array[1];
    }

    public static String getDisplay(Context context) {
        return null;
    }

    public static String getTelephonyModel() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static int getSystemVersionCode() {
        return VERSION.SDK_INT;
    }

    public static String getSystemVersionName() {
        return VERSION.RELEASE;
    }

    public static String getMac(Context context) {
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
            WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getImsi(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getImei(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAndroidId(Context c) {
        return System.getString(c.getContentResolver(), "android_id");
    }

    public static int getSimOperatorType(Context context) {
        try {
            if (((TelephonyManager) context.getSystemService("phone")).getSimState() != 5) {
                return 4;
            }
            String IMSI = getImsi(context);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                return 1;
            }
            if (IMSI.startsWith("46001")) {
                return 2;
            }
            if (IMSI.startsWith("46003")) {
                return 3;
            }
            return 4;
        } catch (Exception e) {
            return 4;
        }
    }

    public static String getIp(Context paramContext) {
        String str = "";
        if (((ConnectivityManager) paramContext.getSystemService("connectivity")).getActiveNetworkInfo() == null) {
            return "";
        }
        try {
            str = Formatter.formatIpAddress(((WifiManager) paramContext.getSystemService("wifi")).getConnectionInfo().getIpAddress());
            if (!TextUtils.isEmpty(str)) {
                return "0.0.0.0".equals(str) ? "" : str;
            } else {
                Enumeration localEnumeration1 = NetworkInterface.getNetworkInterfaces();
                while (localEnumeration1.hasMoreElements()) {
                    Enumeration localEnumeration2 = ((NetworkInterface) localEnumeration1.nextElement()).getInetAddresses();
                    while (localEnumeration2.hasMoreElements()) {
                        InetAddress localInetAddress = (InetAddress) localEnumeration2.nextElement();
                        if (!localInetAddress.isLoopbackAddress() && !localInetAddress.isLinkLocalAddress() && localInetAddress.isSiteLocalAddress() && (localInetAddress instanceof Inet4Address)) {
                            return localInetAddress.getHostAddress();
                        }
                    }
                }
                return str;
            }
        } catch (SocketException e) {
        } catch (Exception e2) {
        }
    }

    public static int getNetWorkType(Context context) {
        int mNetWorkType = 0;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int nType = networkInfo.getType();
            if (nType != 1) {
                if (nType == 0) {
                    switch (getNetworkClass(context)) {
                        case 2:
                            mNetWorkType = 2;
                            break;
                        case 3:
                            mNetWorkType = 3;
                            break;
                        case 4:
                            mNetWorkType = 4;
                            break;
                        default:
                            break;
                    }
                }
            }
            mNetWorkType = 1;
        } else {
            mNetWorkType = 0;
        }
        if (mNetWorkType == 0) {
            return 4;
        }
        return mNetWorkType;
    }

    private static int getNetworkClass(Context context) {
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 3;
            case 13:
                return 4;
            default:
                return 0;
        }
    }
}
