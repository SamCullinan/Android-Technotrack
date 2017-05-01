package edu.phystech.samir.timer;

/**
 * Created by Samir on 30.04.2017.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.content.Intent;
import android.content.Context;



public class ScheduledService extends IntentService {

    private static final String ACTION = "action";
    private static final int NOTIFY_ID = 222;

    public ScheduledService() {
        super("ScheduledService");
    }

    public static void startScheduledJob(Context context) {
        Intent intent = new Intent(context, ScheduledService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    builder.setContentText("Таймер сработал")
                            .setTicker("Последнее китайское предупреждение")
                            .setContentTitle("Напоминание")
                            .setSmallIcon(R.drawable.selftimer)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.selftimer))
                            .setWhen(System.currentTimeMillis())
                            .setShowWhen(true)
                            .setAutoCancel(true);

                    Notification nc = builder.build();
                    NotificationManager nm = (NotificationManager)getApplicationContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(NOTIFY_ID, nc);
                }
            });
        }
    }
}