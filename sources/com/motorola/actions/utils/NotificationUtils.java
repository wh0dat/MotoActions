package com.motorola.actions.utils;

import android.app.NotificationManager;
import android.service.notification.StatusBarNotification;
import com.motorola.actions.ActionsApplication;

public class NotificationUtils {
    public static void dismissNotification(int i) {
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancel(i);
        }
    }

    public static boolean isNotificationActive(int i) {
        for (StatusBarNotification id : ((NotificationManager) ActionsApplication.getAppContext().getSystemService("notification")).getActiveNotifications()) {
            if (id.getId() == i) {
                return true;
            }
        }
        return false;
    }
}
