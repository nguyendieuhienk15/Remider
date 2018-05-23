package com.example.ndh.reminder;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class MyIntent extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntent(String name) {
        super(name);
    }

    public MyIntent() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String sTitle = this.getString(R.string.app_name);
        String sContent = intent.getStringExtra("Alarm");
        Log.d("mytestonly", "here in intent " + sContent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(sTitle)
                .setContentText(sContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

        Intent intentToFire = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat.from(this).notify((int) System.currentTimeMillis(), builder.build());
    }
}
