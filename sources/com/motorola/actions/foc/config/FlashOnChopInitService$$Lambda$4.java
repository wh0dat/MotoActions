package com.motorola.actions.foc.config;

import com.motorola.actions.approach.p005ir.SensorHub.SensorHubResultListener;

final /* synthetic */ class FlashOnChopInitService$$Lambda$4 implements SensorHubResultListener {
    static final SensorHubResultListener $instance = new FlashOnChopInitService$$Lambda$4();

    private FlashOnChopInitService$$Lambda$4() {
    }

    public void onCommandComplete(boolean z) {
        FlashOnChopInitService.lambda$writeSensorHubConfig$4$FlashOnChopInitService(z);
    }
}
