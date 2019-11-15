package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.common.NightDisplayConstants;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.WakeUpAlarmUpdateReceiver */
class WakeUpAlarmUpdateReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(WakeUpAlarmUpdateReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    WakeUpAlarmUpdateReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        DailyAlarmManager.createAlarm();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Received ");
        sb.append(intent.getAction());
        mALogger.mo11957d(sb.toString());
        this.mPdHandlerThread.event(new PIEvent(Event.WAKE_UP_ALARM_UPDATE));
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            LOGGER.mo11957d("Registering receiver");
            DailyAlarmManager.createAlarm();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NightDisplayConstants.ACTION_UPDATE_WAKE_UP_ALARM);
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void unregister() {
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
                DailyAlarmManager.cancelAlarm();
                LOGGER.mo11957d("Unregistering receiver");
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister receiver", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
