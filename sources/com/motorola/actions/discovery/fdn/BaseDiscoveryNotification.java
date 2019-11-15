package com.motorola.actions.discovery.fdn;

import android.app.Notification;
import android.app.Notification.Action;
import android.app.Notification.Action.Builder;
import android.app.Notification.BigPictureStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.instrumentation.DiscoveryInstrumentation;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.ImageUtils;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationUtils;

public abstract class BaseDiscoveryNotification {
    private static final MALogger LOGGER = new MALogger(BaseDiscoveryNotification.class);
    protected FeatureKey mFeatureKey;

    /* access modifiers changed from: protected */
    public abstract int getBigPicture();

    /* access modifiers changed from: protected */
    public abstract int getFDNDismissSwipeActionId();

    /* access modifiers changed from: protected */
    public abstract int getFDNNoThanksActionId();

    /* access modifiers changed from: protected */
    public abstract int getFDNSettingsActionId();

    /* access modifiers changed from: protected */
    public abstract int getFeatureFDNId();

    /* access modifiers changed from: protected */
    public abstract int getNotificationContent();

    /* access modifiers changed from: protected */
    public abstract int getNotificationTitle();

    /* access modifiers changed from: protected */
    public abstract int getPreviewImage();

    protected BaseDiscoveryNotification(FeatureKey featureKey) {
        this.mFeatureKey = featureKey;
    }

    public Notification buildDiscoveryNotification() {
        Context appContext = ActionsApplication.getAppContext();
        Bitmap decode = ImageUtils.decode(appContext.getResources(), getBigPicture());
        PendingIntent createNoThanksPendingIntent = createNoThanksPendingIntent();
        PendingIntent tryItNowPendingIntent = tryItNowPendingIntent();
        PendingIntent createDismissSwipePendingIntent = createDismissSwipePendingIntent();
        PendingIntent createClickPendingIntent = createClickPendingIntent();
        Action build = new Builder(Icon.createWithResource(appContext, C0504R.C0505drawable.ic_action_no_thanks), appContext.getString(C0504R.string.perm_dialog_suggestion_negative), createNoThanksPendingIntent).build();
        return new Notification.Builder(appContext, ActionsNotificationChannel.FDN.name()).setColor(appContext.getResources().getColor(C0504R.color.notification_accent, null)).setContentTitle(appContext.getString(getNotificationTitle())).setContentText(appContext.getString(getNotificationContent())).setSmallIcon(C0504R.C0505drawable.ic_moto_actions_notification).setLargeIcon(ImageUtils.decode(appContext.getResources(), getPreviewImage())).setStyle(new BigPictureStyle().bigLargeIcon(null).bigPicture(decode)).setContentIntent(createClickPendingIntent).setDeleteIntent(createDismissSwipePendingIntent).addAction(build).addAction(new Builder(Icon.createWithResource(appContext, C0504R.C0505drawable.ic_action_done), appContext.getString(C0504R.string.try_it_now_tutorial), tryItNowPendingIntent).build()).setAutoCancel(true).build();
    }

    private PendingIntent createSettingsPendingIntent(String str) {
        Intent intent = new Intent(str);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        intent.putExtra(NotificationReceiver.EXTRA_FDN_ID, this.mFeatureKey);
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), getFDNSettingsActionId(), intent, ErrorDialogData.BINDER_CRASH);
    }

    private PendingIntent createClickPendingIntent() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Creating createClickPendingIntent ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        return createSettingsPendingIntent("com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_CLICK");
    }

    private PendingIntent tryItNowPendingIntent() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Creating tryItNowPendingIntent ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        return createSettingsPendingIntent("com.motorola.actions.discovery.ACTION_OPEN_SETTINGS_TRY_IT_NOW");
    }

    private PendingIntent createNoThanksPendingIntent() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Creating createNoThanksPendingIntent ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        Intent intent = new Intent("com.motorola.actions.discovery.ACTION_NO_THANKS");
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        intent.putExtra(NotificationReceiver.EXTRA_FDN_ID, this.mFeatureKey);
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), getFDNNoThanksActionId(), intent, ErrorDialogData.BINDER_CRASH);
    }

    private PendingIntent createDismissSwipePendingIntent() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Creating createDismissSwipePendingIntent ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        Intent intent = new Intent("com.motorola.actions.discovery.ACTION_DISMISS_SWIPE");
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        intent.putExtra(NotificationReceiver.EXTRA_FDN_ID, this.mFeatureKey);
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), getFDNDismissSwipeActionId(), intent, ErrorDialogData.BINDER_CRASH);
    }

    public void show(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("showDiscoveryNotification for ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        if (!NotificationUtils.isNotificationActive(getFeatureFDNId())) {
            LOGGER.mo11957d("Notification is not active");
            NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
            Notification buildDiscoveryNotification = buildDiscoveryNotification();
            if (notificationManager != null) {
                notificationManager.notify(getFeatureFDNId(), buildDiscoveryNotification);
            } else {
                LOGGER.mo11959e("Unable to retrieve notification manager");
            }
            if (z) {
                DiscoveryInstrumentation.recordShowFDN(this.mFeatureKey.ordinal());
            }
        }
    }

    public void dismiss() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("dismissDiscoveryNotification for ");
        sb.append(this.mFeatureKey);
        mALogger.mo11957d(sb.toString());
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancel(getFeatureFDNId());
        }
    }
}
