package com.motorola.actions.lts.instrumentation;

import android.os.PowerManager;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;

final /* synthetic */ class LiftToSilenceInstrumentation$$Lambda$4 implements Runnable {
    static final Runnable $instance = new LiftToSilenceInstrumentation$$Lambda$4();

    private LiftToSilenceInstrumentation$$Lambda$4() {
    }

    public void run() {
        SharedPreferenceManager.putBoolean(LiftToSilenceInstrumentation.KEY_SCREEN_STATE_BEFORE_CALL, ((PowerManager) ActionsApplication.getAppContext().getSystemService(PowerManager.class)).isInteractive());
    }
}
