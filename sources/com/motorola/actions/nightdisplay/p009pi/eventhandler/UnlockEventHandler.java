package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.UnlockEventHandler */
public final class UnlockEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(UnlockEventHandler.class);

    UnlockEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inDisabledState(DisabledState disabledState) {
        this.mPIContext.getPlatformAccess().unregisterUnlockReceiver();
    }

    public void inPausedState(PausedState pausedState) {
        createRunningNotificationIfNeeded(false);
        this.mPIContext.getPlatformAccess().unregisterUnlockReceiver();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        createRunningNotificationIfNeeded(true);
        this.mPIContext.getPlatformAccess().unregisterUnlockReceiver();
    }

    private void createRunningNotificationIfNeeded(boolean z) {
        if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getPlatformAccess().createRunningServiceNotification(this.mPIContext.getSchedulingController().getStopServiceTime(this.mTimeStamp), z);
        }
    }
}
