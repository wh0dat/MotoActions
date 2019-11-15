package com.motorola.actions.quickscreenshot;

import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.AndroidResourceAccess;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import java.util.Optional;

public class QuickScreenshotHelper {
    private static final MALogger LOGGER = new MALogger(QuickScreenshotHelper.class);
    private static final String PLATFORM_QUICK_SCREENSHOT_AVAILABLE = "config_quickScreenshotAvailable";

    public static boolean isFeatureSupported() {
        boolean z = false;
        if (!Device.isZuiDevice()) {
            Optional boolResource = AndroidResourceAccess.getBoolResource(PLATFORM_QUICK_SCREENSHOT_AVAILABLE);
            if (boolResource.isPresent()) {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("config_quickScreenshotAvailable: ");
                sb.append(false);
                mALogger.mo11957d(sb.toString());
            }
            z = ((Boolean) boolResource.orElse(Boolean.valueOf(false))).booleanValue();
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isFeatureSupported=");
        sb2.append(z);
        mALogger2.mo11957d(sb2.toString());
        return z;
    }

    public static boolean isEnabled() {
        boolean isQuickScreenshotEnabled = MotorolaSettings.isQuickScreenshotEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("quickScreenshotEnabled - ");
        sb.append(isQuickScreenshotEnabled);
        mALogger.mo11957d(sb.toString());
        return isQuickScreenshotEnabled;
    }

    public static boolean shouldShowWelcomeScreen() {
        return !isEnabled() && !SharedPreferenceManager.getBoolean(Constants.QUICK_SCREENSHOT_SCREEN_ALREADY_SHOWN, false);
    }
}
