package com.yunva.sdk;

import android.content.Context;

public class JNI {
    public static void LoadJni(Context context) {
        String path = new StringBuilder(String.valueOf(context.getApplicationInfo().dataDir)).append("/lib").toString();
        try {
            System.loadLibrary("libYvImSdk");
        } catch (UnsatisfiedLinkError e) {
            try {
                System.load(new StringBuilder(String.valueOf(path)).append("/libYvImSdk.so").toString());
            } catch (Exception e2) {
                try {
                    e.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
}
