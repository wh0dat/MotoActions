package com.motorola.actions.foc.config;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubResultListener;

final /* synthetic */ class FlashOnChopInitService$$Lambda$2 implements SensorHubResultListener {
    static final SensorHubResultListener $instance = new FlashOnChopInitService$$Lambda$2();

    private FlashOnChopInitService$$Lambda$2() {
    }

    public void onCommandComplete(boolean z) {
        FlashOnChopInitService.lambda$writeSensorHubConfig$2$FlashOnChopInitService(z);
    }
}
