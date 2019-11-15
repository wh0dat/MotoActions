package com.motorola.actions.lts.sensoraccess;

import p016io.reactivex.functions.Consumer;

final /* synthetic */ class LiftToSilenceSensorListener$$Lambda$0 implements Consumer {
    private final LiftToSilenceSensorListener arg$1;

    LiftToSilenceSensorListener$$Lambda$0(LiftToSilenceSensorListener liftToSilenceSensorListener) {
        this.arg$1 = liftToSilenceSensorListener;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$0$LiftToSilenceSensorListener((Float) obj);
    }
}
