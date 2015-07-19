package com.reopa.kikuna.memowidget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.reopa.kikuna.memowidget.dao.MemosDao;
import com.reopa.kikuna.memowidget.entity.MemosEntity;
import com.reopa.kikuna.memowidget.manager.DBManager;

import java.util.List;

/**
 * Created by atsuko on 15/07/19.
 */
public class MemosUtils {

  public static SQLiteDatabase getReadableDb(Context context) {
    DBManager dbManager = DBManager.getDBManager(context);
    return dbManager.getReadableDatabase();
  }

  public static SQLiteDatabase getWritableDb(Context context) {
    DBManager dbManager = DBManager.getDBManager(context);
    return dbManager.getWritableDatabase();
  }

  public static List<MemosEntity> getLatestMemosFromDb(Context context) {
    SQLiteDatabase db = MemosUtils.getReadableDb(context);
    return new MemosDao(db).findAll();
  }

  public static void deleteMemoById(int id, Context context) {
    SQLiteDatabase db = MemosUtils.getWritableDb(context);
    new MemosDao(db).delete(id);
  }

  public static void insertMemo(String memo, Context context) {
    SQLiteDatabase db = MemosUtils.getWritableDb(context);
    new MemosDao(db).insert(memo);
  }
}
