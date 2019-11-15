package com.motorola.actions.enhancedscreenshot;

final /* synthetic */ class FeatureManager$$Lambda$0 implements Runnable {
    static final Runnable $instance = new FeatureManager$$Lambda$0();

    private FeatureManager$$Lambda$0() {
    }

    public void run() {
        EnhancedScreenshotService.isServiceEnabled();
    }
}
