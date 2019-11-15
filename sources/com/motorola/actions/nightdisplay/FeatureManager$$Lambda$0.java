package com.motorola.actions.nightdisplay;

import android.content.Context;

final /* synthetic */ class FeatureManager$$Lambda$0 implements Runnable {
    private final Context arg$1;

    FeatureManager$$Lambda$0(Context context) {
        this.arg$1 = context;
    }

    public void run() {
        FeatureManager.start(this.arg$1);
    }
}
