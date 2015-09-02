package com.reopa.kikuna.memowidget.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reopa.kikuna.memowidget.entity.MemosEntity;
import com.reopa.kikuna.memowidget.manager.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arthurvc on 2015/07/12.
 */
public class MemosDao {

    public static final String TABLE_NAME = "memos";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEMO_TEXT = "memo_text";
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_MEMO_TEXT};

    private DBManager dbManager;
    private SQLiteDatabase readDb;
    private SQLiteDatabase writeDb;

    public MemosDao(Context context) {
        this.dbManager = DBManager.getDBManager(context);
        this.readDb = dbManager.getReadableDb();
        this.writeDb = dbManager.getWritableDb();
    }

    public List<MemosEntity> findAll() {
        List<MemosEntity> entityList = new ArrayList<MemosEntity>();
        Cursor cursor = readDb.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_ID);

        while(cursor.moveToNext()) {
            MemosEntity entity = new MemosEntity();
            entity.setId(cursor.getInt(0));
            entity.setMemoText(cursor.getString(1));
            entityList.add(entity);
        }
        return entityList;
    }

    public MemosEntity findById(int id) {
        String select = COLUMN_ID + "=" + id;
        Cursor cursor = readDb.query(TABLE_NAME, COLUMNS, select, null, null, null, null);

        cursor.moveToNext();

        MemosEntity entity = new MemosEntity();
        entity.setId(cursor.getInt(0));
        entity.setMemoText(cursor.getString(1));

        return entity;
    }

    public long insert(String memo) {
        ContentValues memoText = new ContentValues();
        memoText.put(COLUMN_MEMO_TEXT, memo);
        return writeDb.insert(TABLE_NAME, null, memoText);
    }

    public int update(MemosEntity entity) {
        String id = COLUMN_ID + "=" + entity.getId();
        ContentValues memoText = new ContentValues();
        memoText.put(COLUMN_MEMO_TEXT, entity.getMemoText());
        return writeDb.update(TABLE_NAME, memoText, id, null);
    }

    public int delete(int id) {
        String whereClause = COLUMN_ID + "=" + id;
        return writeDb.delete(TABLE_NAME, whereClause, null);
    }
}
