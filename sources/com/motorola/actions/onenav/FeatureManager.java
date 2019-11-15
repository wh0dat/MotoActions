package com.motorola.actions.onenav;

import com.motorola.actions.onenav.instrumentation.SoftOneNavInstrumentationObserver;
import com.motorola.actions.reflect.MotorolaSettings;

public class FeatureManager {
    public static void start() {
        if (OneNavHelper.isOneNavPresent()) {
            MotorolaSettings.setOneNavTutorialInactive();
            OneNavHelper.registerObserver();
            SoftOneNavInstrumentationObserver.registerObserver();
            if (OneNavHelper.shouldDisableFPSLock()) {
                MotorolaSettings.setFingerPrintLockScreen(false);
            }
        }
    }

    public static void stop() {
        if (OneNavHelper.isOneNavPresent()) {
            OneNavHelper.unregisterObserver();
            SoftOneNavInstrumentationObserver.unregisterObserver();
        }
    }
}
