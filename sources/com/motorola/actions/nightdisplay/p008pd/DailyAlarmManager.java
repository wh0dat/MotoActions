package com.motorola.actions.nightdisplay.p008pd;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.common.NightDisplayConstants;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pd.DailyAlarmManager */
class DailyAlarmManager {
    private static final MALogger LOGGER = new MALogger(DailyAlarmManager.class);

    DailyAlarmManager() {
    }

    static void createAlarm() {
        Calendar alarmTime = getAlarmTime(Calendar.getInstance());
        cancelAlarm();
        registerAlarm(alarmTime);
        String format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(alarmTime.getTime());
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getAlarmTime set to ");
        sb.append(alarmTime.getTimeInMillis());
        sb.append(", ");
        sb.append(format);
        mALogger.mo11957d(sb.toString());
    }

    private static PendingIntent getPendingIntent() {
        Intent intent = new Intent(NightDisplayConstants.ACTION_UPDATE_WAKE_UP_ALARM);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), 0, intent, 134217728);
    }

    static void cancelAlarm() {
        LOGGER.mo11957d("cancel alarm");
        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager alarmManager = (AlarmManager) ActionsApplication.getAppContext().getSystemService("alarm");
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private static void registerAlarm(Calendar calendar) {
        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager alarmManager = (AlarmManager) ActionsApplication.getAppContext().getSystemService("alarm");
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(0, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private static Calendar getAlarmTime(Calendar calendar) {
        Calendar calendar2 = (Calendar) calendar.clone();
        if (calendar.get(11) >= 12) {
            calendar2.add(6, 1);
        }
        calendar2.set(11, 12);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        return calendar2;
    }
}
