package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.ServicePauseEventHandler */
public final class ServicePauseEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(ServicePauseEventHandler.class);

    ServicePauseEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inPausedState(PausedState pausedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new PausedState(this.mPIContext), this.mTimeStamp);
        }
    }
}
