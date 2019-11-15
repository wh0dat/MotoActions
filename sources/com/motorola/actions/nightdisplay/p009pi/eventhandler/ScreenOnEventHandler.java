package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.ScreenOnEventHandler */
public final class ScreenOnEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(ScreenOnEventHandler.class);

    ScreenOnEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inDisabledState(DisabledState disabledState) {
        registerReceiversScreenOn();
        changeToAutomaticIfNeeded();
    }

    public void inPausedState(PausedState pausedState) {
        registerReceiversScreenOn();
        changeToDisabledIfNeeded();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        registerReceiversScreenOn();
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
        } else {
            automaticState.updateTimeStamp(this.mTimeStamp);
        }
    }

    private void registerReceiversScreenOn() {
        this.mPIContext.getPlatformAccess().registerTimeStampReceiver();
        this.mPIContext.getPlatformAccess().registerNotificationActionReceiver();
        this.mPIContext.getPlatformAccess().registerUserChangedReceiver();
        this.mPIContext.getPlatformAccess().registerNightDisplayUpdateReceiver();
    }
}
