package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.StateController */
public class StateController {
    private static final MALogger LOGGER = new MALogger(StateController.class);
    public static final String NIGHT_DISPLAY_STATE = "night_display_state";
    private State mCurrentState = new UninitializedState();

    public void changeState(State state, Calendar calendar) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Changing from ");
        sb.append(this.mCurrentState.getClass().getSimpleName());
        sb.append(" to ");
        sb.append(state.getClass().getSimpleName());
        mALogger.mo11957d(sb.toString());
        this.mCurrentState = state;
        this.mCurrentState.enterState(calendar);
        SharedPreferenceManager.putString(NIGHT_DISPLAY_STATE, this.mCurrentState.getClass().getSimpleName());
    }

    public void onEvent(EventHandler eventHandler) {
        this.mCurrentState.takeEventHandler(eventHandler);
    }
}
