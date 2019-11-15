package com.motorola.actions.attentivedisplay.notification;

import android.app.Notification.Action;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.MALogger;

public abstract class ADDisabledFeatureBaseNotification {
    private static final MALogger LOGGER = new MALogger(ADDisabledFeatureBaseNotification.class);

    /* access modifiers changed from: protected */
    public abstract String getContentText();

    /* access modifiers changed from: protected */
    public abstract int getDontShowAgainRequestCode();

    /* access modifiers changed from: protected */
    public abstract int getGotItRequestCode();

    /* access modifiers changed from: protected */
    public abstract String getNotificationDontShowAgainAction();

    /* access modifiers changed from: protected */
    public abstract String getNotificationGotItAction();

    /* access modifiers changed from: protected */
    public abstract int getNotificationId();

    public void showNotification() {
        LOGGER.mo11957d("showNotification");
        Context appContext = ActionsApplication.getAppContext();
        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService("notification");
        if (notificationManager == null) {
            LOGGER.mo11959e("showNotification, unable to show notification. NotificationManager is null");
            return;
        }
        Action createPendingIntent = createPendingIntent(getNotificationGotItAction(), getGotItRequestCode(), appContext.getString(C0504R.string.notification_got_it_action));
        notificationManager.notify(getNotificationId(), new Builder(appContext, ActionsNotificationChannel.GENERAL.name()).setColor(appContext.getResources().getColor(C0504R.color.notification_accent, null)).setContentTitle(appContext.getString(C0504R.string.ad_mod_notification_content_title)).setContentText(getContentText()).setSmallIcon(C0504R.C0505drawable.ic_moto_actions_notification).setStyle(new BigTextStyle()).addAction(createPendingIntent).addAction(createPendingIntent(getNotificationDontShowAgainAction(), getDontShowAgainRequestCode(), appContext.getString(C0504R.string.notification_dont_show_again_action))).setOngoing(true).build());
    }

    private static Action createPendingIntent(String str, int i, String str2) {
        Intent intent = new Intent(str);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return new Action.Builder(null, str2, PendingIntent.getBroadcast(ActionsApplication.getAppContext(), i, intent, ErrorDialogData.BINDER_CRASH)).build();
    }
}
