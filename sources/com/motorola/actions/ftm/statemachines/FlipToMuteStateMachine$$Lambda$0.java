package com.motorola.actions.ftm.statemachines;

import p016io.reactivex.functions.Consumer;

final /* synthetic */ class FlipToMuteStateMachine$$Lambda$0 implements Consumer {
    private final FlipToMuteStateMachine arg$1;

    FlipToMuteStateMachine$$Lambda$0(FlipToMuteStateMachine flipToMuteStateMachine) {
        this.arg$1 = flipToMuteStateMachine;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$0$FlipToMuteStateMachine((Float) obj);
    }
}
