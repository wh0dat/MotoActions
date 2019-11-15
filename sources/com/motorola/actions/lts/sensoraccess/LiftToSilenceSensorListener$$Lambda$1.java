package com.motorola.actions.lts.sensoraccess;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubMotionHistoryListener;

final /* synthetic */ class LiftToSilenceSensorListener$$Lambda$1 implements SensorHubMotionHistoryListener {
    private final LiftToSilenceSensorListener arg$1;
    private final boolean arg$2;

    LiftToSilenceSensorListener$$Lambda$1(LiftToSilenceSensorListener liftToSilenceSensorListener, boolean z) {
        this.arg$1 = liftToSilenceSensorListener;
        this.arg$2 = z;
    }

    public void onMotionHistory(int i) {
        this.arg$1.mo11153x93f71535(this.arg$2, i);
    }
}
