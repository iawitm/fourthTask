package com.user.fourthtask.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.user.fourthtask.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SharedPreferences preferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.date_widget);
        String content = preferences.getString("date", context.getString(R.string.appwidget_text));
        if (!content.equals(context.getString(R.string.appwidget_text))){
            Date today = new Date();
            Date targetDay;
            try {
                targetDay = simpleDateFormat.parse(content);
            } catch (ParseException e) {
                targetDay = new Date();
            }
            int days = (int) TimeUnit.DAYS.convert(targetDay.getTime() - today.getTime(), TimeUnit.MILLISECONDS);
            views.setTextViewText(R.id.appwidget_text, "Осталось " + days + " дн.");
        } else {
            views.setTextViewText(R.id.appwidget_text, content);
        }

        Intent openMain = new Intent(context, MainActivity.class);
        openMain.putExtra("type", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 55, openMain, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

