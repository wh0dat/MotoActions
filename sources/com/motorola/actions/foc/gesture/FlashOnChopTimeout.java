package com.motorola.actions.foc.gesture;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.utils.MALogger;
import java.util.Objects;

public class FlashOnChopTimeout {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(FlashOnChopTimeout.class);
    private static final int REPEAT_MILLIS_TIME = 150;
    private final PendingIntent mAlarmIntent;
    private boolean mAlarmIsRunning;
    private final AlarmManager mAlarmMgr;
    private final BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            FlashOnChopTimeout.LOGGER.mo11957d("alarm triggered");
            FlashOnChopTimeout.this.mWakeUpEvent.run();
            FlashOnChopTimeout.this.cancel(context);
        }
    };
    /* access modifiers changed from: private */
    public final Runnable mWakeUpEvent;

    @SuppressLint({"ShortAlarm"})
    public FlashOnChopTimeout(Context context, Runnable runnable, long j, String str) {
        this.mWakeUpEvent = runnable;
        this.mAlarmMgr = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent(str);
        intent.setPackage(context.getPackageName());
        context.registerReceiver(this.mAlarmReceiver, new IntentFilter(str));
        this.mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        ((AlarmManager) Objects.requireNonNull(this.mAlarmMgr)).setRepeating(0, System.currentTimeMillis() + j, 150, this.mAlarmIntent);
        this.mAlarmIsRunning = true;
        LOGGER.mo11957d("alarm created");
    }

    public void cancel(Context context) {
        if (this.mAlarmIsRunning) {
            this.mAlarmMgr.cancel(this.mAlarmIntent);
            LOGGER.mo11957d("alarm cancelled");
            try {
                context.unregisterReceiver(this.mAlarmReceiver);
                LOGGER.mo11957d("mAlarmReceiver unregister");
            } catch (IllegalArgumentException e) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("cancel: ");
                sb.append(e.getMessage());
                mALogger.mo11959e(sb.toString());
            }
            this.mAlarmIsRunning = false;
        }
    }
}
