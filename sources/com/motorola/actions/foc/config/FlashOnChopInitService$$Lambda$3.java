package com.motorola.actions.foc.config;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubResultListener;

final /* synthetic */ class FlashOnChopInitService$$Lambda$3 implements SensorHubResultListener {
    static final SensorHubResultListener $instance = new FlashOnChopInitService$$Lambda$3();

    private FlashOnChopInitService$$Lambda$3() {
    }

    public void onCommandComplete(boolean z) {
        FlashOnChopInitService.lambda$writeSensorHubConfig$3$FlashOnChopInitService(z);
    }
}
