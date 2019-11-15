package com.motorola.actions.nightdisplay.p008pd;

import android.os.Bundle;

/* renamed from: com.motorola.actions.nightdisplay.pd.PDNightDisplayController$$Lambda$0 */
final /* synthetic */ class PDNightDisplayController$$Lambda$0 implements Runnable {
    private final PDNightDisplayController arg$1;
    private final Bundle arg$2;

    PDNightDisplayController$$Lambda$0(PDNightDisplayController pDNightDisplayController, Bundle bundle) {
        this.arg$1 = pDNightDisplayController;
        this.arg$2 = bundle;
    }

    public void run() {
        this.arg$1.lambda$start$0$PDNightDisplayController(this.arg$2);
    }
}
