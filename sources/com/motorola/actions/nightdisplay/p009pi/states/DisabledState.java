package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.DisabledState */
public class DisabledState extends ContextAwareState {
    public void update(Calendar calendar) {
    }

    public DisabledState(PIContext pIContext) {
        super(pIContext);
    }

    public void takeEventHandler(EventHandler eventHandler) {
        eventHandler.inDisabledState(this);
    }

    public void enterState(Calendar calendar) {
        this.mPIContext.getPlatformAccess().disableTwilight();
        this.mPIContext.getPlatformAccess().removeRunningServiceNotification();
    }
}
