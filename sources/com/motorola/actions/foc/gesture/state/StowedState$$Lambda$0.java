package com.motorola.actions.foc.gesture.state;

import p016io.reactivex.functions.Consumer;

final /* synthetic */ class StowedState$$Lambda$0 implements Consumer {
    private final StowedState arg$1;

    StowedState$$Lambda$0(StowedState stowedState) {
        this.arg$1 = stowedState;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$0$StowedState((Float) obj);
    }
}
