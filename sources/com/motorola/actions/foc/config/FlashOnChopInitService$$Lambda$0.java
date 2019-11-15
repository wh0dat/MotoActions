package com.motorola.actions.foc.config;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubResultListener;

final /* synthetic */ class FlashOnChopInitService$$Lambda$0 implements SensorHubResultListener {
    static final SensorHubResultListener $instance = new FlashOnChopInitService$$Lambda$0();

    private FlashOnChopInitService$$Lambda$0() {
    }

    public void onCommandComplete(boolean z) {
        FlashOnChopInitService.lambda$writeSensorHubConfig$0$FlashOnChopInitService(z);
    }
}
