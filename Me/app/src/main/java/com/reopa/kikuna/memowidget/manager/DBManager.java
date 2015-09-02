package com.reopa.kikuna.memowidget.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arthurvc on 2015/07/12.
 */
public class DBManager extends SQLiteOpenHelper {

    private static DBManager sDbManager = null;

    private static final String DB_NAME = "db_memo";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE_SQL = "create table memos (id integer primary key autoincrement, memo_text text not null )";
    private static final String DROP_TABLE_SQL = "drop table if exists memos";


    private DBManager (Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static DBManager getDBManager(Context context) {
        if (sDbManager == null) {
            sDbManager = new DBManager(context);
        }
        return sDbManager;
    }

    public static SQLiteDatabase getReadableDb() {
        return sDbManager.getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDb() {
        return sDbManager.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SQL);
        db.execSQL(CREATE_TABLE_SQL);
    }
}
