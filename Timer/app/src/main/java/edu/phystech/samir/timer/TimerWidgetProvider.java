package edu.phystech.samir.timer;

/**
 * Created by Samir on 30.04.2017.
 */

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.widget.RemoteViews;
import android.content.ComponentName;
import android.content.Context;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;


public class TimerWidgetProvider extends AppWidgetProvider {

    private final String SET = "set";
    private final String INC = "inc";
    private final String DEC = "dec";
    private final int MintoMs = 60000;

    private static int count = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.timer__widget);
        remoteViews.setTextViewText(R.id.TextView, String.valueOf(count));

        Intent intent = new Intent(context, TimerWidgetProvider.class);

        intent.setAction(SET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonSet, pendingIntent);

        intent.setAction(INC);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonInc, pendingIntent);

        intent.setAction(DEC);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonDec, pendingIntent);

        ComponentName myWidget = new ComponentName(context, TimerWidgetProvider.class);
        appWidgetManager.updateAppWidget(myWidget, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(INC)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.timer__widget);
         
            if( count == 99 ) return;
         
            count++;
            remoteViews.setTextViewText(R.id.TextView, String.valueOf(count));

            ComponentName myWidget = new ComponentName(context, TimerWidgetProvider.class);
            appWidgetManager.updateAppWidget(myWidget, remoteViews);
        }

        if (intent.getAction().equals(DEC)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.timer__widget);
            if(count == 0) return;
            
            count--;
            remoteViews.setTextViewText(R.id.TextView, String.valueOf(count));

            ComponentName myWidget = new ComponentName(context, TimerWidgetProvider.class);
            appWidgetManager.updateAppWidget(myWidget, remoteViews);
        }

        if(intent.getAction().equals(SET)) {
            ComponentName serviceComponent = new ComponentName(context, ServiceJob.class);

            JobInfo.Builder builder = new JobInfo.Builder((int)System.currentTimeMillis(), serviceComponent);

            builder.setMinimumLatency(count *  MintoMs)
                    .setOverrideDeadline(2 * count *  MintoMs);
            JobInfo jobInfo = builder.build();

            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(jobInfo);
        }
    }
}
