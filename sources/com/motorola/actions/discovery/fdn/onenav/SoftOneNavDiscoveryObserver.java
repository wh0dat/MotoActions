package com.motorola.actions.discovery.fdn.onenav;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.discovery.DiscoveryManager.FDNDelayMode;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

public class SoftOneNavDiscoveryObserver extends ContentObserver {
    private static final String ACTION_HOUR_ALARM = "com.motorola.actions.HOUR_ALARM";
    private static final int DEBUG_COUNT_FOR_FDN = 0;
    private static final int HOURS_COUNT_FOR_FDN = 24;
    public static final String KEY_HOURS_PAST_HINT = "hours_past_softonenav_hint";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(SoftOneNavDiscoveryObserver.class);
    private AlarmManager mAlarmManager;
    private HourBroadcastReceiver mBroadcastReceiver;
    private boolean mRegistered;

    private static class HourBroadcastReceiver extends BroadcastReceiver {
        private HourBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int i = SharedPreferenceManager.getInt(SoftOneNavDiscoveryObserver.KEY_HOURS_PAST_HINT, 0) + 1;
            SharedPreferenceManager.putInt(SoftOneNavDiscoveryObserver.KEY_HOURS_PAST_HINT, i);
            MALogger access$100 = SoftOneNavDiscoveryObserver.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("HourBroadcastReceiver onReceive. currentHours: ");
            sb.append(i);
            access$100.mo11957d(sb.toString());
            if (i <= (SoftOneNavDiscoveryObserver.isDebugFDNMode() ? 0 : 24)) {
                return;
            }
            if (MotorolaSettings.getSoftOneNavDiscovery() == 1) {
                SoftOneNavDiscoveryObserver.LOGGER.mo11957d("Hint is still active, dismiss and reset counter");
                MotorolaSettings.setSoftOneNavDiscovery(2);
                return;
            }
            SoftOneNavDiscoveryObserver.LOGGER.mo11957d("Hint is dismissed, trigger FDN");
            DiscoveryManager.getInstance().onFDNEvent(FeatureKey.ONE_NAV);
            MotorolaSettings.setSoftOneNavDiscovery(0);
        }
    }

    SoftOneNavDiscoveryObserver(Handler handler) {
        super(handler);
    }

    /* access modifiers changed from: 0000 */
    public void observe() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Start observing SoftOneNav Discovery status. Registered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            this.mRegistered = true;
            ActionsApplication.getAppContext().getContentResolver().registerContentObserver(MotorolaSettings.getSoftOneNavDiscoveryUri(), false, this);
        }
    }

    /* access modifiers changed from: 0000 */
    public void stop() {
        LOGGER.mo11957d("Stop observing SoftOneNav Discovery status");
        ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this);
        this.mRegistered = false;
        try {
            ActionsApplication.getAppContext().unregisterReceiver(this.mBroadcastReceiver);
        } catch (IllegalArgumentException unused) {
            LOGGER.mo11959e("Couldn't unregister receiver");
        }
        if (this.mAlarmManager != null) {
            this.mAlarmManager.cancel(getHourIntent());
        }
        this.mAlarmManager = null;
    }

    public void onChange(boolean z) {
        int softOneNavDiscovery = MotorolaSettings.getSoftOneNavDiscovery();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Discovery status changed to: ");
        sb.append(softOneNavDiscovery);
        mALogger.mo11957d(sb.toString());
        if (softOneNavDiscovery == 1 && this.mAlarmManager == null) {
            LOGGER.mo11957d("Register hourly alarm AND receiver");
            this.mBroadcastReceiver = new HourBroadcastReceiver();
            ActionsApplication.getAppContext().registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION_HOUR_ALARM));
            this.mAlarmManager = (AlarmManager) ActionsApplication.getAppContext().getSystemService("alarm");
            this.mAlarmManager.setRepeating(1, Calendar.getInstance().getTimeInMillis(), isDebugFDNMode() ? 900000 : 3600000, getHourIntent());
        } else if (softOneNavDiscovery == 2) {
            SharedPreferenceManager.putInt(KEY_HOURS_PAST_HINT, 0);
        } else if (softOneNavDiscovery == 3) {
            DiscoveryManager.getInstance().cancelFDN(FeatureKey.ONE_NAV);
        }
    }

    private PendingIntent getHourIntent() {
        Intent intent = new Intent(ACTION_HOUR_ALARM);
        intent.setPackage(ActionsApplication.getAppContext().getPackageName());
        return PendingIntent.getBroadcast(ActionsApplication.getAppContext(), 0, intent, 0);
    }

    /* access modifiers changed from: private */
    public static boolean isDebugFDNMode() {
        return DiscoveryManager.getInstance().getFDNDelayMode() == FDNDelayMode.DEBUG.ordinal();
    }
}
