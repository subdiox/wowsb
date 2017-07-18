package com.yaya.sdk.utils;

import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;

public class AssetsReader {
    public static String getcode(Context c, String fileName) {
        try {
            InputStream is = c.getAssets().open(fileName);
            byte[] data = new byte[is.available()];
            is.read(data);
            is.close();
            return new String(data);
        } catch (IOException e) {
            Toast.makeText(c, "读取出错", 0).show();
            return null;
        }
    }
}
