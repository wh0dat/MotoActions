package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.State */
public abstract class State {
    /* access modifiers changed from: 0000 */
    public void enterState(Calendar calendar) {
    }

    public abstract void takeEventHandler(EventHandler eventHandler);
}
