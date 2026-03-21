package com.suica.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SuicaWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_suica)

            // 保存済み残高を表示
            val balance = BalanceStore.getBalance(context)
            if (balance != null) {
                views.setTextViewText(R.id.text_balance, BalanceStore.formatBalance(balance))
            } else {
                views.setTextViewText(
                    R.id.text_balance,
                    context.getString(R.string.widget_no_data)
                )
            }

            // ウィジェットタップで設定画面を起動
            val intent = Intent(context, SetupActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, SuicaWidgetProvider::class.java)
            )
            for (id in widgetIds) {
                updateWidget(context, appWidgetManager, id)
            }
        }
    }
}
