package com.reopa.kikuna.memowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by atsuko on 15/07/05.
 */
public class MyWidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // ウィジェットレイアウトの初期化
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

    // テキストフィールドに"初期画面"と表示
    remoteViews.setTextViewText(R.id.title, "initial screen");

    // アップデートメソッド呼び出し
    pushWidgetUpdate(context, remoteViews);
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

}
