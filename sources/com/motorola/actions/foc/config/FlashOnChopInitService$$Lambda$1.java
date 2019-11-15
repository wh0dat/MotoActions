package com.motorola.actions.foc.config;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubResultListener;

final /* synthetic */ class FlashOnChopInitService$$Lambda$1 implements SensorHubResultListener {
    static final SensorHubResultListener $instance = new FlashOnChopInitService$$Lambda$1();

    private FlashOnChopInitService$$Lambda$1() {
    }

    public void onCommandComplete(boolean z) {
        FlashOnChopInitService.lambda$writeSensorHubConfig$1$FlashOnChopInitService(z);
    }
}
