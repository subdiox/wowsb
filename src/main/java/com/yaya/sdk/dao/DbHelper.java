package com.yaya.sdk.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.yaya.sdk.constants.Constants;

public class DbHelper extends SQLiteOpenHelper {
    private static final int version = 1;

    public DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  table_name(id integer primary key autoincrement, helloid varchar(20))");
    }

    private void addInitValues(SQLiteDatabase db) {
        ContentValues cv0 = new ContentValues();
        ContentValues cv1 = new ContentValues();
        cv0.put("name", "ober");
        cv1.put("name", "ober2016");
        db.insert("test_user", null, cv0);
        db.insert("test_user", null, cv1);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS table_name");
        onCreate(db);
    }
}
