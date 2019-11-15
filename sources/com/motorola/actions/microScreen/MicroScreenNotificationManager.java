package com.motorola.actions.microScreen;

import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;

public class MicroScreenNotificationManager {
    private static final MALogger LOGGER = new MALogger(MicroScreenNotificationManager.class);

    public static final Intent createIntentOverlaySingleHand() {
        LOGGER.mo11957d("Creating intent to overlay permission for SingleHandService");
        Intent intent = new Intent();
        intent.setAction("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:com.motorola.frameworks.singlehand"));
        intent.addFlags(ErrorDialogData.BINDER_CRASH);
        intent.addFlags(ErrorDialogData.SUPPRESSED);
        intent.addFlags(8388608);
        return intent;
    }

    private static PendingIntent createPendingIntentOverlaySingleHand() {
        LOGGER.mo11957d("Creating PendingIntent to overlay permission for SingleHandService");
        return PendingIntent.getActivity(ActionsApplication.getAppContext(), 0, createIntentOverlaySingleHand(), 0);
    }

    private static Notification createNotificationOverlaySingleHand() {
        LOGGER.mo11957d("Creating the notification to alert the user that SingleHandService needs overlay permission");
        Builder autoCancel = new Builder(ActionsApplication.getAppContext(), ActionsNotificationChannel.FDN.name()).setSmallIcon(C0504R.C0505drawable.ic_notification_moto_actions).setColor(ActionsApplication.getAppContext().getColor(C0504R.color.notification_accent)).setContentTitle(ActionsApplication.getAppContext().getString(C0504R.string.sh_notification_access_title)).setContentText(ActionsApplication.getAppContext().getString(C0504R.string.sh_notification_access_text)).setContentIntent(createPendingIntentOverlaySingleHand()).setAutoCancel(true);
        BigTextStyle bigTextStyle = new BigTextStyle();
        StringBuilder sb = new StringBuilder();
        sb.append(ActionsApplication.getAppContext().getString(C0504R.string.sh_notification_access_text));
        sb.append(ActionsApplication.getAppContext().getString(C0504R.string.sh_notification_access_expand_text));
        return autoCancel.setStyle(bigTextStyle.bigText(sb.toString())).build();
    }

    static void notifyLostPermission() {
        if (!MicroScreenService.isServiceEnabled()) {
            LOGGER.mo11957d("Microscreen is not enabled. No need to do notifyLostPermission");
            return;
        }
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            try {
                notificationManager.notify(NotificationId.ACTIONS_MICROSCREEN_LOST_PERMISSION.ordinal(), createNotificationOverlaySingleHand());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dismissNotification() {
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_MICROSCREEN_LOST_PERMISSION.ordinal());
    }
}
