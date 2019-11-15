package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import android.os.Bundle;
import com.motorola.actions.nightdisplay.p008pd.NightDisplayUpdateReceiver;
import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.PreviewUpdateEventHandler */
public final class PreviewUpdateEventHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(PreviewUpdateEventHandler.class);
    private final int mIntensity;

    PreviewUpdateEventHandler(PIContext pIContext, Calendar calendar, Bundle bundle) {
        super(pIContext, calendar);
        this.mIntensity = bundle.getInt(NightDisplayUpdateReceiver.EXTRA_INTENSITY, 0);
    }

    public void inDisabledState(DisabledState disabledState) {
        updateIntensity();
    }

    public void inPausedState(PausedState pausedState) {
        updateIntensity();
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        updateIntensity();
    }

    private void updateIntensity() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Preview: updateIntensity ");
        sb.append(this.mIntensity);
        mALogger.mo11957d(sb.toString());
        this.mPIContext.getPlatformAccess().enableTwilight(this.mIntensity);
    }
}
