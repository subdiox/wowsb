package com.appsflyer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.support.annotation.RequiresApi;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;

@TargetApi(23)
@RequiresApi(api = 23)
class AFKeystoreWrapper {
    private Context context;
    private KeyStore keystore;
    private final Object lock = new Object();
    private int reInstallCounter;
    private String uid;

    public AFKeystoreWrapper(Context context) {
        this.context = context;
        this.uid = "";
        this.reInstallCounter = 0;
        initKeyStore();
    }

    private void initKeyStore() {
        a.afLog("Initialising KeyStore..");
        try {
            this.keystore = KeyStore.getInstance("AndroidKeyStore");
            this.keystore.load(null);
            return;
        } catch (IOException e) {
        } catch (NoSuchAlgorithmException e2) {
        } catch (CertificateException e3) {
        } catch (KeyStoreException e4) {
        }
        a.afLog("Couldn't load keystore instance of type: AndroidKeyStore");
    }

    public void createFirstInstallData(String appsFlyerUID) {
        this.uid = appsFlyerUID;
        this.reInstallCounter = 0;
        createKey(generateAliasString());
    }

    public void incrementReInstallCounter() {
        String generateAliasString = generateAliasString();
        synchronized (this.lock) {
            this.reInstallCounter++;
            deleteKey(generateAliasString);
        }
        createKey(generateAliasString());
    }

    public boolean loadData() {
        boolean z;
        boolean z2 = true;
        synchronized (this.lock) {
            if (this.keystore != null) {
                try {
                    Enumeration aliases = this.keystore.aliases();
                    while (aliases.hasMoreElements()) {
                        String str = (String) aliases.nextElement();
                        if (str != null && isAppsFlyerPrefix(str)) {
                            String[] split = str.split(",");
                            if (split.length == 3) {
                                a.afLog("Found a matching AF key with alias:\n" + str);
                                try {
                                    String[] split2 = split[1].trim().split("=");
                                    String[] split3 = split[2].trim().split("=");
                                    if (split2.length == 2 && split3.length == 2) {
                                        this.uid = split2[1].trim();
                                        this.reInstallCounter = Integer.parseInt(split3[1].trim());
                                    }
                                    z = true;
                                } catch (Throwable th) {
                                    Object th2 = th;
                                    a.afLog("Couldn't list KeyStore Aliases: " + th2.getClass().getName());
                                    z = z2;
                                    return z;
                                }
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    z2 = false;
                    a.afLog("Couldn't list KeyStore Aliases: " + th2.getClass().getName());
                    z = z2;
                    return z;
                }
            }
            z = false;
        }
        return z;
    }

    private void createKey(String alias) {
        a.afLog("Creating a new key with alias: " + alias);
        try {
            synchronized (this.lock) {
                if (this.keystore.containsAlias(alias)) {
                    a.afLog("Alias already exists: " + alias);
                } else {
                    Calendar instance = Calendar.getInstance();
                    Calendar instance2 = Calendar.getInstance();
                    instance2.add(1, 5);
                    AlgorithmParameterSpec algorithmParameterSpec = null;
                    if (VERSION.SDK_INT >= 23) {
                        algorithmParameterSpec = new Builder(alias, 3).setCertificateSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setCertificateSerialNumber(BigInteger.ONE).setCertificateNotBefore(instance.getTime()).setCertificateNotAfter(instance2.getTime()).build();
                    } else if (VERSION.SDK_INT >= 18) {
                        algorithmParameterSpec = new KeyPairGeneratorSpec.Builder(this.context).setAlias(alias).setSubject(new X500Principal("CN=AndroidSDK, O=AppsFlyer")).setSerialNumber(BigInteger.ONE).setStartDate(instance.getTime()).setEndDate(instance2.getTime()).build();
                    }
                    KeyPairGenerator instance3 = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    instance3.initialize(algorithmParameterSpec);
                    instance3.generateKeyPair();
                }
            }
        } catch (Exception e) {
            a.afLog("Exception " + e.getMessage() + " occurred");
        }
    }

    private void deleteKey(String alias) {
        a.afLog("Deleting key with alias: " + alias);
        try {
            synchronized (this.lock) {
                this.keystore.deleteEntry(alias);
            }
        } catch (KeyStoreException e) {
            a.afLog("Exception " + e.getMessage() + " occurred");
        }
    }

    private boolean isAppsFlyerPrefix(String alias) {
        return alias.startsWith(BuildConfig.APPLICATION_ID);
    }

    private String generateAliasString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BuildConfig.APPLICATION_ID).append(",");
        synchronized (this.lock) {
            stringBuilder.append("KSAppsFlyerId").append("=").append(this.uid).append(",");
            stringBuilder.append("KSAppsFlyerRICounter").append("=").append(this.reInstallCounter);
        }
        return stringBuilder.toString();
    }

    public String getUid() {
        String str;
        synchronized (this.lock) {
            str = this.uid;
        }
        return str;
    }

    public int getReInstallCounter() {
        int i;
        synchronized (this.lock) {
            i = this.reInstallCounter;
        }
        return i;
    }
}
