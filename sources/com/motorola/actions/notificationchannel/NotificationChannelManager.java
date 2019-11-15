package com.motorola.actions.notificationchannel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;

public class NotificationChannelManager {
    private static final MALogger LOGGER = new MALogger(NotificationChannelManager.class);
    private LocaleChangedReceiver mLocaleChangedReceiver = new LocaleChangedReceiver();

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final NotificationChannelManager INSTANCE = new NotificationChannelManager();

        private SingletonHolder() {
        }
    }

    public static NotificationChannelManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void createChannels() {
        ActionsNotificationChannel[] values;
        LOGGER.mo11957d("createChannels");
        NotificationManager notificationManager = (NotificationManager) ActionsApplication.getAppContext().getSystemService("notification");
        for (ActionsNotificationChannel actionsNotificationChannel : ActionsNotificationChannel.values()) {
            NotificationChannel notificationChannel = new NotificationChannel(actionsNotificationChannel.name(), ActionsApplication.getAppContext().getString(actionsNotificationChannel.getTitle()), actionsNotificationChannel.getImportance());
            notificationChannel.setDescription(ActionsApplication.getAppContext().getString(actionsNotificationChannel.getDescription()));
            notificationManager.createNotificationChannel(notificationChannel);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("createChannels - created: ");
            sb.append(actionsNotificationChannel.getTitle());
            mALogger.mo11957d(sb.toString());
        }
    }

    public void start() {
        createChannels();
        this.mLocaleChangedReceiver.register();
    }

    public void stop() {
        this.mLocaleChangedReceiver.unregister();
    }
}
