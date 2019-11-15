package com.motorola.actions.ftm.statemachines;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubMotionHistoryListener;

final /* synthetic */ class FlipToMuteStateMachine$2$$Lambda$0 implements SensorHubMotionHistoryListener {
    private final C05632 arg$1;

    FlipToMuteStateMachine$2$$Lambda$0(C05632 r1) {
        this.arg$1 = r1;
    }

    public void onMotionHistory(int i) {
        this.arg$1.lambda$run$0$FlipToMuteStateMachine$2(i);
    }
}
