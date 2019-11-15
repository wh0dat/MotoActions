package com.motorola.actions.lts.instrumentation;

import android.os.SystemClock;

final /* synthetic */ class LiftToSilenceInstrumentation$$Lambda$2 implements Runnable {
    static final Runnable $instance = new LiftToSilenceInstrumentation$$Lambda$2();

    private LiftToSilenceInstrumentation$$Lambda$2() {
    }

    public void run() {
        LiftToSilenceInstrumentation.sStartTimeCounter = SystemClock.elapsedRealtime();
    }
}
