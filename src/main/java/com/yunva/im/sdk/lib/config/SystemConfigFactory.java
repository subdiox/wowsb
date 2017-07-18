package com.yunva.im.sdk.lib.config;

public class SystemConfigFactory {
    private static SystemConfig systemConfig;
    private static SystemConfigFactory systemConfigFactory;

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    private SystemConfigFactory() {
    }

    public static synchronized SystemConfigFactory getInstance() {
        SystemConfigFactory systemConfigFactory;
        synchronized (SystemConfigFactory.class) {
            if (systemConfigFactory == null) {
                systemConfigFactory = new SystemConfigFactory();
            }
            systemConfigFactory = systemConfigFactory;
        }
        return systemConfigFactory;
    }

    public void buildConfig(boolean isTest) {
        if (isTest) {
            getInstance();
            systemConfig = new TestSystemConfig();
            return;
        }
        getInstance();
        systemConfig = new ProductSystemConfig();
    }

    public String getUpdateUrl() {
        return systemConfig.getUpdateUrl();
    }

    public String getJarPath() {
        return systemConfig.getJarPath();
    }

    public String getDefaultJarFileName() {
        return systemConfig.getJarFileName();
    }

    public int getDefaultYunvaLiveSdkVersion() {
        return systemConfig.getYunvaLiveSdkVersion();
    }

    public String getApkOutputPath() {
        return systemConfig.getApkOutputPath();
    }

    public String getUpdateInfoStrorePath() {
        return systemConfig.getUpdateInfoStrorePath();
    }

    public String getUpdateInfoStroreFileName() {
        return systemConfig.getUpdateInfoStroreFileName();
    }

    public String getDownloadServer() {
        return systemConfig.getDownloadServer();
    }

    public String getPath() {
        return systemConfig.getPath();
    }

    public String getPicSavePath() {
        return systemConfig.getPicSavePath();
    }

    public String getVoiceMessagePath() {
        return systemConfig.getVoiceMessagePath();
    }

    public String getBdViocePath() {
        return systemConfig.getBdViocePath();
    }
}
