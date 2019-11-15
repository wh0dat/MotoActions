package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.ContextAwareState */
abstract class ContextAwareState extends State {
    final PIContext mPIContext;

    public abstract void update(Calendar calendar);

    ContextAwareState(PIContext pIContext) {
        this.mPIContext = pIContext;
    }
}
