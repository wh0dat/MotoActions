package com.motorola.actions.attentivedisplay.notification;

import android.content.Context;
import android.content.Intent;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.attentivedisplay.receiver.BatterySaverReceiver;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;
import java.util.ArrayList;
import java.util.List;

public class ADBatterySaverNotificationReceiver extends ADBaseNotificationReceiver {
    public static final String ACTION_AD_BATTERY_NOTIFICATION_DONT_SHOW_AGAIN = "com.motorola.actions.ACTION_AD_BATTERY_NOTIFICATION_DONT_SHOW_AGAIN";
    public static final String ACTION_AD_BATTERY_NOTIFICATION_GOT_IT = "com.motorola.actions.ACTION_AD_BATTERY_NOTIFICATION_GOT_IT";
    private static final MALogger LOGGER = new MALogger(ADBatterySaverNotificationReceiver.class);
    public static final String SHOULD_NOT_SHOW_AGAIN_AD_BATTERY_PREFERENCE_KEY = "should_not_show_again_ad_battery_notification";

    private static class SingletonHolder {
        static final ADBatterySaverNotificationReceiver INSTANCE = new ADBatterySaverNotificationReceiver();

        private SingletonHolder() {
        }
    }

    public static ADBatterySaverNotificationReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* access modifiers changed from: protected */
    public List<String> getNotificationActions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ACTION_AD_BATTERY_NOTIFICATION_GOT_IT);
        arrayList.add(ACTION_AD_BATTERY_NOTIFICATION_DONT_SHOW_AGAIN);
        return arrayList;
    }

    public void onReceive(Context context, Intent intent) {
        LOGGER.mo11957d("onReceive");
        if (intent == null || intent.getAction() == null) {
            LOGGER.mo11959e("onReceive: missing action");
            return;
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onReceive: action = ");
        sb.append(intent.getAction());
        mALogger.mo11957d(sb.toString());
        String action = intent.getAction();
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -827030496) {
            if (hashCode == 1250707498 && action.equals(ACTION_AD_BATTERY_NOTIFICATION_GOT_IT)) {
                c = 0;
            }
        } else if (action.equals(ACTION_AD_BATTERY_NOTIFICATION_DONT_SHOW_AGAIN)) {
            c = 1;
        }
        switch (c) {
            case 0:
                NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_BATTERY_SAVER_INCOMPATIBILITY.ordinal());
                unregister();
                return;
            case 1:
                SharedPreferenceManager.putBoolean(SHOULD_NOT_SHOW_AGAIN_AD_BATTERY_PREFERENCE_KEY, true);
                BatterySaverReceiver.getInstance().unregister();
                NotificationUtils.dismissNotification(NotificationId.ACTIONS_AD_BATTERY_SAVER_INCOMPATIBILITY.ordinal());
                unregister();
                return;
            default:
                MALogger mALogger2 = LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onReceive: unexpected action: ");
                sb2.append(intent.getAction());
                mALogger2.mo11959e(sb2.toString());
                return;
        }
    }
}
