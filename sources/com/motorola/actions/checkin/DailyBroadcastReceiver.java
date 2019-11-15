package com.motorola.actions.checkin;

import android.content.Context;
import android.content.Intent;
import android.support.p001v4.content.WakefulBroadcastReceiver;
import com.motorola.actions.discovery.DiscoveryManager;

public class DailyBroadcastReceiver extends WakefulBroadcastReceiver {
    public static final String ACTION_ALARM = "com.motorola.actions.alarm.checkin";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ACTION_ALARM.equals(intent.getAction())) {
                CheckinAlarm.getInstance().performDailyCheckin();
                DiscoveryManager.getInstance().handleFDNDelayCount();
            }
            completeWakefulIntent(intent);
        }
    }
}
