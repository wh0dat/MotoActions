package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.ScreenOffEventHandler */
public final class ScreenOffEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(ScreenOffEventHandler.class);

    ScreenOffEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inDisabledState(DisabledState disabledState) {
        this.mPIContext.getPlatformAccess().registerUnlockReceiver();
        unregisterReceiversScreenOff();
    }

    public void inPausedState(PausedState pausedState) {
        handleScreenOffRunningNotification();
        unregisterReceiversScreenOff();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        handleScreenOffRunningNotification();
        unregisterReceiversScreenOff();
    }

    private void unregisterReceiversScreenOff() {
        this.mPIContext.getPlatformAccess().unregisterTimeStampReceiver();
        this.mPIContext.getPlatformAccess().unregisterNotificationActionReceiver();
        this.mPIContext.getPlatformAccess().unregisterUserChangedReceiver();
        this.mPIContext.getPlatformAccess().unregisterNightDisplayUpdateReceiver();
    }

    private void handleScreenOffRunningNotification() {
        this.mPIContext.getPlatformAccess().removeRunningServiceNotification();
        this.mPIContext.getPlatformAccess().registerUnlockReceiver();
    }
}
