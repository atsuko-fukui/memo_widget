package com.reopa.kikuna.memowidget.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reopa.kikuna.memowidget.entity.MemosEntity;

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

    private SQLiteDatabase db;

    public MemosDao(SQLiteDatabase db) {
        this.db = db;
    }

    public List<MemosEntity> findAll() {
        List<MemosEntity> entityList = new ArrayList<MemosEntity>();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_ID);

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
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, select, null, null, null, null);

        cursor.moveToNext();

        MemosEntity entity = new MemosEntity();
        entity.setId(cursor.getInt(0));
        entity.setMemoText(cursor.getString(1));

        return entity;
    }

    public long insert(String memo) {
        ContentValues memoText = new ContentValues();
        memoText.put(COLUMN_MEMO_TEXT, memo);
        return db.insert(TABLE_NAME, null, memoText);
    }

    public int update(MemosEntity entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMO_TEXT, entity.getMemoText());
        String whereClause = COLUMN_ID + "=" + entity.getId();
        return db.update(TABLE_NAME, values, whereClause, null);
    }

    public int delete(int id) {
        String whereClause = COLUMN_ID + "=" + id;
        return db.delete(TABLE_NAME, whereClause, null);
    }
}
