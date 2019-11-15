package com.motorola.actions.nightdisplay.p009pi;

import com.motorola.actions.nightdisplay.common.PlatformAccess;
import com.motorola.actions.nightdisplay.p009pi.scheduling.SchedulingController;
import com.motorola.actions.nightdisplay.p009pi.states.StateController;

/* renamed from: com.motorola.actions.nightdisplay.pi.PIContext */
public class PIContext {
    private long mLastTimestamp;
    private final PlatformAccess mPlatformAccess;
    private final SchedulingController mSchedulingController = new SchedulingController();
    private final StateController mStateController;

    public PIContext(StateController stateController, PlatformAccess platformAccess) {
        this.mStateController = stateController;
        this.mPlatformAccess = platformAccess;
    }

    public StateController getStateController() {
        return this.mStateController;
    }

    public PlatformAccess getPlatformAccess() {
        return this.mPlatformAccess;
    }

    public SchedulingController getSchedulingController() {
        return this.mSchedulingController;
    }

    public void setLastTimestamp(long j) {
        this.mLastTimestamp = j;
    }

    public long getLastTimestamp() {
        return this.mLastTimestamp;
    }
}
