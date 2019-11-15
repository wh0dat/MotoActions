package com.motorola.actions.nightdisplay.p008pd;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.RemoteViews;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.p013ui.settings.SettingsActivity;
import com.motorola.actions.utils.DateUtilities;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pd.RunningNotificationManager */
class RunningNotificationManager {
    private static final MALogger LOGGER = new MALogger(RunningNotificationManager.class);

    RunningNotificationManager() {
    }

    private PendingIntent createNightDisplaySettingsPendingIntent() {
        LOGGER.mo11957d("Creating createNightDisplaySettingsPendingIntent");
        return PendingIntent.getActivity(ActionsApplication.getAppContext(), 0, SettingsActivity.getFeatureLaunchIntent(FeatureKey.NIGHT_DISPLAY.ordinal()), 0);
    }

    @SuppressLint({"StringFormatInvalid"})
    private String getNotificationContentText(int i, Calendar calendar) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getNotificationContentText: stopTimeCal = ");
        sb.append(calendar.getTime().toString());
        mALogger.mo11957d(sb.toString());
        String androidFormattedTime = DateUtilities.getAndroidFormattedTime(calendar);
        return ActionsApplication.getAppContext().getString(i, new Object[]{androidFormattedTime});
    }

    /* access modifiers changed from: 0000 */
    public void createServiceRunningNotification(Calendar calendar, boolean z) {
        LOGGER.mo11957d("createServiceRunningNotification");
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.notify(NotificationId.ACTIONS_NIGHTDISPLAY_SERVICE_RUNNING.ordinal(), createNightServiceRunningNotification(calendar, z));
        } else {
            LOGGER.mo11957d("notificationManager is null to create the running notification ");
        }
    }

    @SuppressLint({"StringFormatInvalid"})
    private Notification createNightServiceRunningNotification(Calendar calendar, boolean z) {
        LOGGER.mo11957d("createNightServiceRunningNotification");
        String notificationContentText = getNotificationContentText(C0504R.string.night_display_service_running_notification, calendar);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("NightServiceRunningNotificationText = ");
        sb.append(notificationContentText);
        mALogger.mo11957d(sb.toString());
        return new Builder(ActionsApplication.getAppContext(), ActionsNotificationChannel.NIGHT_DISPLAY_RUNNING.name()).setSmallIcon(C0504R.C0505drawable.ic_actions_night_display_alarm_notification).setCustomContentView(buildNotificationView(calendar, z)).setContentIntent(createNightDisplaySettingsPendingIntent()).setAutoCancel(false).setOngoing(true).setShowWhen(true).build();
    }

    /* access modifiers changed from: 0000 */
    public void dismissServiceRunningNotification() {
        LOGGER.mo11957d("dismissServiceRunningNotification");
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_NIGHTDISPLAY_SERVICE_RUNNING.ordinal());
    }

    private RemoteViews buildNotificationView(Calendar calendar, boolean z) {
        RemoteViews remoteViews = new RemoteViews(ActionsApplication.getAppContext().getPackageName(), C0504R.layout.notification_night_display);
        remoteViews.setTextViewText(C0504R.C0506id.text_notification_nightdisplay_title, ActionsApplication.getAppContext().getString(C0504R.string.night_shade_enabled));
        remoteViews.setTextViewText(C0504R.C0506id.status, getNotificationContentText(C0504R.string.night_display_service_running_notification, calendar));
        if (!z) {
            remoteViews.setImageViewResource(C0504R.C0506id.controller, C0504R.C0505drawable.ic_actions_nightdisplay_play);
            remoteViews.setOnClickPendingIntent(C0504R.C0506id.controller, getNotificationActionIntent(NotificationActionReceiver.ACTION_NIGHT_DISPLAY_RESUME));
        } else {
            remoteViews.setImageViewResource(C0504R.C0506id.controller, C0504R.C0505drawable.ic_actions_nightdisplay_pause);
            remoteViews.setOnClickPendingIntent(C0504R.C0506id.controller, getNotificationActionIntent(NotificationActionReceiver.ACTION_NIGHT_DISPLAY_PAUSE));
        }
        return remoteViews;
    }

    private PendingIntent getNotificationActionIntent(String str) {
        Intent intent = new Intent(str);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), 0, intent, 134217728);
    }
}
