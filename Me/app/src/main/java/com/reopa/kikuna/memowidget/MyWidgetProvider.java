package com.reopa.kikuna.memowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.reopa.kikuna.memowidget.dao.MemosDao;
import com.reopa.kikuna.memowidget.entity.MemosEntity;
import com.reopa.kikuna.memowidget.manager.DBManager;

import java.util.List;

/**
 * Created by atsuko on 15/07/05.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static RemoteViews remoteViews = null;

    /**
     * Called when first widget is added to screen.
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * Called when widgets are added.<br>
     * notes:If a first widget added, onUpdate() is called after onEnable()..
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // ウィジェットレイアウトの初期化
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // テキストフィールドに"初期画面"と表示
        remoteViews.setTextViewText(R.id.title, "initial screen");

        // Ragistration click event.
        registClickEvent(context);

        // Change widget text.
        changeWidgetText(context);

        // アップデートメソッド呼び出し
        pushWidgetUpdate(context, remoteViews);

    }

    /**
     * Called when widgets are deleted.
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * Called when LAST widget is deleted after onDeleted().
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }


    public static PendingIntent clickButton(Context context) {
        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction("UPDATE_WIDGET");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // アップデート
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    /**
     * Registration click event.
     * When users click widget text, view MainActvity.
     */
    private static void registClickEvent(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget_text, pendingIntent);
    }

    private static void changeWidgetText(Context context) {
        // Get DB data..
        DBManager dbManager = DBManager.getDBManager(context);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        MemosDao dao = new MemosDao(db);
        dao.insert("test"); // TODO とりあえず。メモ保存できるようになったら消す。
        List<MemosEntity> entityList = dao.findAll();

        if (entityList.size() > 0) {
            String viewMemoText = entityList.get(0).getMemoText();
            remoteViews.setTextViewText(R.id.widget_text, viewMemoText);
        }
    }

}