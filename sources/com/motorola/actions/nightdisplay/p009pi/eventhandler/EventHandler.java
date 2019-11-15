package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.EventHandler */
public abstract class EventHandler {
    final PIContext mPIContext;
    final Calendar mTimeStamp;

    public abstract void inAutomaticState(AutomaticState automaticState);

    public abstract void inDisabledState(DisabledState disabledState);

    public abstract void inInitialState(UninitializedState uninitializedState);

    public abstract void inPausedState(PausedState pausedState);

    EventHandler(PIContext pIContext, Calendar calendar) {
        this.mPIContext = pIContext;
        this.mTimeStamp = calendar;
    }

    /* access modifiers changed from: 0000 */
    public void changeToDisabledIfNeeded() {
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
        }
    }

    /* access modifiers changed from: 0000 */
    public void changeToAutomaticIfNeeded() {
        if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            this.mPIContext.getStateController().changeState(new AutomaticState(this.mPIContext), this.mTimeStamp);
        }
    }
}
