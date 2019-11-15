package com.motorola.actions.fpsslide;

import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.MALogger;

public class FPSSlideHelper {
    private static final MALogger LOGGER = new MALogger(FPSSlideHelper.class);
    private static final String PLATFORM_ENABLE_FPS_SLIDE = "config_enableSideFingerprintGesture";

    public static boolean isFeatureSupported() {
        boolean booleanValue = ((Boolean) AndroidResourceAccess.getBoolResource(PLATFORM_ENABLE_FPS_SLIDE).orElse(Boolean.valueOf(false))).booleanValue();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isFeatureSupported=");
        sb.append(booleanValue);
        mALogger.mo11957d(sb.toString());
        return booleanValue;
    }

    public static boolean isEnabled() {
        return FPSSlideModel.isServiceEnabled();
    }

    private static void setServiceEnabled(boolean z) {
        FPSSlideModel.setServiceEnabled(z);
    }

    public static boolean isReversedScroll() {
        return FPSSlideModel.isReversedScroll();
    }

    public static void setReversedScroll(boolean z) {
        FPSSlideModel.setReversedScroll(z);
    }

    public static boolean isScrollOnHomeEnabled() {
        return FPSSlideModel.isScrollOnHomeEnabled();
    }

    public static void setScrollOnHome(boolean z) {
        FPSSlideModel.setScrollOnHome(z);
        updateFPSSlideStatus(z);
    }

    public static boolean isScrollOnAppEnabled() {
        return FPSSlideModel.isScrollOnAppEnabled();
    }

    public static void setScrollOnApp(boolean z) {
        FPSSlideModel.setScrollOnApp(z);
        updateFPSSlideStatus(z);
    }

    private static void updateFPSSlideStatus(boolean z) {
        if (z || isScrollOnHomeEnabled() || isScrollOnAppEnabled()) {
            setServiceEnabled(true);
        } else {
            setServiceEnabled(false);
        }
    }
}
