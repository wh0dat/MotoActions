package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.State;
import com.motorola.actions.nightdisplay.p009pi.states.StateController;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.InitEventHandler */
public final class InitEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(InitEventHandler.class);

    InitEventHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inPausedState(PausedState pausedState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inInitialState(UninitializedState uninitializedState) {
        State state;
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
        if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
            LOGGER.mo11957d("Creating disabled state");
            state = new DisabledState(this.mPIContext);
        } else if (PausedState.class.getSimpleName().equals(SharedPreferenceManager.getString(StateController.NIGHT_DISPLAY_STATE, DisabledState.class.getSimpleName()))) {
            LOGGER.mo11957d("Creating paused state");
            state = new PausedState(this.mPIContext);
        } else {
            LOGGER.mo11957d("Creating automatic state");
            state = new AutomaticState(this.mPIContext);
        }
        this.mPIContext.getStateController().changeState(state, this.mTimeStamp);
        this.mPIContext.getPlatformAccess().registerTimeStampReceiver();
        this.mPIContext.getPlatformAccess().registerScreenOnOffReceiver();
        this.mPIContext.getPlatformAccess().registerWakeUpAlarmUpdateReceiver();
        this.mPIContext.getPlatformAccess().registerNotificationActionReceiver();
        this.mPIContext.getPlatformAccess().registerUserChangedReceiver();
        this.mPIContext.getPlatformAccess().registerNightDisplayUpdateReceiver();
        this.mPIContext.getPlatformAccess().registerUnlockReceiver();
        this.mPIContext.getPlatformAccess().showModeUpgradeNotification();
    }

    public void inAutomaticState(AutomaticState automaticState) {
        LOGGER.mo11959e("Invalid state");
    }
}
