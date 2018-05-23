package com.example.ndh.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentNotify = new Intent(context, MyIntent.class);

        String sContent = intent.getStringExtra("Alarm");
        intentNotify.putExtra("Alarm", sContent);

        context.startService(intentNotify);
    }
}