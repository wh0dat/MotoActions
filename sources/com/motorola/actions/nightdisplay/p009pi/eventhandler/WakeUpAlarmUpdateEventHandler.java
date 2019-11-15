package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.WakeUpAlarmUpdateEventHandler */
public final class WakeUpAlarmUpdateEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(WakeUpAlarmUpdateEventHandler.class);

    WakeUpAlarmUpdateEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        updateWakeUpAlarm();
    }

    public void inPausedState(PausedState pausedState) {
        updateWakeUpAlarm();
    }

    public void inInitialState(UninitializedState uninitializedState) {
        updateWakeUpAlarm();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        updateWakeUpAlarm();
    }

    private void updateWakeUpAlarm() {
        LOGGER.mo11957d("updateWakeUpAlarm");
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
    }
}
