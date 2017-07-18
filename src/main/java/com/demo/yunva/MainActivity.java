package com.demo.yunva;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.yunva.im.sdk.lib.YvLoginInit;
import com.yunva.sdk.JNI;
import com.yunva.sdk.YvImSdk;
import com.yunva.sdk.YvPacketSdk;

public class MainActivity extends UnityPlayerActivity {
    private static YvImSdk sYvImSdk = null;
    private static YvPacketSdk sYvPacketSdk = null;
    private String YvAppId = "600027";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Object appidObj = getPackageManager().getApplicationInfo(getPackageName(), 128).metaData.get("yv_appid");
            if (appidObj != null) {
                this.YvAppId = String.valueOf(appidObj);
            }
            Log.w("YV", "yv appid = " + this.YvAppId);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        YvLoginInit.initApplicationOnCreate(getApplication(), this.YvAppId);
    }

    public static YvImSdk getYvImSdk() {
        if (sYvImSdk == null) {
            sYvImSdk = new YvImSdk();
        }
        return sYvImSdk;
    }

    public static YvPacketSdk getYvPacketSdk() {
        if (sYvPacketSdk == null) {
            sYvPacketSdk = new YvPacketSdk();
        }
        return sYvPacketSdk;
    }

    public int YvInitSdk(String appid, String sqlPath, boolean isTest) {
        JNI.LoadJni(this);
        getYvImSdk();
        getYvPacketSdk();
        return sYvImSdk.YvInitSdk(Long.parseLong(this.YvAppId), sqlPath, isTest);
    }

    public void YvRelease() {
        sYvImSdk.YvRelease();
        sYvImSdk = null;
        sYvPacketSdk = null;
    }

    public int yvpacket_get_parser() {
        return sYvPacketSdk.yvpacket_get_parser();
    }

    public void parser_set_string(int parser, byte cmdId, String value) {
        sYvPacketSdk.parser_set_string(parser, cmdId, value);
    }

    public void parser_set_integer(int parser, byte cmdId, int value) {
        sYvPacketSdk.parser_set_integer(parser, cmdId, value);
    }

    public void parser_set_btye(int parser, byte cmdId, byte value) {
        sYvPacketSdk.parser_get_btye(parser, cmdId, value);
    }

    public int YvSendCmd(int type, int cmdid, int parser) {
        return sYvImSdk.YvSendCmd(type, cmdid, parser);
    }

    public static boolean CheckRecordPermission() {
        if (ContextCompat.checkSelfPermission(UnityPlayer.currentActivity, "android.permission.RECORD_AUDIO") == 0) {
            return false;
        }
        ActivityCompat.requestPermissions(UnityPlayer.currentActivity, new String[]{"android.permission.RECORD_AUDIO"}, 1);
        return true;
    }
}
