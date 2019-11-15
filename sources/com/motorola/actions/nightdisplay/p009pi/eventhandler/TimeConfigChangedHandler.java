package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.states.AutomaticState;
import com.motorola.actions.nightdisplay.p009pi.states.DisabledState;
import com.motorola.actions.nightdisplay.p009pi.states.PausedState;
import com.motorola.actions.nightdisplay.p009pi.states.UninitializedState;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.TimeConfigChangedHandler */
public final class TimeConfigChangedHandler extends EventHandler {
    private static final MALogger LOGGER = new MALogger(TimeConfigChangedHandler.class);
    private static final long TIME_DIFF_THRESHOLD = TimeUnit.MINUTES.toMillis(5);

    TimeConfigChangedHandler(PIContext pIContext, Calendar calendar) {
        super(pIContext, calendar);
    }

    public void inDisabledState(DisabledState disabledState) {
        if (isDiffGreaterThanThreshold()) {
            updateConfiguration();
            changeToAutomaticIfNeeded();
        }
    }

    public void inPausedState(PausedState pausedState) {
        if (isDiffGreaterThanThreshold()) {
            updateConfiguration();
            if (!this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
                this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
            } else {
                pausedState.update(this.mTimeStamp);
            }
        }
    }

    public void inInitialState(UninitializedState uninitializedState) {
        LOGGER.mo11959e("Invalid state");
    }

    public void inAutomaticState(AutomaticState automaticState) {
        if (isDiffGreaterThanThreshold()) {
            updateConfiguration();
            if (this.mPIContext.getSchedulingController().isWithinActiveTime(this.mTimeStamp)) {
                automaticState.update(this.mTimeStamp);
            } else {
                this.mPIContext.getStateController().changeState(new DisabledState(this.mPIContext), this.mTimeStamp);
            }
        }
    }

    private void updateConfiguration() {
        this.mPIContext.getSchedulingController().updateConfiguration(this.mTimeStamp, this.mPIContext.getPlatformAccess().getDefaultState());
    }

    private long getTimeDiff() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("New Timestamp: ");
        sb.append(this.mTimeStamp.getTimeInMillis());
        mALogger.mo11957d(sb.toString());
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Last Timestamp: ");
        sb2.append(this.mPIContext.getLastTimestamp());
        mALogger2.mo11957d(sb2.toString());
        return Math.abs(this.mTimeStamp.getTimeInMillis() - this.mPIContext.getLastTimestamp());
    }

    private boolean isDiffGreaterThanThreshold() {
        long timeDiff = getTimeDiff();
        boolean z = timeDiff > TIME_DIFF_THRESHOLD;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isDiffGreaterThanThreshold: ");
        sb.append(z);
        sb.append(", time diff (minutes): ");
        sb.append(TimeUnit.MILLISECONDS.toMinutes(timeDiff));
        mALogger.mo11957d(sb.toString());
        this.mPIContext.setLastTimestamp(this.mTimeStamp.getTimeInMillis());
        return z;
    }
}
