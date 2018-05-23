package com.example.ndh.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm {
    private AlarmManager am;
    private PendingIntent pendingIntent;

    public void create(Context context, Date deadline, String title) {
        Date current = new Date();
        long diff = deadline.getTime() - current.getTime();

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("Alarm", title);

        pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + diff), pendingIntent);
        Log.d("mytestonly", "here in alram");

        Toast.makeText(context, "Remind in " + diff + " miliseconds", Toast.LENGTH_LONG).show();
    }

    public void cancel() {
        am.cancel(pendingIntent);
        Log.d("alarm", "cancel");
    }
}
