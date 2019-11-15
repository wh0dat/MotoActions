package com.motorola.actions.nightdisplay;

import android.net.Uri;
import java.util.function.Consumer;

final /* synthetic */ class NightDisplaySetupService$$Lambda$2 implements Consumer {
    private final NightDisplaySetupService arg$1;

    NightDisplaySetupService$$Lambda$2(NightDisplaySetupService nightDisplaySetupService) {
        this.arg$1 = nightDisplaySetupService;
    }

    public void accept(Object obj) {
        this.arg$1.bridge$lambda$0$NightDisplaySetupService((Uri) obj);
    }
}
