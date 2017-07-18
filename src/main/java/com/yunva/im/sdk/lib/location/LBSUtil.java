package com.yunva.im.sdk.lib.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import com.yunva.im.sdk.lib.utils.MyLog;
import java.util.List;

public class LBSUtil {
    public static LBSUtil lbsUtil;
    private double lat = 0.0d;
    private double latitude = 0.0d;
    private LocationManager locationManager;
    private double lon = 0.0d;
    private double longitude = 0.0d;
    private Context mContext;
    private LbsInfoReturnListener mLbsInfoReturnListener;

    public static LBSUtil getInstance(Context context, LbsInfoReturnListener listener) {
        if (lbsUtil != null) {
            return lbsUtil;
        }
        lbsUtil = new LBSUtil(context, listener);
        return lbsUtil;
    }

    public LBSUtil(Context context, LbsInfoReturnListener listener) {
        this.mContext = context;
        this.locationManager = (LocationManager) this.mContext.getSystemService("location");
        this.mLbsInfoReturnListener = listener;
    }

    public boolean getLocationByGPS() {
        this.lat = 0.0d;
        this.lon = 0.0d;
        if (this.locationManager.isProviderEnabled("gps")) {
            Location location = this.locationManager.getLastKnownLocation("gps");
            if (location != null) {
                this.lat = location.getLatitude();
                this.lon = location.getLongitude();
            }
            LocationListener gpsListener = requestLocationByGPS();
            Location location2 = this.locationManager.getLastKnownLocation("gps");
            if (location2 != null) {
                this.lat = location2.getLatitude();
                this.lon = location2.getLongitude();
                this.mLbsInfoReturnListener.getLbsInfo(1, this.lon + "_" + this.lat);
                this.locationManager.removeUpdates(gpsListener);
                return true;
            } else if (this.lat == 0.0d || this.lon == 0.0d) {
                this.mLbsInfoReturnListener.returnError(1949, 1);
                return false;
            } else {
                this.mLbsInfoReturnListener.getLbsInfo(1, this.lon + "_" + this.lat);
                return true;
            }
        }
        this.mLbsInfoReturnListener.returnError(1943, 1);
        return false;
    }

    public void getLocationByIP() {
        this.mLbsInfoReturnListener.returnError(1946, 8);
    }

    public void getLocationByWifiCell() {
        if (this.locationManager.isProviderEnabled("network")) {
            LocationListener netListener = requestLocationByNET();
            Location location3 = this.locationManager.getLastKnownLocation("network");
            if (location3 != null) {
                this.latitude = location3.getLatitude();
                this.longitude = location3.getLongitude();
                this.mLbsInfoReturnListener.getLbsInfo(32, this.longitude + "_" + this.latitude);
                this.locationManager.removeUpdates(netListener);
                return;
            }
            return;
        }
        this.mLbsInfoReturnListener.returnError(1946, 32);
    }

    public void getLocationByAGPS() {
        this.latitude = 0.0d;
        this.longitude = 0.0d;
        if (this.locationManager.isProviderEnabled("gps")) {
            Location location = this.locationManager.getLastKnownLocation("gps");
            if (location != null) {
                this.latitude = location.getLatitude();
                this.longitude = location.getLongitude();
            }
            LocationListener gpsListener = requestLocationByGPS();
            Location location2 = this.locationManager.getLastKnownLocation("gps");
            if (location2 != null) {
                this.latitude = location2.getLatitude();
                this.longitude = location2.getLongitude();
                this.mLbsInfoReturnListener.getLbsInfo(64, this.longitude + "_" + this.latitude);
                this.locationManager.removeUpdates(gpsListener);
                return;
            }
        }
        if (this.locationManager.isProviderEnabled("network")) {
            LocationListener netListener = requestLocationByNET();
            Location location3 = this.locationManager.getLastKnownLocation("network");
            if (location3 != null) {
                this.latitude = location3.getLatitude();
                this.longitude = location3.getLongitude();
                this.mLbsInfoReturnListener.getLbsInfo(64, this.longitude + "_" + this.latitude);
                this.locationManager.removeUpdates(netListener);
                return;
            }
        }
        if (this.latitude == 0.0d || this.longitude == 0.0d) {
            this.mLbsInfoReturnListener.returnError(1946, 64);
        } else {
            this.mLbsInfoReturnListener.getLbsInfo(64, this.longitude + "_" + this.latitude);
        }
    }

    private LocationListener requestLocationByGPS() {
        LocationListener locationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                MyLog.d("LBS", "onProviderEnabled : provider=" + provider);
            }

            public void onProviderDisabled(String provider) {
                MyLog.d("LBS", "onProviderDisabled : provider=" + provider);
            }

            public void onLocationChanged(Location location) {
                if (location == null) {
                }
            }
        };
        this.locationManager.requestLocationUpdates("gps", 1000, 10.0f, locationListener);
        return locationListener;
    }

    private LocationListener requestLocationByNET() {
        LocationListener locationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                MyLog.d("LBS", "onProviderDisabled : provider=" + provider);
            }

            public void onLocationChanged(Location location) {
                if (location == null) {
                }
            }
        };
        this.locationManager.requestLocationUpdates("network", 1000, 10.0f, locationListener);
        return locationListener;
    }

    public void getLocationByWiFi() {
        int mncType = ((TelephonyManager) this.mContext.getSystemService("phone")).getPhoneType();
        List<ScanResult> wifiList = ((WifiManager) this.mContext.getSystemService("wifi")).getScanResults();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult result = (ScanResult) wifiList.get(i);
            if (!"".equals(result.BSSID)) {
                sb.append("{");
                sb.append(result.BSSID);
                sb.append("|");
                sb.append(WifiManager.calculateSignalLevel(result.level, 5));
                sb.append("|");
                sb.append("}");
            }
            if (i >= 30) {
                break;
            }
        }
        sb.append("|").append(mncType);
        if (sb.toString().length() == 1) {
            this.mLbsInfoReturnListener.returnError(1940, 2);
        } else {
            this.mLbsInfoReturnListener.getLbsInfo(2, sb.toString());
        }
    }

    public void getLocationByCell() {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
            String operator = mTelephonyManager.getNetworkOperator();
            int mncType = mTelephonyManager.getPhoneType();
            if ("".equals(operator) || operator.length() <= 3) {
                this.mLbsInfoReturnListener.returnError(1946, 4);
                return;
            }
            int mcc = Integer.parseInt(operator.substring(0, 3));
            int mnc = Integer.parseInt(operator.substring(3));
            List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
            StringBuffer sb = new StringBuffer();
            for (NeighboringCellInfo info1 : infos) {
                sb.append("{");
                sb.append(mcc);
                sb.append("|");
                sb.append(mnc);
                sb.append("|");
                sb.append(info1.getCid());
                sb.append("|");
                sb.append(info1.getLac());
                sb.append("|");
                sb.append((info1.getRssi() * 2) - 113);
                sb.append("|");
                sb.append("|");
                sb.append("}");
            }
            sb.append("|").append(mncType);
            this.mLbsInfoReturnListener.getLbsInfo(4, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            this.mLbsInfoReturnListener.returnError(1946, 4);
        }
    }

    public void getLocationByBlueTooth() {
        this.mLbsInfoReturnListener.returnError(1946, 16);
    }
}
