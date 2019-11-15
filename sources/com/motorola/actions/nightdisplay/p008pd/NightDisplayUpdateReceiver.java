package com.motorola.actions.nightdisplay.p008pd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.p001v4.content.LocalBroadcastManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.nightdisplay.p009pi.PIEvent.Event;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pd.NightDisplayUpdateReceiver */
public class NightDisplayUpdateReceiver extends BroadcastReceiver {
    public static final String ACTION_NIGHT_DISPLAY_PREVIEW = "com.motorola.actions.nightdisplay.pd.ACTION_NIGHT_DISPLAY_PREVIEW";
    public static final String EXTRA_INTENSITY = "EXTRA_INTENSITY";
    private static final MALogger LOGGER = new MALogger(NightDisplayUpdateReceiver.class);
    private final PdHandlerThread mPdHandlerThread;
    private boolean mRegistered;

    public NightDisplayUpdateReceiver(PdHandlerThread pdHandlerThread) {
        this.mPdHandlerThread = pdHandlerThread;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_NIGHT_DISPLAY_PREVIEW.equals(intent.getAction())) {
            int intExtra = intent.getIntExtra(EXTRA_INTENSITY, 0);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive. Temperature = ");
            sb.append(intExtra);
            mALogger.mo11957d(sb.toString());
            this.mPdHandlerThread.event(new PIEvent(Event.PREVIEW_UPDATE, intent.getExtras()));
            if (intExtra == 0) {
                this.mPdHandlerThread.event(new PIEvent(Event.CONFIGURATION_CHANGED));
            }
        }
    }

    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_NIGHT_DISPLAY_PREVIEW);
            LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister next alarm changed receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
