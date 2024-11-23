package net.syllabus.whatsoup

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.widget.RemoteViews
import java.util.Calendar


/**
 * Implementation of App Widget functionality.
 */
class TodayWidget2 : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val appWidgetManager = AppWidgetManager.getInstance(context.applicationContext)
        val thisWidget: ComponentName = ComponentName(
            context.applicationContext,
            TodayWidget2::class.java
        )
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        if (appWidgetIds != null && appWidgetIds.size > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    var widgetText = ""
    context?.openFileInput("plats.txt").use { stream ->
        stream?.bufferedReader().use { widgetText = it?.readText().toString() }
    }

    val cal = Calendar.getInstance()
    val day = cal.get(Calendar.DAY_OF_WEEK)
    val index = (day % 7) * 2

    val midi = widgetText.split(MainActivity.Constants.SEP)[index]
    val soir = widgetText.split(MainActivity.Constants.SEP)[index+1]

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.today_widget2)
    views.setTextViewText(R.id.appwidget_text, "Aujourd'hui "+DateFormat.format("EEEE", cal)+"\nMidi : "+midi+"\nSoir : "+soir)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}