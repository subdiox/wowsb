package com.yaya.sdk.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.yaya.sdk.constants.Constants;

public class DaoUtils {
    private static String TAG = DaoUtils.class.getSimpleName();
    private static DaoUtils daoUtils;
    private DbHelper dbHelper;

    public DaoUtils(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public static synchronized DaoUtils getInstance(Context context) {
        DaoUtils daoUtils;
        synchronized (DaoUtils.class) {
            if (daoUtils == null) {
                daoUtils = new DaoUtils(context);
            }
            daoUtils = daoUtils;
        }
        return daoUtils;
    }

    public void insertId(int requestId) {
        String id = String.valueOf(requestId);
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_HELLO_ID, Integer.valueOf(requestId));
        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public boolean isPulled(int helloId) {
        String id = String.valueOf(helloId);
        boolean ispulled = false;
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select helloid from table_name where helloid =?", new String[]{id});
        if (cursor.moveToNext()) {
            ispulled = true;
        }
        cursor.close();
        db.close();
        return ispulled;
    }

    public boolean deleteByhelloId(String helloId) {
        boolean flag = false;
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        if (db.delete(Constants.TABLE_NAME, "helloid=?", new String[]{helloId}) != 0) {
            flag = true;
        }
        db.close();
        return flag;
    }
}
