package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.common.TimeSlot;
import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.AutomaticState */
public class AutomaticState extends ContextAwareState {
    private TimeSlot mTimeSlot;

    public AutomaticState(PIContext pIContext) {
        super(pIContext);
    }

    public void takeEventHandler(EventHandler eventHandler) {
        eventHandler.inAutomaticState(this);
    }

    /* access modifiers changed from: 0000 */
    public void enterState(Calendar calendar) {
        this.mTimeSlot = new TimeSlot(calendar);
        update(calendar);
    }

    public void updateTimeStamp(Calendar calendar) {
        if (!new TimeSlot(calendar).equals(this.mTimeSlot)) {
            update(calendar);
        }
    }

    public void update(Calendar calendar) {
        this.mTimeSlot = new TimeSlot(calendar);
        this.mPIContext.getPlatformAccess().enableTwilight(this.mPIContext.getPlatformAccess().getColorTemperature());
        this.mPIContext.getPlatformAccess().createRunningServiceNotification(this.mPIContext.getSchedulingController().getStopServiceTime(calendar), true);
    }
}
