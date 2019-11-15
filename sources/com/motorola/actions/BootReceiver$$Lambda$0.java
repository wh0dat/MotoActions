package com.motorola.actions;

import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;

final /* synthetic */ class BootReceiver$$Lambda$0 implements Runnable {
    private final ComponentInfo arg$1;
    private final PackageManager arg$2;
    private final Context arg$3;

    BootReceiver$$Lambda$0(ComponentInfo componentInfo, PackageManager packageManager, Context context) {
        this.arg$1 = componentInfo;
        this.arg$2 = packageManager;
        this.arg$3 = context;
    }

    public void run() {
        BootReceiver.lambda$disableMotoActionsComponents$0$BootReceiver(this.arg$1, this.arg$2, this.arg$3);
    }
}
