package com.reopa.kikuna.memowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.reopa.kikuna.memowidget.dao.MemosDao;
import com.reopa.kikuna.memowidget.entity.MemosEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atsuko on 15/07/05.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    private static RemoteViews remoteViews = null;
    private static Context widgetContext = null;

    // Widget表示アップデート
    public static void updateWidget(Context context) {
        if (widgetContext != null) {
            changeWidgetText(context);
            ComponentName thisWidget = new ComponentName(widgetContext, MyWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(widgetContext);
            manager.updateAppWidget(thisWidget, remoteViews);
        }
    }

    /**
     * Registration click event.
     * When users click widget text, view MainActvity.
     */
    private static void registClickEvent(Context context) {
        class ClsActList {
            int layoutView;
            Const.ActTapWidget kind;

            ClsActList(int v, Const.ActTapWidget k) {
                layoutView = v;
                kind = k;
            }
        }
        List<ClsActList> actList = new ArrayList<ClsActList>();
        // Memo (Open Activity only)
        actList.add(new ClsActList(R.id.widget_text, Const.ActTapWidget.KIND_NORMAL));
        // Pen (Open Activity and view add dialog)
        actList.add(new ClsActList(R.id.widget_addButton, Const.ActTapWidget.KIND_ADD));

        for (ClsActList act : actList) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Const.ACT_TAP_WIDGET, act.kind);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, act.layoutView, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(act.layoutView, pendingIntent);
        }
    }

    private static void changeWidgetText(Context context) {
        MemosDao dao = new MemosDao(context);
        List<MemosEntity> entityList = dao.findAll();
        String viewMemoText = "";
        if (entityList.size() > 0) {
            viewMemoText = entityList.get(entityList.size() - 1).getMemoText();
        }
        remoteViews.setTextViewText(R.id.widget_text, viewMemoText);
    }

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
        widgetContext = context;

        // Ragistration click event.
        registClickEvent(context);

        // アップデートメソッド呼び出し
        updateWidget(context);

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
}