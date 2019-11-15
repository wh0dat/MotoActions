package com.motorola.actions.foc.gesture.receiver;

import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import com.motorola.actions.C0504R;
import com.motorola.actions.foc.gesture.service.FlashOnChopService;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.SetupObserver;

public class FlashOnChopForegroundReceiver extends BroadcastReceiver {
    public static final String FLASH_ON_CHOP_TO_BACKGROUND = "com.motorola.FLASH_ON_CHOP_TO_BACKGROUND";
    public static final String FLASH_ON_CHOP_TO_FOREGROUND = "com.motorola.FLASH_ON_CHOP_TO_FOREGROUND";
    private static final MALogger LOGGER = new MALogger(FlashOnChopForegroundReceiver.class);
    private FlashOnChopService mFlashOnChopService = null;

    public FlashOnChopForegroundReceiver(FlashOnChopService flashOnChopService) {
        this.mFlashOnChopService = flashOnChopService;
    }

    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            LOGGER.mo11959e("onReceive: Context must be not null");
        } else if (intent == null) {
            LOGGER.mo11959e("onReceive: Intent must be not null");
        } else if (this.mFlashOnChopService == null) {
            LOGGER.mo11959e("onReceive: mFlashOnChopService must be not null");
        } else if (!SetupObserver.isSetupFinished()) {
            LOGGER.mo11959e("onReceive: FOC should not work on OOBE");
        } else {
            String action = intent.getAction();
            if (FLASH_ON_CHOP_TO_FOREGROUND.equals(action)) {
                LOGGER.mo11957d("onReceive: FLASH_ON_CHOP_TO_FOREGROUND");
                PendingIntent service = PendingIntent.getService(context, 0, FlashOnChopService.createIntent(context), 134217728);
                Resources resources = context.getResources();
                this.mFlashOnChopService.startForeground(NotificationId.ACTIONS_FLASHONCHOPFOREGROUNDRECEIVER_FOREGROUND.ordinal(), new Builder(context, ActionsNotificationChannel.GENERAL.name()).setContentTitle(resources.getString(C0504R.string.foc_turn_off_notification_title)).setContentText(resources.getString(C0504R.string.foc_turn_off_notification_text)).setSmallIcon(C0504R.C0505drawable.ic_stat_flash).setContentIntent(service).setLocalOnly(true).setOngoing(true).setAutoCancel(true).setColor(context.getColor(C0504R.color.notification_accent)).build());
                LOGGER.mo11957d("onReceive: startForeground");
            } else if (FLASH_ON_CHOP_TO_BACKGROUND.equals(action)) {
                LOGGER.mo11957d("onReceive: FLASH_ON_CHOP_TO_BACKGROUND");
                this.mFlashOnChopService.stopForeground(true);
                LOGGER.mo11957d("onReceive: stopForeground");
            }
        }
    }
}
