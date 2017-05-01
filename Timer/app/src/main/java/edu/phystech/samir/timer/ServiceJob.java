package edu.phystech.samir.timer;

import android.app.job.JobService;
import android.app.job.JobParameters;

/**
 * Created by Samir on 30.04.2017.
 */

public class ServiceJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        ScheduledService.startScheduledJob(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}