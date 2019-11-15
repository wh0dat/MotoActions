package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.PausedState */
public class PausedState extends ContextAwareState {
    public PausedState(PIContext pIContext) {
        super(pIContext);
    }

    public void takeEventHandler(EventHandler eventHandler) {
        eventHandler.inPausedState(this);
    }

    public void enterState(Calendar calendar) {
        this.mPIContext.getPlatformAccess().disableTwilight();
        updateNotification(calendar);
    }

    public void update(Calendar calendar) {
        updateNotification(calendar);
    }

    private void updateNotification(Calendar calendar) {
        this.mPIContext.getPlatformAccess().createRunningServiceNotification(this.mPIContext.getSchedulingController().getStopServiceTime(calendar), false);
    }
}
