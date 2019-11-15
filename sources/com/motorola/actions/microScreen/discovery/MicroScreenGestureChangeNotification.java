package com.motorola.actions.microScreen.discovery;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.RemoteViews;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.microScreen.model.MicroScreenModel;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;

public class MicroScreenGestureChangeNotification {
    private static final MALogger LOGGER = new MALogger(MicroScreenGestureChangeNotification.class);
    private MicroScreenGestureChangeNotificationReceiver mMicroScreenGestureChangeNotificationReceiver = new MicroScreenGestureChangeNotificationReceiver(this);

    public void createMicroScreenGestureChangedNotification() {
        if (isNeeded()) {
            LOGGER.mo11957d("createMicroScreenGestureChangedNotification");
            NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.notify(NotificationId.ACTIONS_MICROSCREEN_GESTURE_CHANGED.ordinal(), buildMicroScreenGestureChangedNotification());
                MicroScreenModel.saveDoNotShowGestureChangedNotification();
                this.mMicroScreenGestureChangeNotificationReceiver.register();
            }
        }
    }

    private Notification buildMicroScreenGestureChangedNotification() {
        LOGGER.mo11957d("buildMicroScreenGestureChangedNotification");
        RemoteViews buildGestureChangedNotificationView = buildGestureChangedNotificationView();
        return new Builder(ActionsApplication.getAppContext(), ActionsNotificationChannel.FDN.name()).setSmallIcon(C0504R.C0505drawable.ic_notification_moto_actions).setCustomContentView(buildGestureChangedCustomContentView()).setCustomBigContentView(buildGestureChangedNotificationView).setCustomHeadsUpContentView(buildGestureChangedNotificationView).setContentIntent(createMicroscreenSettingsPendingIntent()).setDeleteIntent(createGestureChangedNoThanksPendingIntent()).setAutoCancel(true).setShowWhen(true).build();
    }

    private RemoteViews buildGestureChangedNotificationView() {
        RemoteViews remoteViews = new RemoteViews(ActionsApplication.getAppContext().getPackageName(), C0504R.layout.notification_microscreen_gesture_changed);
        remoteViews.setTextViewText(C0504R.C0506id.text_notif_microscreen_gesture_changed_title, ActionsApplication.getAppContext().getString(C0504R.string.app_name));
        remoteViews.setTextViewText(C0504R.C0506id.text_title_notif_microscreen_gesture_changed, ActionsApplication.getAppContext().getString(C0504R.string.sh_enabled));
        remoteViews.setTextColor(C0504R.C0506id.text_title_notif_microscreen_gesture_changed, ActionsApplication.getAppContext().getColor(C0504R.color.settings_primary_dark));
        remoteViews.setTextViewText(C0504R.C0506id.text_content_notif_microscreen_gesture_changed, ActionsApplication.getAppContext().getString(C0504R.string.sh_notification_gesture_updated));
        remoteViews.setTextColor(C0504R.C0506id.text_content_notif_microscreen_gesture_changed, ActionsApplication.getAppContext().getColor(C0504R.color.text_detail_black));
        remoteViews.setTextViewText(C0504R.C0506id.action_ms_no_thanks, ActionsApplication.getAppContext().getString(C0504R.string.perm_dialog_suggestion_negative));
        remoteViews.setTextViewText(C0504R.C0506id.action_ms_try_it_now, ActionsApplication.getAppContext().getString(C0504R.string.try_it_now_tutorial));
        remoteViews.setOnClickPendingIntent(C0504R.C0506id.action_ms_no_thanks, createGestureChangedNoThanksPendingIntent());
        remoteViews.setOnClickPendingIntent(C0504R.C0506id.action_ms_try_it_now, createMicroscreenSettingsPendingIntent());
        return remoteViews;
    }

    private RemoteViews buildGestureChangedCustomContentView() {
        RemoteViews buildGestureChangedNotificationView = buildGestureChangedNotificationView();
        setMaxLinesResource(buildGestureChangedNotificationView, C0504R.C0506id.text_notif_microscreen_gesture_changed_title, 1);
        setMaxLinesResource(buildGestureChangedNotificationView, C0504R.C0506id.text_title_notif_microscreen_gesture_changed, 1);
        setMaxLinesResource(buildGestureChangedNotificationView, C0504R.C0506id.text_content_notif_microscreen_gesture_changed, 1);
        return buildGestureChangedNotificationView;
    }

    private PendingIntent createMicroscreenSettingsPendingIntent() {
        LOGGER.mo11957d("Creating createMicroscreenSettingsPendingIntent");
        Intent intent = new Intent(MicroScreenGestureChangeNotificationReceiver.ACTION_OPEN_MICROSCREEN_SETTINGS);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), 1, intent, 134217728);
    }

    private PendingIntent createGestureChangedNoThanksPendingIntent() {
        LOGGER.mo11957d("Creating createGestureChangedNoThanksPendingIntent");
        Intent intent = new Intent(MicroScreenGestureChangeNotificationReceiver.ACTION_NO_THANKS);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), 1, intent, 134217728);
    }

    public static boolean isNeeded() {
        boolean z = deviceHadOldGesture() && MicroScreenModel.isMicroScreenEnabled() && !MicroScreenModel.getDoNotShowGestureChangedNotification();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isNeeded: isNeeded = ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public void dismissMicroScreenGestureChangeNotification() {
        LOGGER.mo11957d("dismissAlarmDiscoveryNotification");
        NotificationUtils.dismissNotification(NotificationId.ACTIONS_MICROSCREEN_GESTURE_CHANGED.ordinal());
    }

    private static boolean deviceHadOldGesture() {
        return Device.isAffinityDevice() || Device.isVectorDevice() || Device.isVertexDevice() || Device.isJudoDevice() || Device.isDanteDevice() || Device.isCopperfieldDevice();
    }

    private void setMaxLinesResource(RemoteViews remoteViews, int i, int i2) {
        if (remoteViews != null) {
            remoteViews.setInt(i, "setMaxLines", i2);
        }
    }
}
