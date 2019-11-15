package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.UserChangedEventHandler */
public final class UserChangedEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(UserChangedEventHandler.class);

    UserChangedEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
        resetIntensity();
        changeToAutomaticIfNeeded();
    }

    public void inPausedState(PausedState pausedState) {
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
            return;
        }
        resetIntensity();
        pausedState.update(this.mTimeStamp);
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
        if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            resetIntensity();
            automaticState.update(this.mTimeStamp);
            return;
        }
        this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
    }

    private void resetIntensity() {
        LOGGER.mo11959e("reset intensity");
        this.mPIContext.getPlatformAccess().disableTwilight();
    }
}
