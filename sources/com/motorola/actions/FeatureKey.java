package com.motorola.actions;

import android.os.Build;

public enum FeatureKey {
    NOT_VALID(false),
    QUICK_CAPTURE(true),
    APPROACH(!Build.ID.startsWith("MCN")),
    FLASH_ON_CHOP(true),
    RISE_TO_EAR(false),
    ATTENTIVE_DISPLAY(false),
    MICROSCREEN(false),
    PICKUP_TO_STOP_RINGING(false),
    FLIP_TO_DND(false),
    NIGHT_DISPLAY(false),
    ONE_NAV(false),
    QUICK_SCREENSHOT(true),
    ENHANCED_SCREENSHOT(true),
    MEDIA_CONTROL(false),
    FPS_SLIDE_HOME(false),
    FPS_SLIDE_APP(false),
    LIFT_TO_UNLOCK(false),
    SLEEP_PATTERN(false);
    
    private boolean mDefaultEnableState;

    private FeatureKey(boolean z) {
        this.mDefaultEnableState = z;
    }

    public boolean getEnableDefaultState() {
        return this.mDefaultEnableState;
    }

    public static FeatureKey getFeatureKey(int i) {
        try {
            return values()[i];
        } catch (IndexOutOfBoundsException unused) {
            return NOT_VALID;
        }
    }
}
