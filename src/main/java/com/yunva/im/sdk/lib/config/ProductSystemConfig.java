package com.yunva.im.sdk.lib.config;

import android.os.Environment;
import com.yunva.im.sdk.lib.YvLoginInit;
import com.yunva.im.sdk.lib.utils.VersionUtil;

public class ProductSystemConfig extends SystemConfig {
    public String getUpdateUrl() {
        return "http://aya.yunva.com/yunva?m=UPDATE";
    }

    public String getPath() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString() + "/im_sdk";
        }
        try {
            return YvLoginInit.context.getFilesDir().getAbsolutePath() + "/im_sdk";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getJarPath() {
        return getPath() + "/jar";
    }

    public String getJarFileName() {
        return VersionUtil.DefaultVersionName;
    }

    public int getYunvaLiveSdkVersion() {
        return VersionUtil.DefaultVersion;
    }

    public String getApkOutputPath() {
        return getPath() + "/sig";
    }

    public String getUpdateInfoStrorePath() {
        return getPath() + "/updateinfo";
    }

    public String getUpdateInfoStroreFileName() {
        return "yunva_dynamic_live_sdk_version_info.txt";
    }

    public String getDownloadServer() {
        return "http://f.aiwaya.cn/";
    }

    public String getPicSavePath() {
        return getPath() + "/pic";
    }

    public String getVoiceMessagePath() {
        return getPath() + "/voice";
    }

    public String getBdViocePath() {
        return getPath() + "/bdvoice";
    }
}
