package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.ServiceResumeEventHandler */
public final class ServiceResumeEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(ServiceResumeEventHandler.class);

    ServiceResumeEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inPausedState(PausedState pausedState) {
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
        } else {
            this.mPIContext.getStateController().changeState(new AutomaticState(this.mPIContext), this.mTimeStamp);
        }
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11963w("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        LOGGER.mo11963w("Invalid state");
    }
}
