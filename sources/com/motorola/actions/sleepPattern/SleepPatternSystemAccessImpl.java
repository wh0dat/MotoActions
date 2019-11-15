package com.motorola.actions.sleepPattern;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import javax.inject.Inject;

public class SleepPatternSystemAccessImpl implements SleepPatternSystemAccess {
    private static final MALogger LOGGER = new MALogger(SleepPatternSystemAccessImpl.class);
    private Context mContext;

    @Inject
    public SleepPatternSystemAccessImpl(Application application) {
        this.mContext = application;
    }

    public Calendar getTimestamp() {
        return Calendar.getInstance();
    }

    public void reschedule() {
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        if (alarmManager != null) {
            alarmManager.cancel(SleepEventsAlarmReceiver.getSleepPatternPendingIntent(this.mContext));
        } else {
            LOGGER.mo11959e("Could not retrieve alarm manager");
        }
        SleepPatternService.scheduleSleepPatternServiceStart(this.mContext);
    }
}
