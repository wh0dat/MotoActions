package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.ConfigEventHandler */
public final class ConfigEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(ConfigEventHandler.class);

    ConfigEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        updateConfiguration();
        changeToAutomaticIfNeeded();
    }

    public void inPausedState(PausedState pausedState) {
        updateConfiguration();
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
        } else {
            pausedState.update(this.mTimeStamp);
        }
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        updateConfiguration();
        if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            automaticState.update(this.mTimeStamp);
        } else {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
        }
    }

    private void updateConfiguration() {
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
    }
}
