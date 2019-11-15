package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.DestroyEventHandler */
public final class DestroyEventHandler extends EventHandler {
    DestroyEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        finish();
    }

    public void inPausedState(PausedState pausedState) {
        finish();
    }

    public void inInitialState(UninitializedState uninitializedState) {
        finish();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        finish();
    }

    private void finish() {
        this.mPIContext.getPlatformAccess().disableTwilight();
        this.mPIContext.getPlatformAccess().unregisterTimeStampReceiver();
        this.mPIContext.getPlatformAccess().unregisterScreenOnOffReceiver();
        this.mPIContext.getPlatformAccess().unregisterWakeUpAlarmUpdateReceiver();
        this.mPIContext.getPlatformAccess().unregisterNotificationActionReceiver();
        this.mPIContext.getPlatformAccess().unregisterUserChangedReceiver();
        this.mPIContext.getPlatformAccess().unregisterNightDisplayUpdateReceiver();
        this.mPIContext.getPlatformAccess().unregisterUnlockReceiver();
        this.mPIContext.getPlatformAccess().removeRunningServiceNotification();
    }
}
