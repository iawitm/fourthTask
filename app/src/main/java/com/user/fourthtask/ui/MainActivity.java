package com.user.fourthtask.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.user.fourthtask.R;
import com.user.fourthtask.utils.AlertReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    TextView date;
    SharedPreferences preferences;
    boolean openIntentDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        date = findViewById(R.id.main_date);
        date.setOnClickListener(view -> {
            openDateDialog();
        });
    }

    public void setDate(String date){
        this.date.setText("Выбранная дата:\n" + date);
        preferences.edit().putString("date", date).apply();
        updateWidget();
        cancelAlarm();
        startAlarm(date);
    }

    public void updateWidget(){
        Intent intent = new Intent(this, DateWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), DateWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    public void openDateDialog(){
        if (getSupportFragmentManager().findFragmentByTag("datePicker") == null){
            DateDialog dialog = new DateDialog();
            dialog.show(getSupportFragmentManager(), "datePicker");
        }
    }

    private void startAlarm(String day) {
        Date date;
        try {
            date = simpleDateFormat.parse(day);
        } catch (ParseException e) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (openIntentDialog) {
            openDateDialog();
            openIntentDialog = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        if (intent.getIntExtra("type", 0) == 1) openIntentDialog = true;
    }

}
