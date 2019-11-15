package com.motorola.actions;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.RunningTasksUtils;
import com.motorola.actions.utils.SecurityUtils;
import com.motorola.actions.utils.ServiceUtils;

public class ForegroundNotificationService extends Service {
    private static final MALogger LOGGER = new MALogger(ForegroundNotificationService.class);
    private Notification mNotification;

    public IBinder onBind(Intent intent) {
        return null;
    }

    private static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), ForegroundNotificationService.class);
    }

    public static void start() {
        if (RunningTasksUtils.isServiceRunning(ActionsApplication.getAppContext(), ForegroundNotificationService.class)) {
            return;
        }
        if (!MultiUserManager.isSystemUser() || SecurityUtils.isUserInDirectBoot()) {
            LOGGER.mo11957d("Starting service");
            ServiceUtils.startForegroundServiceSafe(createIntent());
        }
    }

    public static void stop() {
        if (RunningTasksUtils.isServiceRunning(ActionsApplication.getAppContext(), ForegroundNotificationService.class)) {
            LOGGER.mo11957d("Stopping service");
            ActionsApplication.getAppContext().stopService(createIntent());
        }
    }

    public void onCreate() {
        Resources resources = getResources();
        this.mNotification = new Builder(this, ActionsNotificationChannel.RUNNING_SERVICE.name()).setContentTitle(resources.getString(C0504R.string.background_service_title)).setContentText(resources.getString(C0504R.string.background_service_text)).setSmallIcon(C0504R.C0505drawable.ic_moto_actions_notification).setLocalOnly(true).setOngoing(true).setAutoCancel(false).setOnlyAlertOnce(true).setColor(getColor(C0504R.color.foreground_notification_icon)).build();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        startForeground(NotificationId.ACTIONS_HOLD_NOTIFICATION_SERVICE.ordinal(), this.mNotification);
        return 1;
    }

    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
